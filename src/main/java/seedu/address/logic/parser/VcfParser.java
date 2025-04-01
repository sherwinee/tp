package seedu.address.logic.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.property.FormattedName;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;

/**
 * Parses VCF files using the ez-vcard library and returns a list of Person objects.
 */
public class VcfParser {

    /**
     * Parses the given .vcf file into a list of Person objects.
     *
     * @param filePath The path to the VCF file.
     * @return A list of parsed Person objects.
     * @throws IOException If reading the file fails or rows contain invalid data.
     */
    public static List<Person> parseVcf(String filePath) throws IOException {
        List<Person> persons = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<VCard> vcards = Ezvcard.parse(new File(filePath)).all();

        for (int i = 0; i < vcards.size(); i++) {
            VCard vcard = vcards.get(i);
            int rowNumber = i + 1;
            String fullName = "";
            try {
                fullName = parseName(vcard, rowNumber, errors);
                String phone = parsePhone(vcard, fullName, errors);
                String email = parseEmail(vcard, fullName, errors);
                String address = parseAddress(vcard, fullName, errors);
                String role = parseRole(vcard, fullName, errors);

                if (!errors.isEmpty()) {
                    continue;
                }

                persons.add(new Person(new Name(fullName), new Phone(phone), new Email(email), new Address(address),
                        new Role(role), new HashSet<>(), Optional.empty()
                ));

            } catch (Exception e) {
                errors.add((fullName.isEmpty() ? "Contact" + rowNumber : fullName) + ": " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new IOException("VCF Import failed due to:\n" + String.join("\n", errors));
        }

        return persons;
    }

    private static String parseName(VCard vcard, int rowNumber, List<String> errors) {
        if (vcard.getFormattedNames().size() > 1) {
            errors.add("Contact " + rowNumber + " contains more than one formatted name");
        }

        FormattedName formattedName = vcard.getFormattedName();
        if (formattedName == null || isBlank(formattedName.getValue())) {
            errors.add("Contact " + rowNumber + ": Missing required field 'FN' (Formatted Name)");
            return "";
        }

        return formattedName.getValue().trim();
    }

    private static String parsePhone(VCard vcard, String name, List<String> errors) {
        if (vcard.getTelephoneNumbers().size() > 1) {
            errors.add(name + " contains more than one telephone number");
        }
        if (vcard.getTelephoneNumbers().isEmpty() || isBlank(vcard.getTelephoneNumbers().get(0).getText())) {
            errors.add(name + ": Missing required field 'Phone'");
            return "";
        }

        return vcard.getTelephoneNumbers().get(0).getText().trim();
    }

    private static String parseEmail(VCard vcard, String name, List<String> errors) {
        if (vcard.getEmails().size() > 1) {
            errors.add(name + " contains more than one email address");
        }
        if (vcard.getEmails().isEmpty() || isBlank(vcard.getEmails().get(0).getValue())) {
            errors.add(name + ": Missing required field 'Email'");
            return "";
        }

        return vcard.getEmails().get(0).getValue().trim();
    }

    private static String parseAddress(VCard vcard, String name, List<String> errors) {
        if (vcard.getAddresses().isEmpty()) {
            errors.add(name + ": Missing required field 'Address'");
            return "";
        }

        ezvcard.property.Address adr = vcard.getAddresses().get(0);
        List<String> parts = new ArrayList<>();

        if (!isBlank(adr.getPoBox())) {
            parts.add(adr.getPoBox().trim());
        }

        String fullStreet = "";
        if (!isBlank(adr.getExtendedAddressFull())) {
            fullStreet += adr.getExtendedAddressFull().trim();
        }
        if (!isBlank(adr.getStreetAddressFull())) {
            if (!fullStreet.isEmpty()) {
                fullStreet += ", ";
            }
            fullStreet += adr.getStreetAddressFull().trim();
        }
        if (!fullStreet.isEmpty()) {
            parts.add(fullStreet);
        }

        if (!isBlank(adr.getLocality())) {
            parts.add(adr.getLocality().trim());
        }
        if (!isBlank(adr.getRegion())) {
            parts.add(adr.getRegion().trim());
        }
        if (!isBlank(adr.getPostalCode())) {
            parts.add(adr.getPostalCode().trim());
        }
        if (!isBlank(adr.getCountry())) {
            parts.add(adr.getCountry().trim());
        }

        return String.join(", ", parts);
    }

    private static String parseRole(VCard vcard, String name, List<String> errors) {
        if (vcard.getTitles().size() > 1) {
            errors.add(name + " contains more than one title");
        }

        if (!vcard.getTitles().isEmpty()) {
            String titleValue = vcard.getTitles().get(0).getValue();
            if (!isBlank(titleValue)) {
                return titleValue.trim();
            }
        }

        return "Unassigned";
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

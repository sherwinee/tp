package seedu.address.logic.parser;

import java.io.File;
import java.io.IOException;
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

    private static List<String> lastParseErrors = new ArrayList<>();

    /**
     * Parses the given .vcf file into a list of Person objects.
     *
     * @param filePath The path to the VCF file.
     * @return A list of parsed Person objects.
     * @throws IOException If reading the file fails.
     */
    public static List<Person> parseVcf(String filePath) throws IOException {
        lastParseErrors.clear();
        List<Person> persons = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<VCard> vcards;
        assert filePath != null : "File path should not be null";

        try {
            vcards = Ezvcard.parse(new File(filePath)).all();
        } catch (IOException e) {
            throw new IOException("Failed to read VCF file: " + e.getMessage(), e);
        }

        for (int i = 0; i < vcards.size(); i++) {
            VCard vcard = vcards.get(i);
            int rowNumber = i + 1;
            String fullName = "";
            List<String> contactErrors = new ArrayList<>();

            try {
                fullName = parseName(vcard, rowNumber, contactErrors);
            } catch (Exception e) {
                contactErrors.add("Error parsing name - " + e.getMessage());
            }

            Result result = getResult(fullName, rowNumber, vcard, contactErrors);

            // Only create and add the Person if there are no errors
            if (contactErrors.isEmpty()) {
                try {
                    persons.add(new Person(new Name(fullName), new Phone(result.phoneText()),
                            new Email(result.emailText()), new Address(result.addressText()),
                            new Role(result.roleText()), new HashSet<>(), Optional.empty()));
                } catch (Exception e) {
                    contactErrors.add("Error creating person - " + e.getMessage());
                }
            }

            // Add all errors for this contact to the global errors list
            for (String error : contactErrors) {
                errors.add(result.contactIdentifier() + ": " + error);
            }
        }

        // Store errors in a static field for retrieval by ImportCommand
        if (!errors.isEmpty()) {
            storeErrors(errors);
        }

        return persons;
    }


    /**
     * Stores the errors from the last parse operation.
     *
     * @param errors The errors to store.
     */
    private static void storeErrors(List<String> errors) {
        lastParseErrors.clear();
        lastParseErrors.addAll(errors);
    }

    /**
     * Gets the errors from the last parse operation.
     *
     * @return The errors from the last parse operation.
     */
    public static List<String> getLastParseErrors() {
        return new ArrayList<>(lastParseErrors);
    }

    private static Result getResult(String fullName, int rowNumber, VCard vcard, List<String> contactErrors) {
        ParseFields fields = getParseFields(fullName, rowNumber, vcard, contactErrors);

        // Validate all fields explicitly
        validateField(fullName, Name.class, contactErrors);
        validateField(fields.phoneText(), Phone.class, contactErrors);
        validateField(fields.emailText(), Email.class, contactErrors);
        validateField(fields.addressText(), Address.class, contactErrors);
        validateField(fields.roleText(), Role.class, contactErrors);

        Result result = new Result(fields.contactIdentifier(), fields.phoneText(), fields.emailText(),
                fields.addressText(), fields.roleText());
        return result;
    }

    private static void validateField(String value, Class<?> fieldClass, List<String> contactErrors) {
        if (value.isEmpty()) {
            return;
        }

        try {
            if (fieldClass == Name.class) {
                new Name(value);
            } else if (fieldClass == Phone.class) {
                new Phone(value);
            } else if (fieldClass == Email.class) {
                new Email(value);
            } else if (fieldClass == Address.class) {
                new Address(value);
            } else if (fieldClass == Role.class) {
                new Role(value);
            }
        } catch (IllegalArgumentException e) {
            contactErrors.add("Invalid field - " + e.getMessage());
        }
    }

    private static ParseFields getParseFields(String fullName, int rowNumber, VCard vcard, List<String> contactErrors) {
        String contactIdentifier = createContactIdentifier(fullName, rowNumber);

        String phoneText = extractField(vcard, contactIdentifier, contactErrors, (
                v, id, errors) -> parsePhone(v, id, errors), "phone");

        String emailText = extractField(vcard, contactIdentifier, contactErrors, (
                v, id, errors) -> parseEmail(v, id, errors), "email");

        String addressText = extractField(vcard, contactIdentifier, contactErrors, (
                v, id, errors) -> parseAddress(v, id, errors), "address");

        String roleText = extractField(vcard, contactIdentifier, contactErrors, (
                v, id, errors) -> parseRole(v, id, errors), "role");

        return new ParseFields(contactIdentifier, phoneText, emailText, addressText, roleText);
    }

    private static String createContactIdentifier(String fullName, int rowNumber) {
        return fullName.isEmpty() ? "Contact " + rowNumber : "Contact Number " + rowNumber + " " + fullName;
    }

    private static String extractField(VCard vcard, String contactIdentifier, List<String> contactErrors,
                                       FieldExtractor extractor, String fieldName) {
        try {
            return extractor.extract(vcard, contactIdentifier, contactErrors);
        } catch (Exception e) {
            contactErrors.add("Error parsing " + fieldName + " - " + e.getMessage());
            return "";
        }
    }

    @FunctionalInterface
    private interface FieldExtractor {
        String extract(VCard vcard, String contactIdentifier, List<String> errors);
    }

    private record ParseFields(String contactIdentifier, String phoneText, String emailText, String addressText,
                               String roleText) {
    }

    private record Result(String contactIdentifier, String phoneText, String emailText, String addressText,
                          String roleText) {
    }

    private static String parseName(VCard vcard, int rowNumber, List<String> errors) {
        if (vcard.getFormattedNames().size() > 1) {
            errors.add("Cannot contain more than one formatted name");
        }

        FormattedName formattedName = vcard.getFormattedName();
        if (formattedName == null || isBlank(formattedName.getValue())) {
            errors.add("Missing required field 'FN' (Formatted Name)");
            return "";
        }

        return formattedName.getValue().trim();
    }

    private static String parsePhone(VCard vcard, String name, List<String> errors) {
        if (vcard.getTelephoneNumbers().size() > 1) {
            errors.add("Cannot contain more than one telephone number");
        }
        if (vcard.getTelephoneNumbers().isEmpty() || isBlank(vcard.getTelephoneNumbers().get(0).getText())) {
            errors.add("Missing required field 'Phone'");
            return "";
        }

        return vcard.getTelephoneNumbers().get(0).getText().trim();
    }

    private static String parseEmail(VCard vcard, String name, List<String> errors) {
        if (vcard.getEmails().size() > 1) {
            errors.add("Cannot contain more than one email address");
        }
        if (vcard.getEmails().isEmpty() || isBlank(vcard.getEmails().get(0).getValue())) {
            errors.add("Missing required field 'Email'");
            return "";
        }

        return vcard.getEmails().get(0).getValue().trim();
    }

    private static String parseAddress(VCard vcard, String name, List<String> errors) {
        if (vcard.getAddresses().isEmpty()) {
            errors.add("Missing required field 'Address'");
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
            errors.add("Cannot contain more than one title");
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

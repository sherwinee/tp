package seedu.address.storage;

import java.util.List;

import ezvcard.VCard;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;

/**
 * Contains methods to map Lists of VcfAdaptedPerson objects to list of Vcards
 */
public class VcfMapper {
    /**
     * Maps a given List of VcfAdaptedPerson objects to list of Vcard
     * @param persons List of VcfAdaptedPersons to map
     */
    public static List<VCard> mapToVcards(List<VcfAdaptedPerson> persons) {
        return persons.stream()
                .map(VcfMapper::toVcard)
                .toList();
    }

    private static VCard toVcard(VcfAdaptedPerson person) {
        VCard vcard = new VCard();
        vcard.setFormattedName(person.getFn());
        vcard.addTelephoneNumber(person.getTel(), TelephoneType.CELL);
        vcard.addEmail(person.getEmail());

        Address address = new Address();
        address.setStreetAddress(person.getAdr());
        vcard.addAddress(address);

        vcard.addTitle(person.getTitle());

        return vcard;
    }
}

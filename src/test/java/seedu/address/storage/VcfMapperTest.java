package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ezvcard.VCard;
import ezvcard.property.Address;
import seedu.address.testutil.TypicalPersons;

public class VcfMapperTest {

    @Test
    public void mapToVcards_typicalData_correctlyMapped() {
        // Arrange: Convert typical AB3 persons to VcfAdaptedPersons
        List<VcfAdaptedPerson> adaptedPersons = TypicalPersons.getTypicalAddressBook()
                .getPersonList()
                .stream()
                .map(VcfAdaptedPerson::new)
                .toList();

        // Act: Map to vCards
        List<VCard> vcards = VcfMapper.mapToVcards(adaptedPersons);

        // Assert: Same number of entries
        assertEquals(adaptedPersons.size(), vcards.size());

        // Check a few fields of the first contact
        VcfAdaptedPerson original = adaptedPersons.get(0);
        VCard vcard = vcards.get(0);

        assertEquals(original.getFn(), vcard.getFormattedName().getValue());
        assertEquals(original.getTel(), vcard.getTelephoneNumbers().get(0).getText());
        assertEquals(original.getEmail(), vcard.getEmails().get(0).getValue());

        Address vcardAddress = vcard.getAddresses().get(0);
        assertEquals(original.getAdr(), vcardAddress.getStreetAddress());

        assertEquals(original.getTitle(), vcard.getTitles().get(0).getValue());
    }

    @Test
    public void mapToVcards_emptyList_returnsEmptyList() {
        List<VCard> vcards = VcfMapper.mapToVcards(List.of());
        assertEquals(0, vcards.size());
    }
}

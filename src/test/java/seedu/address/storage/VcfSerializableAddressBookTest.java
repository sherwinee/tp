package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.testutil.TypicalPersons;

public class VcfSerializableAddressBookTest {

    @Test
    public void constructor_fromReadOnlyAddressBook_returnsCorrectPersons() {
        // Use the typical test data
        ReadOnlyAddressBook typicalAddressBook = TypicalPersons.getTypicalAddressBook();
        VcfSerializableAddressBook vcfAddressBook = new VcfSerializableAddressBook(typicalAddressBook);

        // Size should match
        assertEquals(typicalAddressBook.getPersonList().size(), vcfAddressBook.getPersons().size());

        // Check one or two representative fields from the first person
        VcfAdaptedPerson vcfPerson = vcfAddressBook.getPersons().get(0);
        assertEquals(typicalAddressBook.getPersonList().get(0).getName().toString(), vcfPerson.getFn());
        assertEquals(typicalAddressBook.getPersonList().get(0).getPhone().toString(), vcfPerson.getTel());
        assertEquals(typicalAddressBook.getPersonList().get(0).getEmail().toString(), vcfPerson.getEmail());
        assertEquals(typicalAddressBook.getPersonList().get(0).getAddress().toString(), vcfPerson.getAdr());
        assertEquals(typicalAddressBook.getPersonList().get(0).getRole().toString(), vcfPerson.getTitle());
    }
}

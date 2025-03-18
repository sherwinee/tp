package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.testutil.TypicalPersons;

public class CsvSerializableAddressBookTest {

    @Test
    public void constructor_withJsonCreator_returnsCorrectPersons() {
        // Create a list of CsvAdaptedPerson from a typical address book.
        ReadOnlyAddressBook typicalAddressBook = TypicalPersons.getTypicalAddressBook();
        List<CsvAdaptedPerson> csvPersons = typicalAddressBook.getPersonList().stream()
                .map(CsvAdaptedPerson::new)
                .toList();

        // Use the @JsonCreator constructor.
        CsvSerializableAddressBook csvAddressBook = new CsvSerializableAddressBook(csvPersons);

        // Verify that the list size matches and key field(s) are correctly stored.
        assertEquals(csvPersons.size(), csvAddressBook.getPersons().size());
        assertEquals(csvPersons.get(0).getName(), csvAddressBook.getPersons().get(0).getName());
    }

    @Test
    public void constructor_fromReadOnlyAddressBook_returnsCorrectPersons() {
        // Use the constructor that takes a ReadOnlyAddressBook.
        ReadOnlyAddressBook typicalAddressBook = TypicalPersons.getTypicalAddressBook();
        CsvSerializableAddressBook csvAddressBook = new CsvSerializableAddressBook(typicalAddressBook);

        // The number of persons should match.
        assertEquals(typicalAddressBook.getPersonList().size(), csvAddressBook.getPersons().size());
        // Optionally, verify that a specific field (e.g. name) is correctly set.
        assertEquals(typicalAddressBook.getPersonList().get(0).getName().toString(),
                csvAddressBook.getPersons().get(0).getName());
    }
}

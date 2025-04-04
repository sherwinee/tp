package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.model.ReadOnlyAddressBook;

/**
 * An Immutable AddressBook that is serializable to CSV format.
 * Follows the same pattern as JsonSerializableAddressBook.
 */
@JsonRootName(value = "addressbook") // optional
class CsvSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<CsvAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code CsvSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public CsvSerializableAddressBook(@JsonProperty("persons") List<CsvAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     */
    public CsvSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(
                source.getPersonList().stream()
                        .map(CsvAdaptedPerson::new)
                        .collect(Collectors.toList())
        );
    }

    public List<CsvAdaptedPerson> getPersons() {
        return persons;
    }
}

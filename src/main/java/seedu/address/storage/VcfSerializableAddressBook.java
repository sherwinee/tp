package seedu.address.storage;

import seedu.address.model.ReadOnlyAddressBook;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VcfSerializableAddressBook {
    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";

    private final List<VcfAdaptedPerson> persons = new ArrayList<>();

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for ez-vcard use.
     */
    public VcfSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(
                source.getPersonList().stream()
                        .map(VcfAdaptedPerson::new)
                        .toList()
        );
    }

    public List<VcfAdaptedPerson> getPersons() {
        return persons;
    }
}

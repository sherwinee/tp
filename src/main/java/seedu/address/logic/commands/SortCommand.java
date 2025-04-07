package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Sorts the contact list in ascending or descending order.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_SUCCESS = "Contacts sorted by name (then phone) in %s order.";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sort contacts by name in ascending or descending order.\n"
            + "Parameters: asc or desc\n"
            + "Example: " + COMMAND_WORD + " asc";

    public static final String MESSAGE_NO_CONTACTS = "No contacts to sort.";

    private final boolean isAscending;

    public SortCommand(boolean isAscending) {
        this.isAscending = isAscending;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        if (model.getFilteredPersonList().isEmpty()) {
            return new CommandResult(MESSAGE_NO_CONTACTS);
        }

        Comparator<Person> comparator = Comparator.comparing(Person::getName)
                .thenComparing(Person::getPhone);

        if (!isAscending) {
            comparator = comparator.reversed();
        }

        model.sortFilteredPersonList(comparator);

        return new CommandResult(String.format(MESSAGE_SUCCESS, isAscending ? "ascending" : "descending"));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SortCommand otherCommand)) {
            return false;
        }
        return this.isAscending == otherCommand.isAscending;
    }
}

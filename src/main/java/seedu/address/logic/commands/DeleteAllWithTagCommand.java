package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteAllWithTagCommand extends Command {

    public static final String COMMAND_WORD = "deletetag";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes all person(s) associated with the specified tag in the person list.\n"
            + "Parameters: TAG\n"
            + "Example: " + COMMAND_WORD + " wedding";

    private final Predicate<Person> predicate;

    public DeleteAllWithTagCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        int latestSize = model.getFilteredPersonList().size();

        model.deleteAllPersons(this.predicate);

        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        int newSize = model.getFilteredPersonList().size();
        // should change to more meaningful message
        return new CommandResult(String.format(Messages.MESSAGE_PERSONS_DELETED_OVERVIEW,
      latestSize - newSize));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteAllWithTagCommand)) {
            return false;
        }

        DeleteAllWithTagCommand otherDeleteAllWithTagCommand = (DeleteAllWithTagCommand) other;
        return predicate.equals(otherDeleteAllWithTagCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}

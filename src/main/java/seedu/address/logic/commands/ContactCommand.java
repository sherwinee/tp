package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Marks a person as contacted and updates the last contacted time.
 */
public class ContactCommand extends Command {

    public static final String COMMAND_WORD = "contact";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the person at the specified index as contacted.\n"
            + "Parameters: INDEX (must be a positive integer between 1 and " + Integer.MAX_VALUE + ")\n"
            + "Example: " + COMMAND_WORD + " 2";

    public static final String MESSAGE_SUCCESS = "Contact %d marked as contacted.";

    public static final String MESSAGE_INVALID_INDEX = "Invalid index number.";

    private final Index targetIndex;

    public ContactCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX);
        }

        Person personToMark = lastShownList.get(targetIndex.getZeroBased());
        Person updatedPerson = personToMark.markAsContacted();

        model.setPerson(personToMark, updatedPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS, targetIndex.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ContactCommand
                && targetIndex.equals(((ContactCommand) other).targetIndex));
    }
}

package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Collections;
import java.util.List;

import seedu.address.logic.commands.DeleteAllCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagsContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class DeleteAllCommandParser implements Parser<DeleteAllCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteAllCommand
     * and returns a DeleteAllCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteAllCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAllCommand.MESSAGE_USAGE));
        }

        List<String> keyword = Collections.singletonList(trimmedArgs);

        return new DeleteAllCommand(new TagsContainsKeywordsPredicate(keyword));
    }
}

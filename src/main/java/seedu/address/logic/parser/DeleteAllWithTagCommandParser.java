package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Collections;
import java.util.List;

import seedu.address.logic.commands.DeleteAllWithTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagMatchesKeywordPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class DeleteAllWithTagCommandParser implements Parser<DeleteAllWithTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteAllWithTagCommand
     * and returns a DeleteAllWithTagCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteAllWithTagCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty() || trimmedArgs.matches(".*\\s+.*")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAllWithTagCommand.MESSAGE_USAGE));
        }

        List<String> keyword = Collections.singletonList(trimmedArgs);

        return new DeleteAllWithTagCommand(new TagMatchesKeywordPredicate(keyword));
    }
}

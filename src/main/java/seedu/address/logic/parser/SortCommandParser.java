package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SortCommand object.
 */
public class SortCommandParser implements Parser<SortCommand> {
    @Override
    public SortCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.equalsIgnoreCase("asc")) {
            return new SortCommand(true); // Ascending order
        } else if (trimmedArgs.equalsIgnoreCase("desc")) {
            return new SortCommand(false); // Descending order
        } else {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }
    }
}

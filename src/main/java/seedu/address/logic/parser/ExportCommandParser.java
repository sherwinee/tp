package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    @Override
    public ExportCommand parse(String userInput) throws ParseException {
        String trimmedFilename = userInput.trim();
        if (trimmedFilename.isEmpty() || !validateFilename(trimmedFilename)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }

        return new ExportCommand(trimmedFilename);
    }

    private boolean validateFilename(String filename) {
        if (filename.toLowerCase().endsWith(".csv")) {
            return true;
        }
        return false;
    }
}
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.nio.file.Paths;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ImportCommand object.
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    public static final String FILE_PATH_PREFIX = "imports/";
    public static final String INVALID_PATH_MESSAGE = "File name cannot contain '/' or '\\'.";

    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand
     * and returns an ImportCommand object for execution.
     * @param args The user input argument representing the file path.
     * @return An {@code ImportCommand} object with the specified file path.
     * @throws ParseException If the input is invalid or has an incorrect file extension.
     */
    public ImportCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();
        assert trimmedArgs != null : "Trimmed arguments should not be null";

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }
        assert !trimmedArgs.isEmpty() : "File path should not be empty after trimming";


        if (trimmedArgs.contains("/") || trimmedArgs.contains("\\")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, INVALID_PATH_MESSAGE));
        }


        if (!(trimmedArgs.toLowerCase().endsWith(".csv") || trimmedArgs.toLowerCase().endsWith(".vcf"))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_CONSTRAINTS));
        }


        return new ImportCommand(Paths.get(FILE_PATH_PREFIX + trimmedArgs));
    }
}

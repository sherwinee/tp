package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Imports a file ending with CSV format
 */
public class ImportCommand extends Command{

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports all data from the specified filepath "
            + "(case-sensitive) and displays them as list with index numbers.\n"
            + "Parameters: KEYWORD [filename.csv]\n"
            + "Example: " + COMMAND_WORD + "hello.csv";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Import command not implemented yet!";

    public static final String MESSAGE_ARGUMENTS = "Filename: %s";

    private final Path filePath;

    /**
     * @param filePath of the filename to be imported
     *
     */
    public ImportCommand(Path filePath) {
        this.filePath = filePath;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(String.format(MESSAGE_ARGUMENTS, filePath.getFileName().toString()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ImportCommand)) {
            return false;
        }

        ImportCommand e = (ImportCommand) other;
        return filePath.equals(e.filePath);
    }

}

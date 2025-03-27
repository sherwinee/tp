package seedu.address.logic.commands;

import static seedu.address.storage.CsvAddressBookStorage.EXPORT_DIR_PREFIX;

import java.io.IOException;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.storage.CsvAddressBookStorage;

/**
 * Exports all contacts to a CSV file, which can then be imported to AB3.
 * vcf file format support will be implemented at a future date.
 */
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports all contacts to CSV/VCF file\n"
            + "Parameters: FILENAME.[csv|vcf]\n"
            + "Example: export contacts_dump.csv\n"
            + "Note: Exporting to VCF will NOT export tags!";

    public static final String MESSAGE_EXPORT_SUCCESS = "Successfully exported all contacts to %1$s%2$s";
    public static final String MESSAGE_EXPORT_FAILURE = "Failed to export contacts to %1$s due to:\n%2$s";
    public static final String MESSAGE_INCORRECT_EXTENSION = "Filename must end with .csv or .vcf";

    private final String filename;

    public ExportCommand(String filename) {
        this.filename = filename;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (filename.toLowerCase().endsWith(".csv")) {
            return executeCsv(model);
        } else if (filename.toLowerCase().endsWith(".vcf")) {
            return executeVcf(model);
        }
        throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE,
                filename, MESSAGE_EXPORT_FAILURE));
    }

    private CommandResult executeCsv(Model model) throws CommandException {
        CsvAddressBookStorage csvStorage = new CsvAddressBookStorage(filename);
        try {
            csvStorage.saveAddressBook(model.getAddressBook());
            return new CommandResult(String.format(MESSAGE_EXPORT_SUCCESS, EXPORT_DIR_PREFIX, filename));
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE, filename, e.getMessage()));
        }
    }

    private CommandResult executeVcf(Model model) throws CommandException {
        throw new CommandException("");
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExportCommand)) {
            return false;
        }

        ExportCommand otherExportCommand = (ExportCommand) other;
        return filename.equals(otherExportCommand.filename);
    }
}

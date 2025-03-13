package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Exports all contacts to a CSV file, which can then be imported to AB3.
 * vcf file format support will be implemented at a future date.
 */
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports all contacts to CSV/VCF file "
            + "(vcf file support coming soon!)\n"
            + "Parameters: FILENAME.csv\n"
            + "Example: export contacts_dump.csv";

    public static final String MESSAGE_EXPORT_SUCCESS = "Exported all contacts to %1$s";
    public static final String MESSAGE_EXPORT_FAILURE = "Failed to export contacts to %1$s due to:\n%2$s";
    public static final String MESSAGE_EXPORT_WORK_IN_PROGRESS = "Export command is still a work-in-progress :D";

    private final String filename;

    public ExportCommand(String filename) {
        this.filename = filename;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(MESSAGE_EXPORT_WORK_IN_PROGRESS);
    }
}

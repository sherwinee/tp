package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.regex.Pattern;

import seedu.address.commons.util.FileUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.storage.CsvAddressBookStorage;
import seedu.address.storage.VcfAddressBookStorage;

/**
 * Exports all contacts to a CSV file, which can then be imported to AB3.
 * vcf file format support will be implemented at a future date.
 */
public class ExportCommand extends Command {
    public static final String COMMAND_WORD = "export";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Exports all contacts to a CSV or VCF file in "
            + getExportsDirAbsolutePath() + "\n"
            + "Parameters: FILENAME.[csv|vcf]\n"
            + "Example: export contacts_dump.csv\n"
            + "Note: Exporting to VCF will NOT export tags!";

    public static final String MESSAGE_EXPORT_SUCCESS_CSV = "Successfully exported all contacts to %1$s";
    public static final String MESSAGE_EXPORT_SUCCESS_VCF = MESSAGE_EXPORT_SUCCESS_CSV
            + "\nNote: Tags are NOT exported in the vcf file format.";
    public static final String MESSAGE_EXPORT_FAILURE = "Failed to export contacts to %1$s due to:\n%2$s";
    public static final String MESSAGE_NO_CONTACTS = "There are no contacts to export.";
    public static final String MESSAGE_INVALID_FILENAME =
            "Filename must be 1–255 characters long, not start with a dot, not be empty, \n"
            + "contain only letters, numbers, dots, hyphens, underscores, or spaces, \n"
            + "and end with .vcf or .csv (case-insensitive).\n\n"
            + "Directory traversing characters such as '/' are not allowed.\n"
            + "Exported files will be placed in the " + getExportsDirAbsolutePath() + " directory.";
    public static final String MESSAGE_FILE_EXISTS = "A file with the same name already exists.";
    public static final String EXPORT_DIR_PREFIX = "exports/";
    private static final String FILENAME_REGEX = "^(?!\\.)([a-zA-Z0-9._ -]{1,251})\\.(vcf|csv)$";

    private final String filename;

    public ExportCommand(String filename) {
        this.filename = filename;
    }

    /**
     * Returns the absolute filepath of the <code>exports</code> directory.
     */
    public static String getExportsDirAbsolutePath() {
        return new java.io.File(EXPORT_DIR_PREFIX).getAbsolutePath();
    }

    /**
     * Returns the absolute filepath of the export file with the given name
     * @param filename filename of the export dump
     */
    public static String getAbsoluteExportFilePath(String filename) {
        return new java.io.File((filename.startsWith(EXPORT_DIR_PREFIX) ? "" : EXPORT_DIR_PREFIX)
                + filename)
                .getAbsolutePath();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(filename);
        if (FileUtil.isFileExists(Path.of(EXPORT_DIR_PREFIX + filename))) {
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE,
                    filename, MESSAGE_FILE_EXISTS));
        }
        if (model.getAddressBook().equals(new AddressBook())) {
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE,
                    filename, MESSAGE_NO_CONTACTS));
        }
        if (!validateFilename(filename)) {
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE,
                    filename, MESSAGE_INVALID_FILENAME));
        }
        String relativeFilename = EXPORT_DIR_PREFIX + filename;
        if (filename.toLowerCase().endsWith(".csv")) {
            return executeCsv(model, relativeFilename);
        } else if (filename.toLowerCase().endsWith(".vcf")) {
            return executeVcf(model, relativeFilename);
        }
        throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE,
                filename, MESSAGE_EXPORT_FAILURE));
    }

    private CommandResult executeCsv(Model model, String filename) throws CommandException {
        CsvAddressBookStorage csvStorage = new CsvAddressBookStorage(filename);
        try {
            csvStorage.saveAddressBook(model.getAddressBook());
            return new CommandResult(String.format(MESSAGE_EXPORT_SUCCESS_CSV,
                    getAbsoluteExportFilePath(filename)));
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE,
                    getAbsoluteExportFilePath(filename),
                    e.getMessage()));
        }
    }

    private CommandResult executeVcf(Model model, String filename) throws CommandException {
        VcfAddressBookStorage vcfStorage = new VcfAddressBookStorage(filename);
        try {
            vcfStorage.saveAddressBook(model.getAddressBook());
            return new CommandResult(String.format(MESSAGE_EXPORT_SUCCESS_VCF,
                    getAbsoluteExportFilePath(filename)));
        } catch (IOException e) {
            throw new CommandException(String.format(MESSAGE_EXPORT_FAILURE,
                    getAbsoluteExportFilePath(filename),
                    e.getMessage()));
        }
    }

    private boolean validateFilename(String filename) {
        requireNonNull(filename);
        assert (!FILENAME_REGEX.isEmpty());
        Pattern pattern = Pattern.compile(FILENAME_REGEX, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(filename).matches();
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

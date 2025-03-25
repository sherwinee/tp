package seedu.address.logic.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CsvParser;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.tag.Tag;

/**
 * Imports a file ending with CSV format
 */
public class ImportCommand extends Command {
    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports all data from the specified file path "
            + "(case-sensitive) and displays them as list with index numbers.\n"
            + "Parameters: FILENAME (must end with .csv)\n"
            + "Example: " + COMMAND_WORD + " addressbook.csv";

    public static final String MESSAGE_CONSTRAINTS = "File name must end with .csv";
    public static final String MESSAGE_READ_INPUT_ERROR = "Error reading CSV file due to:";
    public static final String MESSAGE_ERROR_DURING_IMPORT = "Some errors occurred during import\n";
    public static final String MESSAGE_EMPTY_FILE = "No persons were imported. Check your CSV file.";
    public static final String MESSAGE_SUCCESS = "Successfully imported %d contacts!";

    private static final Logger logger = Logger.getLogger(ImportCommand.class.getName());

    private final Path filePath;

    /**
     * @param filePath of the filename to be imported
     */
    public ImportCommand(Path filePath) {
        this.filePath = filePath;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        logger.info("Executing ImportCommand with file: " + filePath);
        List<Person> importedPersons;
        List<String> errors = new ArrayList<>();
        List<String> duplicatePersonErrors = new ArrayList<>();

        try {
            importedPersons = importCsv(filePath.toString(), errors);
            logger.info("Parsed " + importedPersons.size() + " valid persons from CSV.");
        } catch (IOException e) {
            logger.severe("Error reading file: " + e.getMessage());
            throw new CommandException(MESSAGE_READ_INPUT_ERROR + e.getMessage());
        }

        if (!errors.isEmpty()) {
            logger.warning("Import errors: " + String.join("; ", errors));
            throw new CommandException(MESSAGE_ERROR_DURING_IMPORT
                    + errors.stream().collect(Collectors.joining("\n")));
        }

        if (importedPersons.isEmpty()) {
            logger.warning("No persons were imported — file may be empty or invalid.");
            throw new CommandException(MESSAGE_EMPTY_FILE);
        }

        // Add imported persons to the model
        for (int i = 0; i < importedPersons.size(); i++) {
            Person person = importedPersons.get(i);
            int rowNumber = i + 2; // Adjusting for 1-based index and skipping header row

            try {
                model.addPerson(person);
            } catch (DuplicatePersonException e) {
                duplicatePersonErrors.add("Row " + rowNumber + ": " + e.getMessage());
            }
        }

        if (!duplicatePersonErrors.isEmpty()) {
            logger.warning("Duplicate entries found during import.");
            throw new CommandException("Duplicate persons found:\n"
                    + duplicatePersonErrors.stream().collect(Collectors.joining("\n")));
        }
        logger.info("Import successful! Imported " + importedPersons.size() + " persons.");
        return new CommandResult(String.format(MESSAGE_SUCCESS, importedPersons.size()));
    }

    /**
     * Reads a CSV file from the specified file path and converts its contents into a list of {@code Person} objects.
     *
     * <p>Each row in the CSV file must have at least four columns:</p>
     * <ul>
     *     <li><b>Name</b> - The person's name (String)</li>
     *     <li><b>Phone</b> - The person's phone number (String)</li>
     *     <li><b>Email</b> - The person's email address (String)</li>
     *     <li><b>Address</b> - The person's address (String)</li>
     *     <li><b>Tags</b> (Optional) - A list of tags separated by commas or semicolons</li>
     * </ul>
     *
     * <p>Any row missing required fields will be skipped.</p>
     *
     * @param filePath The path to the CSV file to be imported.
     * @param errors A list to collect error messages for invalid rows.
     * @return A list of {@code Person} objects parsed from the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<Person> importCsv(String filePath, List<String> errors) throws IOException {
        List<Person> persons = new ArrayList<>();
        List<List<String>> rawData = CsvParser.parseCsv(filePath);

        for (int i = 0; i < rawData.size(); i++) {
            List<String> values = rawData.get(i);

            if (values.size() < 4) {
                errors.add("Row " + (i + 2) + ": Missing required fields in format of (Name, Phone, Email, Address).");
                continue;
            }

            try {
                Name name = new Name(values.get(0).trim());
                Phone phone = new Phone(values.get(1).trim());
                Email email = new Email(values.get(2).trim());
                Address address = new Address(values.get(3).trim());
                Role role = new Role(values.get(4).trim());
                Set<Tag> tags = new HashSet<>();

                if (values.size() > 4) {
                    String[] tagArray = values.get(4).split("[;,]");
                    for (String tag : tagArray) {
                        tag = tag.trim();
                        if (!tag.isEmpty()) {
                            tags.add(new Tag(tag));
                        }
                    }
                }

                persons.add(new Person(name, phone, email, address, role, tags));

            } catch (IllegalArgumentException e) {
                errors.add("Row " + (i + 2) + ": " + e.getMessage() + ".");
            }
        }


        return persons;
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

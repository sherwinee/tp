package seedu.address.logic.commands;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CsvParser;
import seedu.address.logic.parser.VcfParser;
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
 * Imports contact data from a file into the address book.
 * <p>
 * Supports both CSV and VCF file formats:
 * <ul>
 *     <li>CSV: Name, Phone, Email, Address, Role, Tags (optional)</li>
 *     <li>VCF: vCard format with structured contact fields</li>
 * </ul>
 * Skips duplicate entries and shows error messages for invalid rows.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports all data from the specified file path "
            + "(case-sensitive) and displays them as list with index numbers.\n"
            + "Parameters: FILENAME (must end with .csv or .vcf)\n"
            + "Example: " + COMMAND_WORD + " addressbook.csv";

    public static final String MESSAGE_CONSTRAINTS = "File name must end with .csv or .vcf";
    public static final String MESSAGE_READ_INPUT_ERROR = "Error reading file due to: ";
    public static final String MESSAGE_ERROR_DURING_IMPORT = "Some errors occurred during import:\n";
    public static final String MESSAGE_EMPTY_FILE = "No persons were imported. Check your file.";
    public static final String MESSAGE_SUCCESS = "Successfully imported %d contacts!";

    private static final Logger logger = Logger.getLogger(ImportCommand.class.getName());

    private final Path filePath;

    public ImportCommand(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        logger.info("Executing ImportCommand with file: " + filePath);

        List<String> errors = new ArrayList<>();
        List<Person> importedPersons = parseFile(filePath, errors);

        if (!errors.isEmpty()) {
            throw new CommandException(MESSAGE_ERROR_DURING_IMPORT + String.join("\n", errors));
        }

        if (importedPersons.isEmpty()) {
            throw new CommandException(MESSAGE_EMPTY_FILE);
        }

        int startingRowNumber = filePath.toString().toLowerCase().endsWith(".csv") ? 2 : 1;
        List<String> duplicateErrors = addPersonsToModel(model, importedPersons, startingRowNumber);

        if (!duplicateErrors.isEmpty()) {
            throw new CommandException(formatDuplicateErrors(duplicateErrors));
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, importedPersons.size()));
    }

    private List<Person> parseFile(Path filePath, List<String> errors) throws CommandException {
        try {
            if (filePath.toString().toLowerCase().endsWith(".csv")) {
                return importCsv(filePath.toString(), errors);
            } else if (filePath.toString().toLowerCase().endsWith(".vcf")) {
                return VcfParser.parseVcf(filePath.toString());
            } else {
                throw new CommandException("Unsupported file type. Only .csv and .vcf files are supported.");
            }
        } catch (IOException e) {
            throw new CommandException(MESSAGE_READ_INPUT_ERROR + e.getMessage());
        }
    }

    private List<String> addPersonsToModel(Model model, List<Person> persons, int startingRowNumber) {
        List<String> duplicateErrors = new ArrayList<>();

        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            int rowNumber = i + startingRowNumber;

            try {
                model.addPerson(person);
            } catch (DuplicatePersonException e) {
                duplicateErrors.add("Row " + rowNumber + ": " + e.getMessage());
            }
        }

        return duplicateErrors;
    }

    private String formatDuplicateErrors(List<String> duplicateErrors) {
        return "Duplicate persons found:\n" + String.join("\n", duplicateErrors);
    }

    /**
     * Parses a CSV file and converts each valid row into a {@code Person} object.
     * <p>
     * Each row must contain at least 5 values: Name, Phone, Email, Address, and Role.
     * The 6th column (optional) can include tags separated by commas or semicolons.
     * <p>
     * Invalid rows trigger error messages and abort the import
     *
     * @param filePath The path to the CSV file.
     * @param errors A list to collect error messages for invalid rows.
     * @return A list of successfully parsed {@code Person} objects.
     * @throws IOException If reading the file fails.
     */
    public static List<Person> importCsv(String filePath, List<String> errors) throws IOException {
        List<Person> persons = new ArrayList<>();
        List<List<String>> rawData = CsvParser.parseCsv(filePath);

        for (int i = 0; i < rawData.size(); i++) {
            List<String> values = rawData.get(i);
            Person person = createPerson(values, i + 2, errors); // +2 for CSV line number
            if (person != null) {
                persons.add(person);
            }
        }

        return persons;
    }

    private static Person createPerson(List<String> values, int rowNumber, List<String> errors) {
        if (values.size() < 5) {
            errors.add("Row " + rowNumber + ": Missing required fields (Name, Phone, Email, Address, Role).");
            return null;
        }

        try {
            Name name = new Name(values.get(0).trim());
            Phone phone = new Phone(values.get(1).trim());
            Email email = new Email(values.get(2).trim());
            Address address = new Address(values.get(3).trim());
            Role role = new Role(values.get(4).trim());

            Set<Tag> tags = (values.size() > 5) ? parseTags(values.get(5)) : new HashSet<>();

            return new Person(name, phone, email, address, role, tags, Optional.empty());
        } catch (IllegalArgumentException e) {
            errors.add("Row " + rowNumber + ": " + e.getMessage());
            return null;
        }
    }

    private static Set<Tag> parseTags(String rawTags) {
        Set<Tag> tags = new HashSet<>();
        String[] tagArray = rawTags.split("[;,]");
        for (String tag : tagArray) {
            tag = tag.trim();
            if (!tag.isEmpty()) {
                tags.add(new Tag(tag));
            }
        }
        return tags;
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

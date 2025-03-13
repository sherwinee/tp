package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.CSVParser;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Imports a file ending with CSV format
 */
public class ImportCommand extends Command{

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports all data from the specified filepath "
            + "(case-sensitive) and displays them as list with index numbers.\n"
            + "Parameters: import [filename.csv]\n"
            + "Example: " + COMMAND_WORD + " addressbook.csv";

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
        List<Person> importedPersons;
        try {
            importedPersons = importCSV(filePath.toString());
        } catch (IOException e) {
            throw new CommandException("Error reading CSV file: " + e.getMessage());
        }

        if (importedPersons.isEmpty()) {
            throw new CommandException("No persons were imported. Check your CSV file.");
        }

        // Add imported persons to the model
        for (Person person : importedPersons) {
            model.addPerson(person);
        }
        return new CommandResult("Successfully imported " + importedPersons.size()
                + " contacts!");
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


    public static List<Person> importCSV(String filePath) throws IOException {
        List<Person> persons = new ArrayList<>();
        List<List<String>> rawData = CSVParser.parseCSV(filePath);

        for (List<String> values : rawData) {
            if (values.size() >= 4) {
                Name name = new Name(values.get(0).trim());
                Phone phone = new Phone(values.get(1).trim());
                Email email = new Email(values.get(2).trim());
                Address address = new Address(values.get(3).trim());
                Set<Tag> tags = new HashSet<>();

                if (values.size() > 4) {
                    String[] tagArray = values.get(4).split("[;,]");
                    for (String tag : tagArray) {
                        tag = tag.trim(); // Keep alphanumeric
                        if (!tag.isEmpty()) {
                            tags.add(new Tag(tag));
                        }
                    }
                }

                persons.add(new Person(name, phone, email, address, tags));
            }
        }
        return persons;
    }
}

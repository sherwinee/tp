package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.VcfParser;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains tests for {@code ImportCommand}.
 */
public class ImportCommandTest {

    private Path tempCsvFile;
    private Path testCsvFile = Path.of("import_test_files/Typical_Persons_CSV.csv");
    private Path testVcfFile = Path.of("import_test_files/contacts.vcf");
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @BeforeEach
    public void setUp() throws IOException {
        // Create a temporary CSV file for testing
        tempCsvFile = Files.createTempFile("test_import", ".csv");

    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempCsvFile);
    }

    @Test
    public void execute_validCsv_importsSuccessfully() {
        ImportCommand command = new ImportCommand(testCsvFile);
        assertCommandSuccess(command, model, String.format(ImportCommand.MESSAGE_SUCCESS, 7), expectedModel);
    }

    @Test
    public void execute_validVcf_importsSuccessfully() throws Exception {
        ImportCommand command = new ImportCommand(testVcfFile);

        // Start with an empty expected model
        Model expectedModel = new ModelManager();

        // Use the same parser to get the expected persons from the VCF
        List<Person> expectedPersons = VcfParser.parseVcf(testVcfFile.toString());
        for (Person person : expectedPersons) {
            expectedModel.addPerson(person);
        }

        // Assert command success with expected message and model
        assertCommandSuccess(command, model,
                String.format(ImportCommand.MESSAGE_SUCCESS, expectedPersons.size()), expectedModel);
    }


    @Test
    public void execute_missingFields_importCsvFailure() throws IOException {
        String csvData = "Name, Phone, Email, Address, Role, Tags\n"
                + "Alice Tan,91234567,,\"123, Jurong West Ave 6, #08-111\",,\n" // Missing email and role
                + "Bob Lim,,bob@example.com,456 Avenue,Banker\n" // Missing phone
                + "Carl Kurz,812348,carl@yahoo.com, ,Project Manager, \"friend, ,colleague\"\n"; // Missing address

        Files.write(tempCsvFile, csvData.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        ImportCommand command = new ImportCommand(tempCsvFile);
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void importCsv_variousTagCases() throws IOException {
        String csvData = "Name, Phone, Email, Address, Role, Tags\n"
                + "Alice Tan,91234567,alice@example.com,123 Street,Software Engineer,\n"
                + "Bob Lim,98765432,bob@example.com,456 Avenue,Banker\n"
                + "Carl Kurz,812348,carl@yahoo.com,Wall Street,Project Manager,\"friend, ,colleague\"\n";

        Files.write(tempCsvFile, csvData.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);

        List<String> errors = new ArrayList<>();
        List<Person> persons = ImportCommand.importCsv(tempCsvFile.toString(), errors);

        assertTrue(persons.size() == 3, "Expected 3 persons, got " + persons.size());
        assertTrue(persons.get(0).getTags().size() == 0, "Expected no tags for first person");
        assertTrue(persons.get(1).getTags().size() == 0, "Expected no tags for second person");
        assertTrue(persons.get(2).getTags().size() == 2, "Expected 2 tags for third person");
    }


    @Test
    public void importCsv_missingRequiredFields_importFailure() throws IOException {
        String csvData = "Name,Phone,\n"
                + "Alice Tan,91234567"
                + "Bob Lim, 81234567";

        Files.write(tempCsvFile, csvData.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        ImportCommand command = new ImportCommand(tempCsvFile);
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_duplicatePersons_importCsvPartialSuccess() throws IOException {
        // Partial success as some contacts will be imported successfully
        String csvData = "Name,Phone,Email,Address,Role,Tags\n"
                + "Alice Pauline,94351253,alice@example.com,\"123, Jurong West Ave 6, #08-111\",Organizer,friends\n"
                + "Benson Meier,98765432,johnd@example.com,\"311, Clementi Ave 2, #02-25\","
                + "Developer,owesMoney;friends\n"
                + "Carl Kurz,95352563,heinz@example.com,wall street,Booth Vendor,\n"
                + "Daniel Meier,87652533,cornelia@example.com,10th street,Senior Developer,friends\n"
                + "Elle Meyer,9482224,werner@example.com,michegan ave,Software Engineer,\n"
                + "Fiona Kunz,9482427,lydia@example.com,little tokyo,Project Manager,\n"
                + "George Best,9482442,anna@example.com,4th street,Manager,\n"
                + "Alice Pauline,94351253,alice@example.com,\"123, Jurong West Ave 6, #08-111\",Organizer,friends\n";

        Files.write(tempCsvFile, csvData.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        ImportCommand command = new ImportCommand(tempCsvFile);
        String expectedMessage = String.format(ImportCommand.MESSAGE_SUCCESS, 7)
                + "\nHowever, some duplicate entries were found and skipped:\n"
                + "Row 9: Operation would result in duplicate persons\n";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_emptyCsv_failure() throws IOException {
        // Write only the header (no data)
        Files.write(tempCsvFile, "Name, Phone, Email, Address, Role, Tags\n".getBytes(),
                StandardOpenOption.TRUNCATE_EXISTING);

        ImportCommand command = new ImportCommand(tempCsvFile);
        assertCommandFailure(command, model, ImportCommand.MESSAGE_EMPTY_FILE);
    }

    @Test
    public void execute_nonExistentFile_throwsCommandException() {
        Path invalidPath = Path.of("non_existent_file.csv");

        ImportCommand command = new ImportCommand(invalidPath);
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_unsupportedFileType_throwsCommandException() {
        Path unsupportedFile = Path.of("import_test_files/invalid_file.txt"); // any non-csv/vcf file
        ImportCommand command = new ImportCommand(unsupportedFile);

        assertThrows(CommandException.class, () -> command.execute(model));
    }



    @Test
    public void equals() {
        Path filePath1 = Path.of("data1.csv");
        Path filePath2 = Path.of("data2.csv");

        ImportCommand importFirstCommand = new ImportCommand(filePath1);
        ImportCommand importSecondCommand = new ImportCommand(filePath2);

        // same object -> returns true
        assertTrue(importFirstCommand.equals(importFirstCommand));

        // same values -> returns true
        ImportCommand importFirstCommandCopy = new ImportCommand(filePath1);
        assertTrue(importFirstCommand.equals(importFirstCommandCopy));

        // different file path -> returns false
        assertTrue(!importFirstCommand.equals(importSecondCommand));

        // different types -> returns false
        assertTrue(!importFirstCommand.equals(1));

        // null -> returns false
        assertFalse(importFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(importFirstCommand.equals(importSecondCommand));
    }
}

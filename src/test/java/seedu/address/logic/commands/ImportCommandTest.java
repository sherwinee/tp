package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

/**
 * Contains tests for {@code ImportCommand}.
 */
public class ImportCommandTest {

    private Path tempFile;
    private Path testFile = Path.of("import_test_files/Typical_Persons_CSV.csv");
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @BeforeEach
    public void setUp() throws IOException {
        // Create a temporary CSV file for testing
        tempFile = Files.createTempFile("test_import", ".csv");

    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void execute_validCsv_importsSuccessfully() {
        ImportCommand command = new ImportCommand(testFile);
        assertCommandSuccess(command, model, String.format(ImportCommand.MESSAGE_SUCCESS, 7), expectedModel);
    }

    @Test
    public void execute_missingFields_importFailure() throws IOException {
        String csvData = "Name,Phone,Email,Address,Tags\n"
                + "Alice Tan,91234567,,\"123, Jurong West Ave 6, #08-111\",\n" // Missing email
                + "Bob Lim,,bob@example.com,456 Avenue\n" // Missing phone
                + "Carl Kurz,812348,carl@yahoo.com,,\"friend, ,colleague\"\n"; // Missing address

        Files.write(tempFile, csvData.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        ImportCommand command = new ImportCommand(tempFile);
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void importCsv_variousTagCases() throws IOException {
        // CSV data with:
        // - No tags (Row 2)
        // - Empty tags field (Row 3)
        // - Tags field with an empty entry (Row 4)
        String csvData = "Name,Phone,Email,Address,Tags\n"
                + "Alice Tan,91234567,alice@example.com,123 Street\n"  // No tags
                + "Bob Lim,98765432,bob@example.com,456 Avenue,\n" // Empty tags field
                + "Carl Kurz,812348,carl@yahoo.com,Wall Street,\"friend, ,colleague\"\n";  // Tags field with an empty entry

        Files.write(tempFile, csvData.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);

        List<String> errors = new ArrayList<>();
        List<Person> persons = ImportCommand.importCsv(tempFile.toString(), errors);

        // Ensure persons are added
        assertEquals(3, persons.size());

        // Ensure tags are assigned correctly
        assertEquals(0, persons.get(0).getTags().size()); // No tags
        assertEquals(0, persons.get(1).getTags().size()); // Empty tags
        assertEquals(2, persons.get(2).getTags().size()); // "friend" and "colleague" (empty space skipped)
    }


    @Test
    public void importCsv_missingRequiredFields_importFailure() throws IOException {
        String csvData = "Name,Phone,\n"
                + "Alice Tan,91234567"
                + "Bob Lim, 81234567";

        Files.write(tempFile, csvData.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        ImportCommand command = new ImportCommand(tempFile);
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_duplicatePersons_importFailure() throws IOException {
        String csvData = "Name,Phone,Email,Address,Tags\n"
                + "Alice Tan,91234567, alice@example.com, \"123, Jurong West Ave 6, #08-111\",friend\n"
                + "Alice Tan,91234567, alice@example.com, \"123, Jurong West Ave 6, #08-111\",friend";


        Files.write(tempFile, csvData.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
        ImportCommand command = new ImportCommand(tempFile);
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_emptyCsv_failure() throws IOException {
        // Write only the header (no data)
        Files.write(tempFile, "Name,Phone,Email,Address,Tags\n".getBytes(), StandardOpenOption.TRUNCATE_EXISTING);

        ImportCommand command = new ImportCommand(tempFile);
        assertCommandFailure(command, model, ImportCommand.MESSAGE_EMPTY_FILE);
    }

    @Test
    public void execute_nonExistentFile_throwsCommandException() {
        Path invalidPath = Path.of("non_existent_file.csv");

        ImportCommand command = new ImportCommand(invalidPath);
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

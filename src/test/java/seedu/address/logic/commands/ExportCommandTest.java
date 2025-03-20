package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.commons.util.FileUtil.readFromFile;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ExportCommand.MESSAGE_EXPORT_SUCCESS;
import static seedu.address.storage.CsvAddressBookStorage.EXPORT_DIR_PREFIX;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;


/**
 * Contains integration tests (interaction with the Model) for {@code ExportCommand}.
 */
public class ExportCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_emptyAddressBook_emptyFile() {
        String filename = "emptyfile.csv";
        ExportCommand ec = new ExportCommand(filename);
        String expectedMsg = String.format(MESSAGE_EXPORT_SUCCESS, EXPORT_DIR_PREFIX, filename);
        assertCommandSuccess(ec, model, expectedMsg, expectedModel);
        try {
            assertEquals("name,phone,email,address,tags\n", readFromFile(Path.of(EXPORT_DIR_PREFIX + filename)));
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void execute_typicalAddressBook_populatedFile() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String filename = "typical.csv";
        ExportCommand ec = new ExportCommand(filename);
        String expectedMsg = String.format(MESSAGE_EXPORT_SUCCESS, EXPORT_DIR_PREFIX, filename);
        assertCommandSuccess(ec, model, expectedMsg, expectedModel);
    }

    @Test
    public void equals() {
        ExportCommand exportFirstCommand = new ExportCommand("testEquals1.csv");
        ExportCommand exportSecondCommand = new ExportCommand("testEquals2.csv");

        // same object -> returns true
        assertTrue(exportSecondCommand.equals(exportSecondCommand));

        // same values -> returns true
        ExportCommand exportFirstCommandCopy = new ExportCommand("testEquals1.csv");
        assertTrue(exportFirstCommand.equals(exportFirstCommandCopy));

        // different types -> returns false
        assertFalse(exportSecondCommand.equals(1));

        // null -> returns false
        assertFalse(exportSecondCommand.equals(null));

        // different filename -> returns false
        assertFalse(exportFirstCommand.equals(exportSecondCommand));
    }
}

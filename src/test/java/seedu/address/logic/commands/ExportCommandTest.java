package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ExportCommand.MESSAGE_EXPORT_FAILURE;
import static seedu.address.logic.commands.ExportCommand.MESSAGE_EXPORT_SUCCESS_CSV;
import static seedu.address.logic.commands.ExportCommand.MESSAGE_EXPORT_SUCCESS_VCF;
import static seedu.address.logic.commands.ExportCommand.MESSAGE_FILE_EXISTS;
import static seedu.address.logic.commands.ExportCommand.MESSAGE_INVALID_FILENAME;
import static seedu.address.logic.commands.ExportCommand.MESSAGE_NO_CONTACTS;
import static seedu.address.logic.commands.ExportCommand.getAbsoluteExportFilePath;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.File;

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
        String expectedMsg = String.format(MESSAGE_EXPORT_FAILURE, filename, MESSAGE_NO_CONTACTS);
        assertCommandFailure(ec, model, expectedMsg);
    }

    @Test
    public void execute_typicalAddressBookVcf_success() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String filename = "typicalTest.vcf";
        ExportCommand ec = new ExportCommand(filename);
        String expectedMsg = String.format(MESSAGE_EXPORT_SUCCESS_VCF, getAbsoluteExportFilePath(filename));
        assertCommandSuccess(ec, model, expectedMsg, expectedModel);

        // Test for same filename failure
        ec = new ExportCommand(filename);
        expectedMsg = String.format(MESSAGE_EXPORT_FAILURE, filename, MESSAGE_FILE_EXISTS);
        assertCommandFailure(ec, model, expectedMsg);
        new File(getAbsoluteExportFilePath(filename)).delete();
    }

    @Test
    public void execute_typicalAddressBookCsv_success() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String filename = "typicalTest.csv";
        ExportCommand ec = new ExportCommand(filename);
        String expectedMsg = String.format(MESSAGE_EXPORT_SUCCESS_CSV, getAbsoluteExportFilePath(filename));
        assertCommandSuccess(ec, model, expectedMsg, expectedModel);

        // Test for same filename failure
        ec = new ExportCommand(filename);
        expectedMsg = String.format(MESSAGE_EXPORT_FAILURE, filename, MESSAGE_FILE_EXISTS);
        assertCommandFailure(ec, model, expectedMsg);
        new File(getAbsoluteExportFilePath(filename)).delete();
    }

    @Test
    public void execute_typicalAddressBookInvalidFormat_failure() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String filename = "typicalTest.sasdad";
        ExportCommand ec = new ExportCommand(filename);
        String expectedMsg = String.format(MESSAGE_EXPORT_FAILURE, filename, MESSAGE_INVALID_FILENAME);
        assertCommandFailure(ec, model, expectedMsg);
        new File(getAbsoluteExportFilePath(filename)).delete();
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

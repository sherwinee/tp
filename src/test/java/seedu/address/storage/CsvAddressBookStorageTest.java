package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.ExportCommand.EXPORT_DIR_PREFIX;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;

public class CsvAddressBookStorageTest {
    // Note: Our CSV storage always writes to "./exports/<filename>"
    // so we use a relative file name here.
    private static final String RELATIVE_FILE_NAME = "TempAddressBook.csv";

    @TempDir
    public Path tempFolder;

    private Optional<ReadOnlyAddressBook> readAddressBook(String fileName) throws Exception {
        // Since the read methods are not implemented, any call will throw a DataLoadingException.
        return new CsvAddressBookStorage(fileName).readAddressBook(Paths.get(fileName));
    }

    @Test
    public void read_missingFile_throwsDataLoadingException() throws Exception {
        // When trying to read a file that does not exist (or when read logic isn't implemented),
        // we expect a DataLoadingException.
        assertThrows(DataLoadingException.class, () -> readAddressBook("NonExistentFile.csv"));
    }

    @Test
    public void read_notCsvFormat_exceptionThrown() {
        // If the file exists but is not in CSV format, we also expect a DataLoadingException.
        assertThrows(DataLoadingException.class, () -> readAddressBook("notCsvFormatAddressBook.csv"));
    }

    @Test
    public void readAddressBook_invalidPersonAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidPersonAddressBook.csv"));
    }

    @Test
    public void readAddressBook_invalidAndValidPersonAddressBook_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readAddressBook("invalidAndValidPersonAddressBook.csv"));
    }

    @Test
    public void saveAddressBook_createsFile_success() throws Exception {
        // Instead of using an absolute path, we pass a relative file name.
        // The CsvAddressBookStorage constructor will construct the full path as:
        // Path.of("./exports/" + RELATIVE_FILE_NAME)
        AddressBook original = getTypicalAddressBook();
        CsvAddressBookStorage csvStorage = new CsvAddressBookStorage(RELATIVE_FILE_NAME);

        // Save the address book.
        // We use the same relative path (as a Path) for saving.
        Path filePath = Paths.get("./exports/" + RELATIVE_FILE_NAME);
        csvStorage.saveAddressBook(original, filePath);

        // Verify that the file now exists and is not empty.
        assertTrue(Files.exists(filePath), "The CSV export file should exist.");
        String content = Files.readString(filePath);
        assertFalse(content.isEmpty(), "The CSV export file should not be empty.");

        // Since import (read) is not implemented, reading should throw DataLoadingException.
        assertThrows(DataLoadingException.class, () -> csvStorage.readAddressBook(filePath));
    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(null, RELATIVE_FILE_NAME));
    }

    /**
     * Helper method to save an address book at the specified relative file name.
     */
    private void saveAddressBook(ReadOnlyAddressBook addressBook, String fileName) {
        try {
            new CsvAddressBookStorage(fileName)
                    .saveAddressBook(addressBook, Paths.get("./exports/" + fileName));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void getAddressBookFilePath_returnsCorrectPath() {
        String fileName = "TestFile.csv";
        CsvAddressBookStorage csvStorage = new CsvAddressBookStorage(
                EXPORT_DIR_PREFIX + fileName);
        Path expected = Path.of(ExportCommand.getAbsoluteExportFilePath(fileName));
        assertEquals(expected, csvStorage.getAddressBookFilePath());
    }

    @Test
    public void readAddressBook_noArg_throwsDataLoadingException() {
        String fileName = "AnyFile.csv"; // File name doesn't matter since read is not implemented
        CsvAddressBookStorage csvStorage = new CsvAddressBookStorage(fileName);
        // The no-argument readAddressBook() method always throws a DataLoadingException.
        assertThrows(DataLoadingException.class, csvStorage::readAddressBook);
    }

    @Test
    public void saveAddressBook_noArg_savesFileCorrectly() throws Exception {
        // Use a relative file name so that the constructed path is valid.
        String fileName = "TempAddressBook_noArg.csv";
        CsvAddressBookStorage csvStorage = new CsvAddressBookStorage(EXPORT_DIR_PREFIX + fileName);
        Path filePath = csvStorage.getAddressBookFilePath();

        // Get a typical address book from your test utilities.
        AddressBook original = seedu.address.testutil.TypicalPersons.getTypicalAddressBook();

        // Call the no-argument version, which internally calls saveAddressBook(addressBook, filePath)
        csvStorage.saveAddressBook(original);

        // Verify that the file exists and is not empty.
        assertTrue(Files.exists(filePath), "The CSV export file should exist.");
        String content = Files.readString(filePath);
        assertFalse(content.isEmpty(), "The CSV export file should not be empty.");
    }

}

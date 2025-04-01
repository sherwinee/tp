package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;

public class VcfAddressBookStorageTest {
    private static final String RELATIVE_FILE_NAME = "TempAddressBook.vcf";

    @TempDir
    public Path tempFolder;

    private Optional<ReadOnlyAddressBook> readAddressBook(String fileName) throws Exception {
        return new VcfAddressBookStorage(fileName).readAddressBook(Paths.get(fileName));
    }

    @Test
    public void readAddressBook_noArg_throwsDataLoadingException() {
        VcfAddressBookStorage vcfStorage = new VcfAddressBookStorage("AnyFile.vcf");
        assertThrows(DataLoadingException.class, vcfStorage::readAddressBook);
    }

    @Test
    public void readAddressBook_withPath_throwsDataLoadingException() {
        assertThrows(DataLoadingException.class, () ->
                readAddressBook("NonExistentFile.vcf"));
    }

    @Test
    public void saveAddressBook_createsVcfFile_success() throws Exception {
        AddressBook original = getTypicalAddressBook();
        VcfAddressBookStorage vcfStorage = new VcfAddressBookStorage(RELATIVE_FILE_NAME);
        Path filePath = Paths.get("./exports/" + RELATIVE_FILE_NAME);

        vcfStorage.saveAddressBook(original, filePath);

        assertTrue(Files.exists(filePath), "The VCF export file should exist.");
        String content = Files.readString(filePath);
        assertFalse(content.isEmpty(), "The VCF export file should not be empty.");

        // Ensure import still throws
        assertThrows(DataLoadingException.class, () -> vcfStorage.readAddressBook(filePath));
    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(null, RELATIVE_FILE_NAME));
    }

    private void saveAddressBook(ReadOnlyAddressBook addressBook, String fileName) {
        try {
            new VcfAddressBookStorage(fileName)
                    .saveAddressBook(addressBook, Paths.get("./exports/" + fileName));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void getAddressBookFilePath_returnsCorrectPath() {
        String fileName = "TestFile.vcf";
        VcfAddressBookStorage vcfStorage = new VcfAddressBookStorage(fileName);
        Path expected = Path.of("./exports/" + fileName);
        assertEquals(expected, vcfStorage.getAddressBookFilePath());
    }

    @Test
    public void saveAddressBook_noArg_savesFileCorrectly() throws Exception {
        String fileName = "TempAddressBook_noArg.vcf";
        VcfAddressBookStorage vcfStorage = new VcfAddressBookStorage(fileName);
        Path filePath = vcfStorage.getAddressBookFilePath();

        AddressBook original = getTypicalAddressBook();
        vcfStorage.saveAddressBook(original);

        assertTrue(Files.exists(filePath), "The VCF export file should exist.");
        String content = Files.readString(filePath);
        assertFalse(content.isEmpty(), "The VCF export file should not be empty.");
    }
}

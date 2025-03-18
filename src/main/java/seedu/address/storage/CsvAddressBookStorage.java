package seedu.address.storage;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyAddressBook;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.databind.MappingIterator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * A class to access AddressBook data stored as a CSV file on the hard disk.
 */
public class CsvAddressBookStorage implements AddressBookStorage {

    private static final Logger logger = LogsCenter.getLogger(CsvAddressBookStorage.class);
    private static final String EXPORT_DIR_PREFIX = "./exports/";

    private final Path filePath;
    private final CsvMapper csvMapper;

    public CsvAddressBookStorage(String filePath) {
        this.filePath = Path.of(EXPORT_DIR_PREFIX + filePath);
        this.csvMapper = new CsvMapper(); // Jackson CSV mapper
    }

    @Override
    public Path getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        throw new DataLoadingException(new Exception("Import logic not implemented using Jackson yet."));
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
        throw new DataLoadingException(new Exception("Import logic not implemented using Jackson yet."));
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        // Ensure file/directory exists
        FileUtil.createIfMissing(filePath);

        // Convert AddressBook -> CsvSerializableAddressBook -> List<CsvAdaptedPerson>
        CsvSerializableAddressBook csvAddressBook = new CsvSerializableAddressBook(addressBook);
        List<CsvAdaptedPerson> persons = csvAddressBook.getPersons();

        // Write them out using Jackson CSV
        CsvSchema schema = csvMapper.schemaFor(CsvAdaptedPerson.class).withHeader();
        csvMapper.writer(schema).writeValue(filePath.toFile(), persons);
    }
}

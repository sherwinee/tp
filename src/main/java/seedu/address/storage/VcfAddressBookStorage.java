package seedu.address.storage;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.ReadOnlyAddressBook;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import ezvcard.Ezvcard;
import ezvcard.VCard;

import static java.util.Objects.requireNonNull;

public class VcfAddressBookStorage implements AddressBookStorage {
    public static final String EXPORT_DIR_PREFIX = "./exports/";

    private static final Logger logger = LogsCenter.getLogger(VcfAddressBookStorage.class);

    private final Path filePath;

    public VcfAddressBookStorage(String filePath) {
        this.filePath = Path.of(EXPORT_DIR_PREFIX + filePath);
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

        // Convert AddressBook -> VcfSerializableAddressBook -> List<VcfAdaptedPerson>
        VcfSerializableAddressBook csvAddressBook = new VcfSerializableAddressBook(addressBook);
        List<VcfAdaptedPerson> persons = csvAddressBook.getPersons();

        // Write to VCF file
        List<VCard> vcards = VcfMapper.mapToVcards(persons);
        Ezvcard.write(vcards).go(filePath.toFile());
    }
}

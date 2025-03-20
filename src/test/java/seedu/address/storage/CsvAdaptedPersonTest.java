package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.stream.Collectors;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import org.junit.jupiter.api.Test;

public class CsvAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    // For CSV, tags are stored as a flattened string (e.g., "owesMoney;friends")
    private static final String VALID_TAGS = BENSON.getTags().stream()
            .map(tag -> tag.tagName)
            .collect(Collectors.joining(";"));

    @Test
    public void constructor_fromPerson_returnsCorrectValues() {
        CsvAdaptedPerson csvPerson = new CsvAdaptedPerson(BENSON);
        assertEquals(VALID_NAME, csvPerson.getName());
        assertEquals(VALID_PHONE, csvPerson.getPhone());
        assertEquals(VALID_EMAIL, csvPerson.getEmail());
        assertEquals(VALID_ADDRESS, csvPerson.getAddress());
        assertEquals(VALID_TAGS, csvPerson.getTags());
    }

    @Test
    public void constructor_withInvalidName_storesInvalidName() {
        CsvAdaptedPerson csvPerson =
                new CsvAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertEquals(INVALID_NAME, csvPerson.getName());
    }

    @Test
    public void constructor_withNullName_storesNull() {
        CsvAdaptedPerson csvPerson =
                new CsvAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertNull(csvPerson.getName());
    }

    @Test
    public void constructor_withInvalidPhone_storesInvalidPhone() {
        CsvAdaptedPerson csvPerson =
                new CsvAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertEquals(INVALID_PHONE, csvPerson.getPhone());
    }

    @Test
    public void constructor_withNullPhone_storesNull() {
        CsvAdaptedPerson csvPerson =
                new CsvAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertNull(csvPerson.getPhone());
    }

    @Test
    public void constructor_withInvalidEmail_storesInvalidEmail() {
        CsvAdaptedPerson csvPerson =
                new CsvAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        assertEquals(INVALID_EMAIL, csvPerson.getEmail());
    }

    @Test
    public void constructor_withNullEmail_storesNull() {
        CsvAdaptedPerson csvPerson =
                new CsvAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS, VALID_TAGS);
        assertNull(csvPerson.getEmail());
    }

    @Test
    public void constructor_withInvalidAddress_storesInvalidAddress() {
        CsvAdaptedPerson csvPerson =
                new CsvAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_TAGS);
        assertEquals(INVALID_ADDRESS, csvPerson.getAddress());
    }

    @Test
    public void constructor_withNullAddress_storesNull() {
        CsvAdaptedPerson csvPerson =
                new CsvAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null, VALID_TAGS);
        assertNull(csvPerson.getAddress());
    }

    @Test
    public void constructor_withInvalidTags_storesInvalidTags() {
        // Append an invalid tag to the valid tags string.
        String invalidTags = VALID_TAGS + ";" + INVALID_TAG;
        CsvAdaptedPerson csvPerson =
                new CsvAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, invalidTags);
        assertEquals(invalidTags, csvPerson.getTags());
    }

    @Test
    public void deserializeCsvAdaptedPerson_fromCsvRow_correctlyDeserializes() throws Exception {
        // Prepare CSV data with a header row and one data row.
        String csvData = "name,phone,email,address,tags\n"
                + "Alice Pauline,94351253,alice@example.com,\"123, Jurong West Ave 6, #08-111\",friends";

        // Create a CsvMapper and build a schema based on CsvAdaptedPerson.
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(CsvAdaptedPerson.class).withHeader();

        // Deserialize the CSV data.
        MappingIterator<CsvAdaptedPerson> it = mapper.readerFor(CsvAdaptedPerson.class)
                .with(schema)
                .readValues(csvData);
        CsvAdaptedPerson csvPerson = it.next();

        // Verify that the fields were set correctly.
        assertEquals("Alice Pauline", csvPerson.getName());
        assertEquals("94351253", csvPerson.getPhone());
        assertEquals("alice@example.com", csvPerson.getEmail());
        assertEquals("123, Jurong West Ave 6, #08-111", csvPerson.getAddress());
        assertEquals("friends", csvPerson.getTags());
    }
}

package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class CsvAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_ROLE = " ";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_ROLE = BENSON.getRole().toString();
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
        assertEquals(VALID_ROLE, csvPerson.getRole());
        assertEquals(VALID_TAGS, csvPerson.getTags());
    }

    @Test
    public void deserializeCsvAdaptedPerson_fromCsvRow_correctlyDeserializes() throws Exception {
        // Prepare CSV data with a header row and one data row.
        String csvData = "name,phone,email,address,tags\n"
                + "Alice Pauline,94351253,alice@example.com,\"123, Jurong West Ave 6, #08-111\",Vendor,friends";

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
        assertEquals("Vendor", csvPerson.getRole());
        assertEquals("friends", csvPerson.getTags());
    }
}

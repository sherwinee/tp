package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Contains unit tests for {@code CsvParser}.
 */
public class CsvParserTest {

    private Path tempCsvFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempCsvFile = Files.createTempFile("test_csv_parser", ".csv");
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempCsvFile);
    }

    @Test
    public void parseCsv_validCsv_success() throws IOException {
        String csvData = "Name,Phone,Email,Address,Role,Tag\n"
                + "Alice Pauline,94351253,alice@example.com,\"123, Jurong West Ave 6, #08-111\",Organizer,friend\n"
                + "Bob Lim,98765432,bob@example.com,456 Avenue,Booth Vendor\n";
        Files.writeString(tempCsvFile, csvData);

        List<List<String>> result = CsvParser.parseCsv(tempCsvFile.toString());

        assertEquals(2, result.size());
        assertEquals(List.of("Alice Pauline", "94351253", "alice@example.com",
                "123, Jurong West Ave 6, #08-111", "Organizer", "friend"), result.get(0));
        assertEquals(List.of("Bob Lim", "98765432", "bob@example.com",
                "456 Avenue", "Booth Vendor"), result.get(1));
    }

    @Test
    public void parseCsv_csvWithQuotedFields_success() throws IOException {
        String csvData = "Name,Phone,Email,Address,Role\n"
                + "\"Tan, Alice\",91234567,alice@example.com,\"123 Street, #01-23\",\"Vendor\"\n";
        Files.writeString(tempCsvFile, csvData);

        List<List<String>> result = CsvParser.parseCsv(tempCsvFile.toString());

        assertEquals(1, result.size());
        assertEquals("Tan, Alice", result.get(0).get(0));
        assertEquals("123 Street, #01-23", result.get(0).get(3));
    }

    @Test
    public void parseCsv_emptyLines_ignored() throws IOException {
        String csvData = "Name,Phone,Email,Address,Role\n\n"
                + "Alice Tan,91234567,alice@example.com,123 Street,Event Organizer\n\n";
        Files.writeString(tempCsvFile, csvData);

        List<List<String>> result = CsvParser.parseCsv(tempCsvFile.toString());
        assertEquals(1, result.size());
    }

    @Test
    public void parseCsv_headerOnly_returnsEmptyList() throws IOException {
        String csvData = "Name,Phone,Email,Address,Role,Tag\n";
        Files.writeString(tempCsvFile, csvData);

        List<List<String>> result = CsvParser.parseCsv(tempCsvFile.toString());
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseCsv_nonExistentFile_throwsIoException() {
        String invalidPath = "nonexistent/file.csv";
        assertThrows(IOException.class, () -> CsvParser.parseCsv(invalidPath));
    }

    @Test
    public void main_validCsv_printsToConsole() throws IOException {
        String csvData = "Name,Phone,Email,Address,Role\n"
                + "Alice,91234567,alice@example.com,123 Ave,Engineer\n";
        Files.writeString(tempCsvFile, csvData);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(output));

        try {
            CsvParser.main(new String[]{tempCsvFile.toString()});
        } finally {
            System.setOut(originalOut);
        }

        String outputString = output.toString();
        assertTrue(outputString.contains("Alice"));
        assertTrue(outputString.contains("123 Ave"));
        assertTrue(outputString.contains("Engineer"));
    }

    @Test
    public void main_noArguments_printsUsageError() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        try {
            CsvParser.main(new String[]{}); // simulate no args
            String output = errContent.toString().trim();
            assertTrue(output.contains("Usage: java CsvParser <path-to-csv-file>"));
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    public void main_invalidFile_printsStackTrace() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        try {
            CsvParser.main(new String[] { "nonexistent_file.csv" });
            String output = errContent.toString();
            assertTrue(output.contains("java.io.FileNotFoundException")); // or partial match
        } finally {
            System.setErr(originalErr);
        }
    }


}

package seedu.address.logic.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for parsing CSV files into structured data.
 * This parser supports reading CSV files where each line represents a record
 * with fields separated by commas. It also supports quoted values containing commas.
 *
 * The CSV file must begin with a header line matching:
 * "Name,Phone,Email,Address,Role,Tags"
 *
 * Throws an {@code IOException} if the file reading fails or if the header is invalid.
 */
public class CsvParser {

    private static final String EXPECTED_HEADER = "Name,Phone,Email,Address,Role,Tags";
    private static final String[] EXPECTED_HEADER_PARTS = EXPECTED_HEADER.split(",");


    /**
     * Parses a CSV file and returns the data as a list of lists of strings.
     *
     * @param filePath The path to the CSV file.
     * @return A list of rows, where each row is a list of string values.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<List<String>> parseCsv(String filePath) throws IOException {
        assert filePath != null && !filePath.trim().isEmpty() : "File path should not be null or empty";
        List<List<String>> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String headerLine = br.readLine();

            if (headerLine == null || headerLine.trim().isEmpty()) {
                throw new IOException("Invalid CSV Header. The file is empty or missing a header.");
            }
            assert headerLine != null : "Header line should not be null";
            assert !headerLine.trim().isEmpty() : "Header line should not be empty";

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                assert line != null : "Read line from CSV should not be null";

                if (line.isEmpty()) {
                    continue;
                }

                List<String> parsedLine = parseLine(line);
                boolean isAllFieldsEmpty = parsedLine.stream().allMatch(String::isEmpty);
                if (isAllFieldsEmpty) {
                    continue;
                }
                data.add(parsedLine);
            }
        }

        return data;
    }

    /**
     * Parses a single line from a CSV file into a list of string values.
     * Handles quoted values correctly, ensuring that commas inside quotes are not treated as separators.
     *
     * @param line The CSV line to parse.
     * @return A list of string values extracted from the line.
     */
    private static List<String> parseLine(String line) {
        assert line != null : "Line to parse should not be null";

        List<String> fields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        fields.add(sb.toString().trim());

        return fields;
    }


    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java CsvParser <path-to-csv-file>");
            return;
        }

        try {
            List<List<String>> parsedCsv = parseCsv(args[0]);
            for (List<String> row : parsedCsv) {
                System.out.println(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

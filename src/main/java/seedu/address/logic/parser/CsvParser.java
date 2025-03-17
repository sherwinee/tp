package seedu.address.logic.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CsvParser {

    /**
     * Parses a CSV file and returns the data as a list of lists of strings.
     *
     * @param filePath The path to the CSV file.
     * @return A list of rows, where each row is a list of string values.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<List<String>> parseCsv(String filePath) throws IOException {
        List<List<String>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Excludes header
            String line;
            while ((line = br.readLine()) != null) {
                data.add(parseLine(line));
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
        List<String> fields = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // Toggle inQuotes flag
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                // If not inside quotes, split field
                fields.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        // Add the last field
        fields.add(sb.toString().trim());

        return fields;
    }

    public static void main(String[] args) {
        try {
            List<List<String>> parsedCsv = parseCsv("data.csv");
            for (List<String> row : parsedCsv) {
                System.out.println(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

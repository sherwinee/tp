package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;

public class VcfParserTest {

    private Path tempVcfFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempVcfFile = Files.createTempFile("test_vcf_parser", ".vcf");
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempVcfFile);
    }

    @Test
    public void parseVcf_validVcf_parsesSuccessfully() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "VERSION:4.0\n"
                + "FN:Alice Pauline\n"
                + "TEL:94351253\n"
                + "EMAIL:alice@example.com\n"
                + "ADR:;;123 Street;Singapore;;600123;Singapore\n"
                + "TITLE:Engineer\n"
                + "END:VCARD\n";

        Files.writeString(tempVcfFile, vcfData);
        List<Person> result = VcfParser.parseVcf(tempVcfFile.toString());

        assertEquals(1, result.size());
        Person alice = result.get(0);
        assertEquals("Alice Pauline", alice.getName().toString());
        assertEquals("94351253", alice.getPhone().toString());
        assertEquals("alice@example.com", alice.getEmail().toString());
        assertEquals("123 Street, Singapore, 600123, Singapore", alice.getAddress().toString());
        assertEquals("Engineer", alice.getRole().toString());
    }

    @Test
    public void parseVcf_missingName_collectsError() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "FN:\n"
                + "VERSION:4.0\n"
                + "TEL:98765432\n"
                + "EMAIL:bob2@example.com\n"
                + "ADR:12345;;456 Avenue;Singapore;;;Singapore\n"
                + "TITLE:Manager\n"
                + "END:VCARD\n";

        Files.writeString(tempVcfFile, vcfData);
        List<Person> persons = VcfParser.parseVcf(tempVcfFile.toString());

        assertTrue(persons.isEmpty());
        List<String> errors = VcfParser.getLastParseErrors();
        assertTrue(errors.stream().anyMatch(e -> e.contains("Missing required field 'FN'")));
    }

    @Test
    public void parseVcf_emptyVcf_collectsError() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "END:VCARD\n";

        Files.writeString(tempVcfFile, vcfData);
        List<Person> persons = VcfParser.parseVcf(tempVcfFile.toString());

        assertTrue(persons.isEmpty());
        List<String> errors = VcfParser.getLastParseErrors();
        assertTrue(errors.stream().anyMatch(e -> e.contains("Missing required field 'FN'")));
    }

    @Test
    public void parseVcf_missingAllFields_collectsError() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "FN:\n"
                + "VERSION:4.0\n"
                + "TEL:\n"
                + "EMAIL:\n"
                + "ADR:\n"
                + "TITLE:\n"
                + "END:VCARD\n";

        Files.writeString(tempVcfFile, vcfData);
        List<Person> persons = VcfParser.parseVcf(tempVcfFile.toString());

        assertTrue(persons.isEmpty());
        List<String> errors = VcfParser.getLastParseErrors();
        assertTrue(errors.stream().anyMatch(e -> e.contains("Missing required field")));
    }

    @Test
    public void parseVcf_multiplePhone_collectsError() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "VERSION:4.0\n"
                + "FN:Bob Lim\n"
                + "TEL:98765432\n"
                + "TEL:81243124\n"
                + "EMAIL:bob2@example.com\n"
                + "ADR:12345;;456 Avenue;Singapore;;;Singapore\n"
                + "TITLE:Manager\n"
                + "END:VCARD\n";

        Files.writeString(tempVcfFile, vcfData);
        List<Person> persons = VcfParser.parseVcf(tempVcfFile.toString());

        assertTrue(persons.isEmpty());
        List<String> errors = VcfParser.getLastParseErrors();
        assertTrue(errors.stream().anyMatch(e -> e.contains("Cannot contain more than one telephone number")));
    }

    @Test
    public void parseVcf_multipleTitle_collectsError() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "VERSION:4.0\n"
                + "FN:Bob Lim\n"
                + "TEL:98765432\n"
                + "EMAIL:bob2@example.com\n"
                + "ADR;TYPE=home:;;Blk 123, Jurong West Ave 6, #08-111;Singapore;;600123;Singapore\n"
                + "TITLE:Manager\n"
                + "TITLE:Booth Vendor\n"
                + "END:VCARD\n";

        Files.writeString(tempVcfFile, vcfData);
        List<Person> persons = VcfParser.parseVcf(tempVcfFile.toString());

        assertTrue(persons.isEmpty());
        List<String> errors = VcfParser.getLastParseErrors();
        assertTrue(errors.stream().anyMatch(e -> e.contains("Cannot contain more than one title")));
    }

    @Test
    public void parseVcf_multipleEmails_collectsError() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "VERSION:4.0\n"
                + "FN:Bob Lim\n"
                + "TEL:98765432\n"
                + "EMAIL:bob@example.com\n"
                + "EMAIL:bob2@example.com\n"
                + "ADR:;;456 Avenue;Singapore;;;Singapore\n"
                + "TITLE:Manager\n"
                + "END:VCARD\n";

        Files.writeString(tempVcfFile, vcfData);
        List<Person> persons = VcfParser.parseVcf(tempVcfFile.toString());

        assertTrue(persons.isEmpty());
        List<String> errors = VcfParser.getLastParseErrors();
        assertTrue(errors.stream().anyMatch(e -> e.contains("Cannot contain more than one email address")));
    }

    @Test
    public void parseVcf_addressWithFullAddress_combinesCorrectly() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "VERSION:4.0\n"
                + "FN:Carl Tan\n"
                + "TEL:82345678\n"
                + "EMAIL:carl@example.com\n"
                + "ADR;TYPE=work:PO Box 10;Suite 900;10 Downing St;London;Greater London;SW1A 2AA;United Kingdom\n"
                + "TITLE:Manager\n"
                + "END:VCARD\n";


        Files.writeString(tempVcfFile, vcfData);
        List<Person> persons = VcfParser.parseVcf(tempVcfFile.toString());

        assertEquals("PO Box 10, Suite 900, 10 Downing St, London, Greater London, SW1A 2AA, United Kingdom",
                persons.get(0).getAddress().toString());
    }

    @Test
    public void parseVcf_nonExistentFile_throwsIoException() {
        String invalidPath = "nonexistent_file.vcf";

        IOException thrown = assertThrows(IOException.class, () -> VcfParser.parseVcf(invalidPath));
        assertTrue(thrown.getMessage().contains("Failed to read VCF file"));
    }

    @Test
    public void parseVcf_missingTitle_returnsUnassigned() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "VERSION:4.0\n"
                + "FN:Alice\n"
                + "TEL:91234567\n"
                + "EMAIL:alice@example.com\n"
                + "ADR:;;123 Street;Singapore;;123456;Singapore\n"
                + "END:VCARD\n";

        Files.writeString(tempVcfFile, vcfData);
        List<Person> people = VcfParser.parseVcf(tempVcfFile.toString());

        assertEquals("Unassigned", people.get(0).getRole().toString());
    }

    @Test
    public void parseVcf_missingEmail_collectsError() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "VERSION:4.0\n"
                + "FN:Alice Pauline\n"
                + "TEL:94351253\n"
                + "ADR:;;123 Street;Singapore;;600123;Singapore\n"
                + "TITLE:Engineer\n"
                + "END:VCARD\n";

        Files.writeString(tempVcfFile, vcfData);
        List<Person> persons = VcfParser.parseVcf(tempVcfFile.toString());

        assertTrue(persons.isEmpty());

        List<String> errors = VcfParser.getLastParseErrors();
        assertTrue(errors.stream().anyMatch(e -> e.contains("Missing required field 'Email'")));
    }

    @Test
    public void parseVcf_multipleNames_collectsError() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "VERSION:4.0\n"
                + "FN:Alice Pauline\n"
                + "FN:Bob Lim\n"
                + "TEL:98765432\n"
                + "EMAIL:bob@example.com\n"
                + "ADR:;;456 Avenue;Singapore;;;Singapore\n"
                + "TITLE:Manager\n"
                + "END:VCARD\n";

        Files.writeString(tempVcfFile, vcfData);
        List<Person> persons = VcfParser.parseVcf(tempVcfFile.toString());

        assertTrue(persons.isEmpty());

        List<String> errors = VcfParser.getLastParseErrors();
        assertTrue(errors.stream().anyMatch(e -> e.contains("Cannot contain more than one formatted name")));
    }

    @Test
    public void parseVcf_invalidPhoneFormat_collectsError() throws IOException {
        String vcfData = "BEGIN:VCARD\n"
                + "VERSION:4.0\n"
                + "FN:Alice Pauline\n"
                + "TEL:invalid-phone-number\n"
                + "EMAIL:alice@example.com\n"
                + "ADR:;;123 Street;Singapore;;600123;Singapore\n"
                + "TITLE:Engineer\n"
                + "END:VCARD\n";

        Files.writeString(tempVcfFile, vcfData);
        List<Person> persons = VcfParser.parseVcf(tempVcfFile.toString());

        assertTrue(persons.isEmpty());

        List<String> errors = VcfParser.getLastParseErrors();
        assertTrue(errors.stream().anyMatch(e -> e.contains("Invalid field")));
        assertTrue(errors.stream().anyMatch(e ->
                e.contains("Phone numbers should only contain numeric digits between 3 and 15 characters in length")));
    }
}

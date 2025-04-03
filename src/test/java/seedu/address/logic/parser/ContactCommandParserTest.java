package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ContactCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ContactCommandParserTest {

    private final ContactCommandParser parser = new ContactCommandParser();

    @Test
    public void parse_validArgs_returnsContactCommand() throws Exception {
        ContactCommand expectedCommand = new ContactCommand(Index.fromOneBased(1));
        assertEquals(expectedCommand, parser.parse("1"));
    }

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(""));
    }

    @Test
    public void parse_nonNumericArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("abc"));
    }

    @Test
    public void parse_negativeIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("-1"));
    }

    @Test
    public void parse_zeroIndex_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("0"));
    }

    @Test
    public void parse_validArgsWithExtraWhitespace_returnsContactCommand() throws Exception {
        ContactCommand expectedCommand = new ContactCommand(Index.fromOneBased(2));
        assertEquals(expectedCommand, parser.parse("  2  "));
    }

    @Test
    public void parse_mixedLettersAndNumbers_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1a"));
        assertThrows(ParseException.class, () -> parser.parse("a1"));
    }

    @Test
    public void parse_specialCharacters_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("@3"));
        assertThrows(ParseException.class, () -> parser.parse("3!"));
    }

    @Test
    public void parse_extremelyLargeNumber_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("99999999999999999999"));
    }

    @Test
    public void parse_multipleSpacesBetweenNumbers_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("1 2"));
    }
}

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
    public void parse_extraWhitespace_validArgs_returnsContactCommand() throws Exception {
        ContactCommand expectedCommand = new ContactCommand(Index.fromOneBased(2));
        assertEquals(expectedCommand, parser.parse("  2  "));
    }
}

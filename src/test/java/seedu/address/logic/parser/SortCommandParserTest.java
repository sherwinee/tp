package seedu.address.logic.parser;

import static seedu.address.logic.commands.SortCommand.MESSAGE_INVALID_ORDER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

/**
 * Test class for SortCommandParser.
 */
public class SortCommandParserTest {

    private final SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "invalid", MESSAGE_INVALID_ORDER);
        assertParseFailure(parser, "ascending", MESSAGE_INVALID_ORDER);
        assertParseFailure(parser, "123", MESSAGE_INVALID_ORDER);
        assertParseFailure(parser, "", MESSAGE_INVALID_ORDER);
    }
}

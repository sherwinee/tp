package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.SortCommand.MESSAGE_USAGE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

/**
 * Test class for SortCommandParser.
 */
public class SortCommandParserTest {

    private final SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "invalid", String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        assertParseFailure(parser, "ascending", String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        assertParseFailure(parser, "123", String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT, MESSAGE_USAGE));
    }
}

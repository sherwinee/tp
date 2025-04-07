package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteAllWithTagCommand;
import seedu.address.model.person.TagMatchesKeywordPredicate;

public class DeleteAllWithTagCommandParserTest {

    private DeleteAllWithTagCommandParser parser = new DeleteAllWithTagCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            DeleteAllWithTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_containsWhitespace_throwsParseException() {
        assertParseFailure(parser, "friends colleagues", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            DeleteAllWithTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsDeleteAllWithTagCommand() {
        // no leading and trailing whitespaces
        DeleteAllWithTagCommand expectedFindCommand =
                new DeleteAllWithTagCommand(new TagMatchesKeywordPredicate(Collections.singletonList("friends")));
        assertParseSuccess(parser, "friends", expectedFindCommand);
    }

}

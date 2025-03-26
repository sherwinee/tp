package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteAllCommand;
import seedu.address.model.person.TagsContainsKeywordsPredicate;

public class DeleteAllCommandParserTest {

    private DeleteAllCommandParser parser = new DeleteAllCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            DeleteAllCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsDeleteAllCommand() {
        // no leading and trailing whitespaces
        DeleteAllCommand expectedFindCommand =
                new DeleteAllCommand(new TagsContainsKeywordsPredicate(Collections.singletonList("friends")));
        assertParseSuccess(parser, "friends", expectedFindCommand);
    }

}

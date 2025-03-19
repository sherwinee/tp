package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.TagsContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertAll("empty argument tests",
            () -> assertParseFailure(parser, "     ", expectedMessage),
            () -> assertParseFailure(parser, "n/     ", expectedMessage),
            () -> assertParseFailure(parser, "t/     ", expectedMessage)
        );
    }

    @Test
    public void parse_multiplePrefixes_throwsParseException() {
        assertParseFailure(parser, "n/Alice t/friends", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }


    @Test
    public void parse_validNameArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "n/Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, "n/ \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_validTagArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new TagsContainsKeywordsPredicate(Arrays.asList("friend", "colleague")));
        assertParseSuccess(parser, "t/friend colleague", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, "t/ \n friend \n \t colleague  \t", expectedFindCommand);

        // case sensitivity
        assertParseSuccess(parser, "t/Friend Colleague", expectedFindCommand);
    }


}

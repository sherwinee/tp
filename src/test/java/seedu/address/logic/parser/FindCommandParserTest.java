package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_ALICE;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_COLLEAGUE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_ALICE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_COLLEAGUE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ROLE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.ROLE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ROLE_DESC_BOB;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PhoneNumberContainsKeywordsPredicate;
import seedu.address.model.person.RoleContainsKeywordsPredicate;
import seedu.address.model.person.TagsContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertAll("empty argument tests", () -> assertParseFailure(parser, "    ", expectedMessage), (
                ) -> assertParseFailure(parser, "n/     ", expectedMessage), (
        ) -> assertParseFailure(parser, "t/     ", expectedMessage));
    }

    @Test
    public void parse_multiplePrefixes_throwsParseException() {
        assertParseFailure(parser, NAME_DESC_ALICE + " " + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validNameArgs_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList(VALID_NAME_ALICE, VALID_NAME_BOB)));
        assertParseSuccess(parser, NAME_DESC_ALICE + " " + NAME_DESC_BOB, expectedFindCommand);
    }

    @Test
    public void parse_validTagArgs_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new TagsContainsKeywordsPredicate(
                        Arrays.asList(VALID_TAG_COLLEAGUE, VALID_TAG_FRIEND)));
        assertParseSuccess(parser, TAG_DESC_COLLEAGUE + " " + TAG_DESC_FRIEND, expectedFindCommand);
    }

    @Test
    public void parse_validPhoneArgs_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new PhoneNumberContainsKeywordsPredicate(
                        Arrays.asList(VALID_PHONE_AMY, VALID_PHONE_BOB)));
        assertParseSuccess(parser, PHONE_DESC_AMY + " " + PHONE_DESC_BOB, expectedFindCommand);
    }

    @Test
    public void parse_validRoleArgs_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new RoleContainsKeywordsPredicate(
                        Arrays.asList(VALID_ROLE_AMY, VALID_ROLE_BOB)));
        assertParseSuccess(parser, ROLE_DESC_AMY + " " + ROLE_DESC_BOB, expectedFindCommand);
    }

}

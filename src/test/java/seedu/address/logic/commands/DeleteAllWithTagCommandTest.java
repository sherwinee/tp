package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.TagMatchesKeywordPredicate;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteAllWithTagCommand}.
 */
public class DeleteAllWithTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validTag_success() {
        TagMatchesKeywordPredicate predicate = prepareTagPredicate("friends");
        DeleteAllWithTagCommand deleteAllCommand = new DeleteAllWithTagCommand(predicate);

        String expectedMessage = String.format(Messages.MESSAGE_PERSONS_DELETED_OVERVIEW, 3);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteAllPersons(predicate);

        assertCommandSuccess(deleteAllCommand, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA, GEORGE), model.getFilteredPersonList());
    }

    @Test
    public void equals() {
        TagMatchesKeywordPredicate deleteAllFirstPredicate =
                new TagMatchesKeywordPredicate(Collections.singletonList("first"));
        TagMatchesKeywordPredicate deleteAllSecondPredicate =
                new TagMatchesKeywordPredicate(Collections.singletonList("second"));

        DeleteAllWithTagCommand deleteAllFirstCommand = new DeleteAllWithTagCommand(deleteAllFirstPredicate);
        DeleteAllWithTagCommand deleteAllSecondCommand = new DeleteAllWithTagCommand(deleteAllSecondPredicate);

        // same object -> returns true
        assertTrue(deleteAllFirstCommand.equals(deleteAllFirstCommand));

        // same values -> returns true
        DeleteAllWithTagCommand deleteAllFirstCommandCopy = new DeleteAllWithTagCommand(deleteAllFirstPredicate);
        assertTrue(deleteAllFirstCommand.equals(deleteAllFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteAllFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteAllFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteAllFirstCommand.equals(deleteAllSecondCommand));
    }

    /**
     * Parses {@code userInput} into a {@code TagMatchesKeywordPredicate}.
     */
    private TagMatchesKeywordPredicate prepareTagPredicate(String userInput) {
        return new TagMatchesKeywordPredicate(Arrays.asList(userInput));
    }

}

package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.TagsContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteAllWithTagCommand}.
 */
public class DeleteAllWithTagCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validTag_success() {
        TagsContainsKeywordsPredicate predicate = prepareTagPredicate(VALID_TAG_FRIEND);
        DeleteAllWithTagCommand deleteAllCommand = new DeleteAllWithTagCommand(predicate);

        String expectedMessage = String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, 4);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteAllPersons(predicate);

        assertCommandSuccess(deleteAllCommand, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA, GEORGE), model.getFilteredPersonList());
    }

    @Test
    public void equals() {
        TagsContainsKeywordsPredicate deleteAllFirstPredicate =
                new TagsContainsKeywordsPredicate(Collections.singletonList("first"));
        TagsContainsKeywordsPredicate deleteAllSecondPredicate =
                new TagsContainsKeywordsPredicate(Collections.singletonList("second"));

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

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code TagsContainsKeywordsPredicate}.
     */
    private TagsContainsKeywordsPredicate prepareTagPredicate(String userInput) {
        return new TagsContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }

}

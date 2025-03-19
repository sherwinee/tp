package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for SortCommand.
 */
public class SortCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_sortAscending_sortsListCorrectly() {
        SortCommand sortCommand = new SortCommand(true);
        expectedModel.sortFilteredPersonList(Comparator.comparing(Person::getName).thenComparing(Person::getPhone));
        assertCommandSuccess(sortCommand, model, String.format(SortCommand.MESSAGE_SUCCESS, "ascending"), expectedModel);
    }

    @Test
    public void execute_sortDescending_sortsListCorrectly() {
        SortCommand sortCommand = new SortCommand(false);
        expectedModel.sortFilteredPersonList(Comparator.comparing(Person::getName).thenComparing(Person::getPhone).reversed());
        assertCommandSuccess(sortCommand, model, String.format(SortCommand.MESSAGE_SUCCESS, "descending"), expectedModel);
    }

    @Test
    public void execute_emptyList_showsNoContactsMessage() {
        model = new ModelManager(); // Empty address book
        SortCommand sortCommand = new SortCommand(true);
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_NO_CONTACTS, model);
    }
}

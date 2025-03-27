package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class ContactCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        Person person = new PersonBuilder().build();
        model.addPerson(person);

        ContactCommand command = new ContactCommand(Index.fromOneBased(1));
        CommandResult result = command.execute(model);

        assertEquals(String.format(ContactCommand.MESSAGE_SUCCESS, 1), result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        ContactCommand command = new ContactCommand(Index.fromOneBased(100));
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        ContactCommand command = new ContactCommand(Index.fromOneBased(1));
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_differentObjects_returnsFalse() {
        ContactCommand command1 = new ContactCommand(Index.fromOneBased(1));
        ContactCommand command2 = new ContactCommand(Index.fromOneBased(2));

        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_null_returnsFalse() {
        ContactCommand command = new ContactCommand(Index.fromOneBased(1));
        assertFalse(command.equals(null));
    }
}

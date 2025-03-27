package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.Assert;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Role(null));
    }

    @Test
    public void constructor_invalidRole_throwsIllegalArgumentException() {
        String invalidRole = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Role(invalidRole));
    }

    @Test
    public void isValidRole() {
        // null role
        Assert.assertThrows(NullPointerException.class, () -> Role.isValidRole(null));

        // invalid roles
        assertFalse(Role.isValidRole("")); // empty string
        assertFalse(Role.isValidRole(" ")); // spaces only
        assertFalse(Role.isValidRole("^")); // only non-alphabetic characters
        assertFalse(Role.isValidRole("developer*")); // contains non-alphabetic characters
        assertFalse(Role.isValidRole("123")); // numbers
        assertFalse(Role.isValidRole("developer_1")); // alphanumeric with underscore

        // valid roles
        assertTrue(Role.isValidRole("developer")); // alphabets only
        assertTrue(Role.isValidRole("Software Engineer")); // with space
        assertTrue(Role.isValidRole("CEO")); // all caps
        assertTrue(Role.isValidRole("a")); // single character
        assertTrue(Role.isValidRole("Product Manager")); // multiple words
    }

    @Test
    public void equals() {
        Role role = new Role("Developer");

        // same values -> returns true
        assertEquals(role, new Role("Developer"));

        // same object -> returns true
        assertEquals(role, role);

        // null -> returns false
        assertNotEquals(null, role);

        // different types -> returns false
        assertNotEquals(5, role);

        // different values -> returns false
        assertNotEquals(role, new Role("Manager"));
    }

    @Test
    public void hashCode_test() {
        Role role1 = new Role("Developer");
        Role role2 = new Role("Developer");
        Role role3 = new Role("Manager");

        // same role -> same hashcode
        assertEquals(role1.hashCode(), role2.hashCode());

        // different role -> different hashcode
        assertNotEquals(role1.hashCode(), role3.hashCode());
    }

    @Test
    public void compareTo() {
        Role developer = new Role("Developer");
        Role manager = new Role("Manager");
        Role developerLower = new Role("developer");
        Role developerWithSpace = new Role("Developer ");

        // same object -> returns 0
        assertEquals(0, developer.compareTo(developer));

        // case insensitive comparison -> returns 0
        assertEquals(0, developer.compareTo(developerLower));

        // different role -> returns non-zero
        assertTrue(developer.compareTo(manager) < 0);
        assertTrue(manager.compareTo(developer) > 0);

        // same role with trailing space -> returns 0
        assertEquals(0, developer.compareTo(developerWithSpace));
    }

    @Test
    public void toString_test() {
        String roleString = "Software Engineer";
        Role role = new Role(roleString);
        assertEquals(roleString, role.toString());
    }
}
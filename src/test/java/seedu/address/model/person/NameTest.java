package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("^")); // only non-alphanumeric characters
        assertFalse(Name.isValidName("peter*")); // contains non-alphanumeric characters

        // valid name
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }

    @Test
    public void hashCode_test() {
        Name name1 = new Name("Alice");
        Name name2 = new Name("Alice");
        Name name3 = new Name("Bob");

        // Same object -> same hashCode
        assertEquals(name1.hashCode(), name1.hashCode());

        // Equal objects -> same hashCode
        assertEquals(name1.hashCode(), name2.hashCode());

        // Different objects -> different hashCode
        assertNotEquals(name1.hashCode(), name3.hashCode());
    }

    @Test
    public void compareTo_test() {
        Name nameA = new Name("Alice");
        Name nameB = new Name("Bob");
        Name nameC = new Name("alice"); // Same name, different case

        // Alphabetical order comparison
        assertTrue(nameA.compareTo(nameB) < 0); // Alice < Bob
        assertTrue(nameB.compareTo(nameA) > 0); // Bob > Alice
        assertEquals(0, nameA.compareTo(nameC)); // Alice == alice (case-insensitive)
    }
}

package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.BENSON;

import org.junit.jupiter.api.Test;

public class VcfAdaptedPersonTest {

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_ROLE = BENSON.getRole().toString();

    @Test
    public void constructor_fromPerson_copiesAllFieldsCorrectly() {
        VcfAdaptedPerson vcfPerson = new VcfAdaptedPerson(BENSON);

        assertEquals(VALID_NAME, vcfPerson.getFn());
        assertEquals(VALID_PHONE, vcfPerson.getTel());
        assertEquals(VALID_EMAIL, vcfPerson.getEmail());
        assertEquals(VALID_ADDRESS, vcfPerson.getAdr());
        assertEquals(VALID_ROLE, vcfPerson.getTitle());
    }
}

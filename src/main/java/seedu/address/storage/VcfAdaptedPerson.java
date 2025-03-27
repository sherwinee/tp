package seedu.address.storage;

import seedu.address.model.person.Person;

/**
 * ez-vcard-friendly version of {@link Person}.
 */
public class VcfAdaptedPerson {
    private final String fn;
    private final String tel;
    private final String email;
    private final String adr;
    private final String title;

    VcfAdaptedPerson(Person p) {
        this.fn = p.getName().fullName;
        this.tel = p.getPhone().value;
        this.email = p.getEmail().value;
        this.adr = p.getAddress().value;
        this.title = p.getRole().value;
    }

    public String getFn() {
        return fn;
    }

    public String getTel() {
        return tel;
    }

    public String getEmail() {
        return email;
    }

    public String getAdr() {
        return adr;
    }

    public String getTitle() {
        return title;
    }
}

package seedu.address.storage;

import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import seedu.address.model.person.Person;

/**
 * CSV-friendly version of {@link Person}.
 */
@JsonPropertyOrder({ "name", "phone", "email", "address", "role", "tags" })
class CsvAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String role;
    private final String tags;

    /**
     * Constructs a {@code CsvAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public CsvAdaptedPerson(@JsonProperty("name") String name,
                            @JsonProperty("phone") String phone,
                            @JsonProperty("email") String email,
                            @JsonProperty("address") String address,
                            @JsonProperty("role") String role,
                            @JsonProperty("tags") String tags) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
        this.tags = tags;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public CsvAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        this.role = source.getRole().value;
        tags = source.getTags().stream()
                .map(tag -> tag.tagName)
                .collect(Collectors.joining(";"));
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }

    public String getTags() {
        return tags;
    }
}

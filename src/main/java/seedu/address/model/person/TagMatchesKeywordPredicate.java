package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that one of a {@code Person}'s {@code Tags} exactly match (case-insensitive) the keyword given.
 */
public class TagMatchesKeywordPredicate implements Predicate<Person> {
    private final List<String> keyword;

    public TagMatchesKeywordPredicate(List<String> keyword) {
        this.keyword = keyword;
    }

    // expecting singleton list, get(0) alright?
    @Override
    public boolean test(Person person) {
        return person.getTags().stream()
                .anyMatch(tag -> StringUtil.containsWordIgnoreCase(tag.getTag(), keyword.get(0)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagMatchesKeywordPredicate)) {
            return false;
        }

        TagMatchesKeywordPredicate otherTagMatchesKeywordPredicate = (TagMatchesKeywordPredicate) other;
        return keyword.equals(otherTagMatchesKeywordPredicate.keyword);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keyword", keyword).toString();
    }

}


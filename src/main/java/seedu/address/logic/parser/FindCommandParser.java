package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PHONE;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PhoneNumberContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;


/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (trimmedArgs.startsWith("/p")) {
            // Search by phone number
            String phoneKeywords = trimmedArgs.substring(2).trim();
            if (phoneKeywords.isEmpty()) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }

            List<String> keywords = Arrays.asList(phoneKeywords.split("\\s+"));
            for (String keyword : keywords) {
                if (!Phone.isValidPhone(keyword)) {
                    throw new ParseException(MESSAGE_INVALID_PHONE);
                }
            }

            Predicate<Person> predicate = new PhoneNumberContainsKeywordsPredicate(keywords);
            return new FindCommand(predicate);
        } else {
            // Search by name
            List<String> keywords = Arrays.asList(trimmedArgs.split("\\s+"));
            Predicate<Person> predicate = new NameContainsKeywordsPredicate(keywords);
            return new FindCommand(predicate);
        }
    }

}

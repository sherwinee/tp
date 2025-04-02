package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.ImportCommand.IMPORT_DIR_PREFIX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ImportCommand;

public class ImportCommandParserTest {

    private ImportCommandParser parser = new ImportCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ImportCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validCsvFilePath_success() {
        String filePath = "address_book.csv";
        ImportCommand expectedCommand = new ImportCommand(Path.of(IMPORT_DIR_PREFIX + filePath));
        assertParseSuccess(parser, filePath, expectedCommand);
    }

    @Test
    public void parse_validVcfFilePath_success() {
        String filePath = "address_book.vcf";
        ImportCommand expectedCommand = new ImportCommand(Path.of(IMPORT_DIR_PREFIX + filePath));
        assertParseSuccess(parser, filePath, expectedCommand);
    }

    @Test
    public void parse_invalidFileFormat_throwsParseException() {
        assertParseFailure(parser, "addressbook", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ImportCommand.MESSAGE_CONSTRAINTS));

        assertParseFailure(parser, "addressbook.doc", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ImportCommand.MESSAGE_CONSTRAINTS));
    }
}

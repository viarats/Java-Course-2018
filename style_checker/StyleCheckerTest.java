package bg.sofia.uni.fmi.mjt.stylechecker;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

public class StyleCheckerTest {
    private static final String FIXME_WILDCARDS = "// FIXME Wildcards are " +
                                                  "not allowed in import statements";
    private static final String FIXME_STATEMENTS = "// FIXME Only one statement per line is allowed";
    private static final String FIXME_BRACKETS = "// FIXME Opening brackets" +
                                                 " should be placed on the same line as the declaration";
    private static final String FIXME_DEFAULT_LINE_LENGTH = "// FIXME Length of line " +
                                                            "should not exceed 100 characters";
    private static final String FIXME_CUSTOM_LINE_LENGTH = "// FIXME Length of line " +
                                                           "should not exceed 10 characters";

    private static final String PROPERTIES = "statements.per.line.check.active=true\n" +
                                             "length.of.line.check.active=true\n" +
                                             "wildcard.import.check.active=false\n" +
                                             "opening.bracket.check.active=false\n" +
                                             "line.length.limit=10";


    private static final int DEFAULT_LINE_LENGTH = 100;

    static private StyleChecker checker;
    static private StyleChecker defaultChecker;

    @Before
    public void setup() {
        checker = new StyleChecker(new ByteArrayInputStream(PROPERTIES.getBytes()));
        defaultChecker = new StyleChecker();
    }

    @Test
    public void testWildcardInImportStatement() {
        String actual = initializeStreams("import java.util.*;", defaultChecker);

        assertEquals(FIXME_WILDCARDS + "\nimport java.util.*;", actual.trim());
    }

    @Test
    public void testWildcardCheckTurnedOff() {
        String actual = initializeStreams("import java.util.*;", checker);

        assertEquals("import java.util.*;", actual.trim());
    }

    @Test
    public void testSingleStatementPerLine() {
        String actual = initializeStreams("doSomething;;;;", defaultChecker);

        assertEquals("doSomething;;;;", actual.trim());
    }

    @Test
    public void testMultipleStatementsPerLine() {
        String actual = initializeStreams("doSomething(); counter++;" +
                        " doSomethingElse(counter);", defaultChecker);

        assertEquals(FIXME_STATEMENTS +
                        "\ndoSomething(); counter++; doSomethingElse(counter);", actual.trim());
    }

    @Test
    public void testLineLengthExceedWithDefaultValue() {
        String actual = initializeStreams("rep".repeat(DEFAULT_LINE_LENGTH), defaultChecker);

        assertEquals(FIXME_DEFAULT_LINE_LENGTH + '\n' + "rep".repeat(DEFAULT_LINE_LENGTH),
                    actual.trim());
    }

    @Test
    public void testLineLengthExceedWithCustomValue() {
        String actual = initializeStreams("doSomething", checker);
        assertEquals(FIXME_CUSTOM_LINE_LENGTH + "\ndoSomething", actual.trim());
    }

    @Test
    public void testLineLengthExceedForImportStatements() {
        String actual = initializeStreams("import java.util.List", checker);

        assertEquals("import java.util.List", actual.trim());
    }

    @Test
    public void testNewlineOpeningBrackets() {
        String actual = initializeStreams("\t{", defaultChecker);

        assertEquals(FIXME_BRACKETS + "\n\t{", actual.trim());
    }

    @Test
    public void testMultipleViolationsPerLine() {
        String actual = initializeStreams("doSomething(); counter++;" +
                " doSomethingElse(counter);", checker);

        assertEquals(FIXME_CUSTOM_LINE_LENGTH + '\n' + FIXME_STATEMENTS +
                "\ndoSomething(); counter++; doSomethingElse(counter);", actual.trim());
    }

    private String initializeStreams(String inputContent, StyleChecker checker) {
        ByteArrayInputStream input = new ByteArrayInputStream(inputContent.getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        checker.checkStyle(input, output);

        return new String(output.toByteArray());
    }
}
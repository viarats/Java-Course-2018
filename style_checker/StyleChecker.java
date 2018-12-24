package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

public class StyleChecker {
    private Properties properties;

    public StyleChecker() {
        properties = new Properties();

        properties.setProperty("wildcard.import.check.active", "true");
        properties.setProperty("statements.per.line.check.active", "true");
        properties.setProperty("opening.bracket.check.active", "true");
        properties.setProperty("length.of.line.check.active", "true");
        properties.setProperty("line.length.limit", "100");
    }

    public StyleChecker(InputStream inputStream) {
        this();
        try (ByteArrayInputStream input = new ByteArrayInputStream(readInputStream(inputStream))) {
            properties.load(input);

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public void checkStyle(InputStream source, OutputStream output) {
        String input = readSource(source);
        String[] lines = input.split("\n");

        StringBuilder outputContent = new StringBuilder();

        for (String line : lines) {
            outputContent.append(writeComments(line));
            outputContent.append('\n');
            outputContent.append(line);
        }

        writeOutput(output, outputContent.toString());
    }

    private String writeComments(String line) {
        StringBuilder output = new StringBuilder();
        if (!isImportStatement(line) &&
                properties.getProperty("length.of.line.check.active").equals("true")) {
            if (exceededLength(line)) {
                output.append("\n// FIXME Length of line should not exceed ");
                output.append(properties.getProperty("line.length.limit"));
                output.append(" characters");
            }
        }
        if (properties.getProperty("wildcard.import.check.active").equals("true") &&
                containsWildcards(line)) {
            output.append("\n// FIXME Wildcards are not allowed in import statements");
        }
        if (properties.getProperty("statements.per.line.check.active").equals("true") &&
                !containsSingleStatement(line)) {
            output.append("\n// FIXME Only one statement per line is allowed");
        }
        if (properties.getProperty("opening.bracket.check.active").equals("true") &&
                containsOpeningBracket(line) && hasValidPosition(line)) {
            output.append("\n// FIXME Opening brackets should" +
                    " be placed on the same line as the declaration");
        }

        return output.toString();
    }

    private boolean isImportStatement(String line) {
        return line.contains("import");
    }

    private boolean exceededLength(String line) {
        return line.trim().length() > Integer.parseInt(properties.getProperty("line.length.limit"));
    }

    private boolean containsWildcards(String line) {
        return isImportStatement(line) && line.contains("*");
    }

    private boolean containsOpeningBracket(String line) {
        return line.contains("{");
    }

    private boolean hasValidPosition(String line) {
        return line.trim().indexOf('{') == 0;
    }

    private boolean containsSingleStatement(String line) {
        String[] statements = line.split(";");

        return !line.contains(";") || statements.length == 1;
    }

    private byte[] readInputStream(InputStream input) throws IOException {
        return input.readAllBytes();
    }

    private String readSource(InputStream source) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(source))) {

            StringBuilder buffer = new StringBuilder();
            String currentLine;

            currentLine = br.readLine();
            buffer.append(currentLine);

            while ((currentLine = br.readLine()) != null) {
                buffer.append('\n');
                buffer.append(currentLine);
            }

            return buffer.toString();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    private void writeOutput(OutputStream output, String outputContent) {
        try (output) {
            output.write(outputContent.getBytes());
            output.flush();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
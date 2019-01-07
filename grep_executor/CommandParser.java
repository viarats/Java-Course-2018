package bg.sofia.uni.fmi.mjt.grep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandParser {
    private boolean searchWholeWords;
    private boolean caseSensitivity;
    private String stringToFind;
    private String pathToDirectoryTree;
    private int numberOfParallelThreads;
    private String pathToOutputFile;

    public CommandParser(String command) {
        List<String> toWords = new ArrayList<>(Arrays.asList(command.split("\\s")));
        if (!toWords.get(0).equals("grep")) {
             throw new UnsupportedOperationException("Unknown Operation");
        }
        if (toWords.get(1).equals("-w") || toWords.get(2).equals("-w")) {
            searchWholeWords = true;
            toWords.remove("-w");
        }
        if (toWords.get(1).equals("-i")) {
            caseSensitivity = true;
            toWords.remove(1);
        }

        StringBuilder sb = new StringBuilder();

        if (!toWords.get(toWords.size() - 1).matches("\\d+")) {
            pathToOutputFile = toWords.get(toWords.size() - 1);
            toWords.remove(toWords.size() - 1);
        }
        numberOfParallelThreads = Integer.parseInt(toWords.get(toWords.size() - 1));
        pathToDirectoryTree = toWords.get(toWords.size() - 2);
        for (int i = 1; i < toWords.size() - 2; i++) {
            sb.append(toWords.get(i)).append(" ");
        }
        stringToFind = sb.toString().trim();
    }

    public String getStringToFind() {
        return stringToFind;
    }

    public String getPathToDirectoryTree() {
        return pathToDirectoryTree;
    }

    public int getNumberOfParallelThreads() {
        return numberOfParallelThreads;
    }

    public String getPathToOutputFile() {
        return pathToOutputFile;
    }

    public boolean searchWholeWords() {
        return searchWholeWords;
    }

    public boolean isCaseSensitive() {
        return caseSensitivity;
    }
}

package bg.sofia.uni.fmi.mjt.grep;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GrepCommandExecutor {
    private CommandParser parser;
    private ExecutorService pool;

    public GrepCommandExecutor(CommandParser parser) {
        this.parser = parser;
        this.pool = Executors.newFixedThreadPool(parser.getNumberOfParallelThreads());
    }

    public void exploreFilesInDirectoryTree(String directory) {
        File directoryTree = new File(directory);
        List<Future<?>> futures = new ArrayList<>();

        for (String filename : directoryTree.list()) {
            String filePath = directoryTree.toPath().resolve(filename).toString();
            File file = new File(filePath);
            if (file.isDirectory()) {
                futures.add(exploreFilesInDirectory(filePath));
            }
            else {
                futures.add(pool.submit(() -> searchString(file)));
            }
        }

        for (Future<?> future : futures) {
            if (future != null) {
                try {
                    future.get();
                }
                catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        pool.shutdown();
    }

    public Future<?> exploreFilesInDirectory(String directory) {
        Future<?> future = null;
        File directoryTree = new File(directory);

        for (String filename : directoryTree.list()) {
            String filePath = directoryTree.toPath().resolve(filename).toString();
            File file = new File(filePath);
            if (file.isDirectory()) {
                future = this.pool.submit(() -> exploreFilesInDirectory(filePath));
            }
            else if (isTextFile(file)){
                future = this.pool.submit(() -> searchString(file));
            }
        }
        return future;
    }

    private void searchString(File file) {
        try (LineNumberReader reader = new LineNumberReader(new FileReader(file))) {
            if (parser.searchWholeWords()) {
                if (parser.isCaseSensitive()) {
                    reader.lines()
                        .filter(l -> containsWholeWord(l.toLowerCase(), parser.getStringToFind().toLowerCase()))
                        .forEach(l -> getResult(file, reader.getLineNumber(), l));
                }
                else {
                    reader.lines()
                            .filter(l -> containsWholeWord(l, parser.getStringToFind()))
                            .forEach(l -> getResult(file, reader.getLineNumber(), l));
                }
            }
            else if (parser.isCaseSensitive()) {
                reader.lines()
                        .filter(l -> l.toLowerCase().contains(parser.getStringToFind().toLowerCase()))
                        .forEach(l -> getResult(file, reader.getLineNumber(), l));
            }
            else {
                reader.lines()
                        .filter(l -> l.contains(parser.getStringToFind()))
                        .forEach(l -> getResult(file, reader.getLineNumber(), l));
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private boolean isTextFile(File file) {
        return file.getName().endsWith(".txt") || file.getName().endsWith(".TXT");
    }

    private void getResult(File file, int lineNumber, String line) {
        String fileName = file.getPath().substring(parser.getPathToDirectoryTree().length() + 1);

        if (parser.getPathToOutputFile() == null) {
            System.out.println(fileName + ":" + lineNumber + ":" + line);
        }
        else {
            synchronized (this) {
                try (FileWriter writer = new FileWriter(new File(parser.getPathToOutputFile()), true)) {
                    writer.write(fileName + ":" + lineNumber + ":" + line + "\n");
                    writer.flush();
                }
                catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            }
        }
    }

    private boolean containsWholeWord(String line, String stringToFind) {
        return line.trim().equals(stringToFind) ||
                line.trim().contains(" " + stringToFind + " ") ||
                line.trim().startsWith(stringToFind + " ") ||
                line.trim().endsWith(" " + stringToFind);
    }
}
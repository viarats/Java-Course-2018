package bg.sofia.uni.fmi.mjt.grep;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        CommandParser parser = new CommandParser(sc.nextLine());
        GrepCommandExecutor executor = new GrepCommandExecutor(parser);

        while (true) {
            executor.exploreFilesInDirectoryTree(parser.getPathToDirectoryTree());
            parser = new CommandParser(sc.nextLine());
            executor = new GrepCommandExecutor(parser);
        }
    }
}
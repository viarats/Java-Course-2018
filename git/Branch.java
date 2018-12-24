package bg.sofia.uni.fmi.mjt.git;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Objects;

public class Branch {
    private String name;
    private LinkedList<Commit> commits;

    public Branch() {
        name = "master";
        commits = new LinkedList<>();
    }

    public Branch(String name, Branch other) {
        this.name = name;
        this.commits = new LinkedList<>(other.commits);
    }

    public String getName() {
        return name;
    }

    public Result commit(int count, String message) {
        commits.add(new Commit(message));
        return new Result("" + count + " files changed", true);
    }

    public Commit getHead() {
        return !commits.isEmpty() ? commits.getLast() : null;
    }

    public Result log() {
        StringBuilder message = new StringBuilder();

        Collections.reverse(commits);
        for (Commit commit : commits) {
            message.append("commit " + commit.getHash() + "\nDate: " +
                    commit.getCreatedOn() + "\n\n\t" + commit.getMessage() + "\n\n");
        }
        message.setLength(message.length() - 2);

        Collections.reverse(commits);
        return new Result(message.toString(), true);
    }

    public Result checkoutCommit(String hash) {
        for (int i = 0; i < commits.size(); i++) {
            if (commits.get(i).getHash() == hash) {
                commits.subList(i + 1, commits.size()).clear();
                return new Result("HEAD is now at " + hash, true);
            }
        }
        return new Result("commit " + hash + " does not exist", false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Branch branch = (Branch) o;
        return Objects.equals(name, branch.name) &&
                Objects.equals(commits, branch.commits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, commits);
    }
}

package bg.sofia.uni.fmi.mjt.git;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Repository {
    private Set<Branch> branches;
    private Branch currentBranch;
    private Map<String, Boolean> files;

    public Repository() {
        currentBranch = new Branch();
        branches = new TreeSet<>(new BranchComparator());
        branches.add(currentBranch);
        files = new HashMap<>();
    }

    public Result add(String... files) {
        for (String file : files) {
            if (this.files.containsKey(file)) {
                return new Result("'" + file + "' already exists", false);
            }
        }

        StringBuilder buffer = new StringBuilder();

        for (String file : files) {
            this.files.put(file, true);
            buffer.append(file + ", ");
        }

        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return new Result("added " + buffer + "to stage", true);
    }

    public Result remove(String... files) {
        for (String file : files) {
            if (!this.files.containsKey(file)) {
                return new Result("'" + file + "' did not match any files", false);
            }
        }

        StringBuilder buffer = new StringBuilder();

        for (String file : files) {
           this.files.put(file, false);
           buffer.append(file + ", ");
        }
        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return new Result("added " + buffer + "for removal", true);
    }

    public Result commit(String message) {
        if (!files.isEmpty()) {
            return currentBranch.commit(files.size(), message);
        }
        return new Result("nothing to commit, working tree clean", false);
    }

    public Commit getHead() {
        return currentBranch.getHead();
    }

    public Result log() {
        if (currentBranch.getHead() != null) {
            return currentBranch.log();
        }
        return new Result("branch " + getBranch() + " does not have any commits yet", false);
    }

    public String getBranch() {
        return currentBranch.getName();
    }

    public Result createBranch(String name) {
        for (Branch branch : branches) {
            if (branch.getName() == name) {
                return new Result("branch " + name + " already exists", false);
            }
        }
        branches.add(new Branch(name, currentBranch));
        return new Result("created branch " + name, true);
    }

    public Result checkoutBranch(String name) {
        for (Branch branch : branches) {
            if (branch.getName() == name) {
                currentBranch = branch;
                return new Result("switched to branch " + name, true);
            }
        }
        return new Result("branch " + name + " does not exist", false);
    }

    public Result checkoutCommit(String hash) {
        return currentBranch.checkoutCommit(hash);
    }
}

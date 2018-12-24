package bg.sofia.uni.fmi.mjt.git;

import java.util.Comparator;

public class BranchComparator implements Comparator<Branch> {
    public int compare(Branch fst, Branch snd) {
        return fst.getName().compareTo(snd.getName());
    }
}

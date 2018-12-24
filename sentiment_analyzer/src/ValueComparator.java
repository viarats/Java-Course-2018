package bg.sofia.uni.fmi.mjt.sentiment;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

class ValueComparator<K, V extends Comparable<V>> implements Comparator<K> {

    private Map<K, V> map;

    public ValueComparator(Map<K, V> map) {
        this.map = new HashMap<>(map);
    }

    @Override
    public int compare(K s1, K s2) {
        if (map.get(s1).compareTo(map.get(s2)) > 0) {
            return 1;
        }
        else {
            return -1;
        }
    }
}
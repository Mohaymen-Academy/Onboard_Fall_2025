import java.util.*;

public class SimpleIndex implements Index {
    private Map<String, Set<String>> index = new HashMap<>();

    @Override
    public void add(String word, String file) {
        index.putIfAbsent(word, new HashSet<>());
        index.get(word).add(file);
    }

    @Override
    public Set<String> get(String word) {
        return index.getOrDefault(word, Collections.emptySet());
    }
}

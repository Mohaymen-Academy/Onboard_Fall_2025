package index;

import java.util.*;

public class SimpleIndex implements Index {
    private Map<String, Set<String>> index = new HashMap<>();

    @Override
    public void add(String query, String contentId) {
        index.putIfAbsent(query, new HashSet<>());
        index.get(query).add(contentId);
    }


    @Override
    public Set<String> get(String query) {
        return index.getOrDefault(query, Collections.emptySet());
    }

    @Override
    public Set<String> getAllDocuments() {
        Set<String> allDocuments = new HashSet<>();
        for (Set<String> documents : index.values()) {
            allDocuments.addAll(documents);
        }
        return allDocuments;
    }
}

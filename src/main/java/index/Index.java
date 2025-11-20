package index;

import java.util.Set;

public interface Index {
    void add(String query, String contentId);
    Set<String> get(String query);
    Set<String> getAllDocuments();
}

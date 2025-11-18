package index;

import java.util.Set;

public interface Index {
    void add(String word, String file);
    Set<String> get(String word);
    Set<String> getAllDocuments();
}

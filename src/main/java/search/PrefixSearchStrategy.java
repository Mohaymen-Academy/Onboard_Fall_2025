package search;

import index.Index;

import java.util.HashSet;
import java.util.Set;

public class PrefixSearchStrategy implements SearchStrategy {

    @Override
    public Set<String> performSearch(String searchWord, Index index) {
        Set<String> result = new HashSet<>();

        for(String word : index.getAllDocuments()) {
            if(word.startsWith(searchWord)) {
                result.add(word);
            }
        }
        return result;
    }
}

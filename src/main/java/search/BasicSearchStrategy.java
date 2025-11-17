package search;

import index.Index;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicSearchStrategy implements SearchStrategy {
    @Override
    public Set<String> performSearch(String searchWord, Index index) {
        List<String> mustWords = new ArrayList<>();
        List<String> orWords = new ArrayList<>();
        List<String> notWords = new ArrayList<>();

        String[] words = searchWord.split("\\s+");
        for (String word : words) {
            if (word.startsWith("+")) {
                orWords.add(word.substring(1));
            } else if (word.startsWith("-")) {
                notWords.add(word.substring(1));
            } else {
                mustWords.add(word);
            }
        }

        Set<String> result = new HashSet<>();

        if (!mustWords.isEmpty()) {
            result = new HashSet<>(index.get(mustWords.get(0)));
            for (int i = 1; i < mustWords.size(); i++) {
                result.retainAll(index.get(mustWords.get(i)));
            }
        }

        if (!orWords.isEmpty()) {
            Set<String> orSet = new HashSet<>();
            for (String word : orWords) {
                orSet.addAll(index.get(word));
            }
            if (mustWords.isEmpty()) {
                result = orSet;
            } else {
                result.retainAll(orSet);
            }
        }

        if (orWords.isEmpty()) {
            result = new HashSet<>(index.getAllDocuments());
        }

        for (String word : notWords) {
            result.removeAll(index.get(word));
        }

        return result;
    }
}

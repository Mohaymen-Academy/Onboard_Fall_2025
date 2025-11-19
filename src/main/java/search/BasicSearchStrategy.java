package search;

import index.Index;
import normalizer.Normalizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BasicSearchStrategy implements SearchStrategy {

    private final Normalizer normalizer;

    public BasicSearchStrategy(Normalizer normalizer) {
        this.normalizer = normalizer;
    }

    @Override
    public Set<String> performSearch(String query, Index index) {
        List<String> mustWords = new ArrayList<>();
        List<String> orWords = new ArrayList<>();
        List<String> notWords = new ArrayList<>();

        String[] words = query.split("\\s+");
        for (String raw : words) {
            if (raw.isBlank()) continue;

            char first = raw.charAt(0);
            String token;
            switch (first) {
                case '+':
                    token = normalizer.normalize(raw.substring(1));
                    if (!token.isBlank()) orWords.add(token);
                    break;
                case '-':
                    token = normalizer.normalize(raw.substring(1));
                    if (!token.isBlank()) notWords.add(token);
                    break;
                default:
                    token = normalizer.normalize(raw);
                    if (!token.isBlank()) mustWords.add(token);
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

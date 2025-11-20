import lombok.Getter;
import lombok.Setter;
import normalizer.LowerCaseNormalizer;
import normalizer.Normalizer;
import tokenizer.DefaultTokenizer;
import tokenizer.Tokenizer;

import java.util.*;

@Getter
@Setter
public class SearchEngine {
    private final Tokenizer tokenizer;
    private final Normalizer normalizer;
    private final Map<String, Set<String>> index = new HashMap<>();

    public SearchEngine(Tokenizer tokenizer,
                        Normalizer normalizer) {
        this.tokenizer = Objects.requireNonNull(tokenizer);
        this.normalizer = Objects.requireNonNull(normalizer);
    }



    public void addDocument(String contentId, String content) {
        if (contentId == null || content == null) return;

        String[] tokens = tokenizer.tokenize(content);
        for (String token : tokens) {
            String normalizedToken = normalizer.normalize(token);
            if (!normalizedToken.isBlank()) {
                for (int i = 1; i <= normalizedToken.length(); i++) {
                    String prefix = normalizedToken.substring(0, i);
                    if (!prefix.isBlank()) {
                        this.addToIndex(prefix, contentId);
                    }
                }
                this.addToIndex(normalizedToken, contentId);
            }
        }
    }

    public void addDocuments(Map<String, String> documents) {
        if (documents == null) return;
        documents.forEach(this::addDocument);
    }

    private void addToIndex(String query, String contentId) {
        index.putIfAbsent(query, new HashSet<>());
        index.get(query).add(contentId);
    }

    public Set<String> getAllDocuments() {
        Set<String> allDocuments = new HashSet<>();
        for (Set<String> documents : index.values()) {
            allDocuments.addAll(documents);
        }
        return allDocuments;
    }

    public Set<String> search(String query) {
        if (query == null || query.isBlank()) {
            return Set.of();
        }
        return this.performSearch(query);
    }

    public Set<String> performSearch(String query) {
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
            result = new HashSet<>(this.getAllDocuments());
        }

        for (String word : notWords) {
            result.removeAll(index.get(word));
        }

        return result;
    }

    public static SearchEngine createDefault() {
        return new SearchEngineBuilder().build();
    }

    public static class SearchEngineBuilder {
        private Tokenizer tokenizer;
        private Normalizer normalizer;


        public SearchEngineBuilder() {
            this.tokenizer = new DefaultTokenizer();
            this.normalizer = new LowerCaseNormalizer();
        }


        public SearchEngineBuilder withTokenizer(Tokenizer tokenizer) {
            this.tokenizer = tokenizer;
            return this;
        }

        public SearchEngineBuilder withNormalizer(Normalizer normalizer) {
            this.normalizer = normalizer;
            return this;
        }


        public SearchEngine build() {
            return new SearchEngine(tokenizer, normalizer);
        }
    }

}


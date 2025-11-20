import index.Index;
import index.SimpleIndex;
import lombok.Getter;
import lombok.Setter;
import normalizer.LowerCaseNormalizer;
import normalizer.Normalizer;
import search.BasicSearchStrategy;
import search.SearchStrategy;
import tokenizer.DefaultTokenizer;
import tokenizer.Tokenizer;

import java.util.*;

@Getter
@Setter
public class SearchEngine {
    private final Index index;
    private final Tokenizer tokenizer;
    private final SearchStrategy searchStrategy;
    private final Normalizer normalizer;

    public SearchEngine(Index index,
                        Tokenizer tokenizer,
                        Normalizer normalizer,
                        SearchStrategy searchStrategy) {
        this.index = Objects.requireNonNull(index);
        this.tokenizer = Objects.requireNonNull(tokenizer);
        this.normalizer = Objects.requireNonNull(normalizer);
        this.searchStrategy = Objects.requireNonNull(searchStrategy);
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
                        index.add(prefix, contentId);
                    }
                }
                index.add(normalizedToken, contentId);
            }
        }
    }

    public void addDocuments(Map<String, String> documents) {
        if (documents == null) return;
        documents.forEach(this::addDocument);
    }

    public Set<String> search(String query) {
        if (query == null || query.isBlank()) {
            return Set.of();
        }
        return searchStrategy.performSearch(query, index);
    }

    public static SearchEngine createDefault() {
        return new SearchEngineBuilder().build();
    }

    public static class SearchEngineBuilder {
        private Index index;
        private Tokenizer tokenizer;
        private Normalizer normalizer;
        private SearchStrategy searchStrategy;


        public SearchEngineBuilder() {
            this.index = new SimpleIndex();
            this.tokenizer = new DefaultTokenizer();
            this.normalizer = new LowerCaseNormalizer();
            this.searchStrategy = new BasicSearchStrategy(normalizer);
        }

        public SearchEngineBuilder withIndex(Index index) {
            this.index = index;
            return this;
        }

        public SearchEngineBuilder withTokenizer(Tokenizer tokenizer) {
            this.tokenizer = tokenizer;
            return this;
        }

        public SearchEngineBuilder withNormalizer(Normalizer normalizer) {
            this.normalizer = normalizer;
            return this;
        }

        public SearchEngineBuilder withSearchStrategy(SearchStrategy searchStrategy) {
            this.searchStrategy = searchStrategy;
            return this;
        }

        public SearchEngine build() {
            return new SearchEngine(index, tokenizer, normalizer, searchStrategy);
        }
    }

}


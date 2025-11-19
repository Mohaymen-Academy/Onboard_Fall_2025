import index.Index;
import lombok.Getter;
import lombok.Setter;
import normalizer.Normalizer;
import search.SearchStrategy;
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

    public void addDocument(String id, String content) {
        if (id == null || content == null) return;

        String[] tokens = tokenizer.tokenize(content);
        for (String token : tokens) {
            String normalized = normalizer.normalize(token);
            if (!normalized.isBlank()) {
                index.add(normalized, id);
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

    public static SearchEngine createDefault(Index index,
                                             Tokenizer tokenizer,
                                             Normalizer normalizer) {
        return new SearchEngine(
                index,
                tokenizer,
                normalizer,
                new search.BasicSearchStrategy(normalizer)
        );
    }
}

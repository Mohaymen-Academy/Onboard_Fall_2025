import normalizer.LowerCaseNormalizer;
import normalizer.Normalizer;
import search.BasicSearchStrategy;
import search.SearchStrategy;
import tokenizer.DefaultTokenizer;
import tokenizer.Tokenizer;
import index.Index;
import index.SimpleIndex;

public class SearchEngineConfig {
    public static SearchEngine createSearchEngine() {
        Normalizer normalizer = new LowerCaseNormalizer();
        Tokenizer tokenizer = new DefaultTokenizer();
        Index invertedIndex = new SimpleIndex();
        SearchStrategy strategy = new BasicSearchStrategy(normalizer);
        return new SearchEngine(invertedIndex, tokenizer, normalizer, strategy);
    }
}

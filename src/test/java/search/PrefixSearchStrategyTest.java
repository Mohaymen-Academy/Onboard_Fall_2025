package search;
import static org.junit.jupiter.api.Assertions.*;

import index.Index;
import index.SimpleIndex;
import org.junit.jupiter.api.Test;
import tokenizer.DefaultTokenizer;

import java.util.Set;

public class PrefixSearchStrategyTest {

    @Test
    public void testPrefixSearch() {
        Index index = new SimpleIndex();
        SearchStrategy strategy = new PrefixSearchStrategy();
        SearchEngine searchEngine = new SearchEngine(index, new DefaultTokenizer(), strategy);

        index.add("mohammad", "user1.txt");
        index.add("mohammad", "user2.txt");
        index.add("ahmad", "user3.txt");
        index.add("ali", "user4.txt");

        Set<String> result = searchEngine.search("mo");

        assertTrue(result.contains("user1.txt"));
        assertTrue(result.contains("user2.txt"));
        assertFalse(result.contains("user3.txt"));
        assertFalse(result.contains("user4.txt"));
    }
}


package search;

import index.Index;
import index.SimpleIndex;
import org.junit.Before;

import java.util.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicSearchStrategyTest {

    private Index index;
    private SearchStrategy searchStrategy;

    @Before
    public void setUp() {
        index = new SimpleIndex();
        searchStrategy = new BasicSearchStrategy();

        index.add("java", "a.txt");
        index.add("java", "b.txt");
        index.add("spring", "b.txt");
        index.add("docker", "c.txt");
        index.add("kubernetes", "d.txt");
    }

    @Test
    public void mustWordsOnlyShouldIntersectDocuments() {
        Set<String> result = searchStrategy.performSearch("java spring", index);
        assertEquals(Set.of("b.txt"), result);
    }

    @Test
    public void orWordsOnlyShouldReturnUnion() {
        Set<String> result = searchStrategy.performSearch("+java +docker", index);
        assertEquals(Set.of("a.txt", "b.txt", "c.txt"), result);
    }

    @Test
    public void mustAndOrCombinationShouldIntersect() {
        Set<String> result = searchStrategy.performSearch("java +docker", index);
        assertTrue(result.isEmpty());
    }

    @Test
    public void emptyQueryShouldReturnAllDocuments() {
        Set<String> result = searchStrategy.performSearch("", index);
        assertEquals(Set.of("a.txt", "b.txt", "c.txt", "d.txt"), result);
    }

}
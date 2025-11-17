import java.util.Set;

public interface SearchStrategy {
    Set<String> performSearch(String searchWord, Index index);
}

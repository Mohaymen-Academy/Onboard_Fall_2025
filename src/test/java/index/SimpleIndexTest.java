package java.index;

import index.Index;
import index.SimpleIndex;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleIndexTest {

    @Test
    void addAndGetShouldStoreWordsPerFile(){
        Index index = new SimpleIndex();

        index.add("java", "file1.txt");
        index.add("java", "file2.txt");
        index.add("python", "file2.txt");

        Set<String> javaFiles = index.get("java");
        Set<String> pythonFiles = index.get("python");
        Set<String> unknownFiles = index.get("unknown");

        assertEquals(Set.of("file1.txt", "file2.txt"), javaFiles);
        assertEquals(Set.of("file2.txt"), pythonFiles);
        assertTrue(unknownFiles.isEmpty());
    }

    @Test
    void getAllDocumentsShouldReturnAllDistinctFiles(){
        Index index = new SimpleIndex();

        index.add("java", "file1.txt");
        index.add("java", "file2.txt");
        index.add("python", "file3.txt");

        Set<String> allFiles = index.getAllDocuments();

        assertEquals(Set.of("file1.txt", "file2.txt", "file3.txt"), allFiles);
    }
}

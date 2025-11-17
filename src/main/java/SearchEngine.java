import file.FileReader;
import index.Index;
import search.SearchStrategy;
import tokenizer.Tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SearchEngine {
    private final Index index;
    private final Tokenizer tokenizer;
    private final SearchStrategy searchStrategy;

    public SearchEngine(Index index, Tokenizer tokenizer, SearchStrategy searchStrategy) {
        this.index = index;
        this.tokenizer = tokenizer;
        this.searchStrategy = searchStrategy;
    }

    public void processFiles(String folderPath) throws IOException {
        Files.list(Paths.get(folderPath))
                .filter(path -> path.toString().endsWith(".txt"))
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        List<String> lines = new FileReader().readLines(path.toString());
                        for (String line : lines) {
                            String[] words = tokenizer.tokenize(line);
                            for (String word : words) {
                                word = word.toLowerCase();
                                index.add(word, path.getFileName().toString());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public Set<String> search(String searchWord) {
        return searchStrategy.performSearch(searchWord, index);
    }
}

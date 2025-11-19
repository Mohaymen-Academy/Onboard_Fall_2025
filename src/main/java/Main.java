import index.Index;
import index.SimpleIndex;
import normalizer.LowerCaseNormalizer;
import normalizer.Normalizer;
import search.BasicSearchStrategy;
import search.SearchStrategy;
import tokenizer.DefaultTokenizer;
import tokenizer.Tokenizer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String folderPath = "/Users/mohammadhosseinsurani/Downloads/SoftwareBooksDataset-2cd9f22c39e9982e287ed4b473f78878";
        Normalizer normalizer = new LowerCaseNormalizer();
        Tokenizer tokenizer = new DefaultTokenizer();
        Index invertedIndex = new SimpleIndex();
        SearchStrategy strategy = new BasicSearchStrategy(normalizer);
        SearchEngine searchEngine = new SearchEngine(invertedIndex, tokenizer, normalizer, strategy);

        try {
            Files.list(Paths.get(folderPath))
                    .filter(path -> path.toString().endsWith(".txt"))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            List<String> lines = Files.readAllLines(path);
                            for (String line : lines) {
                                String[] words = tokenizer.tokenize(line);
                                for (String word : words) {
                                    word = normalizer.normalize(word);
                                    if (!word.isBlank()) {
                                        invertedIndex.add(word, path.getFileName().toString());
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter word to search: ");
            String searchWord = scanner.nextLine().toLowerCase();

            Set<String> result = searchEngine.search(searchWord);

            if (result.isEmpty()) {
                System.out.println("No document with these words.");
            } else {
                result.forEach(System.out::println);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

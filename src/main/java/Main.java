import index.Index;
import index.SimpleIndex;
import search.BasicSearchStrategy;
import search.SearchStrategy;
import tokenizer.DefaultTokenizer;
import tokenizer.Tokenizer;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String folderPath = "C:\\Users\\alire\\Documents\\Mohaymen";
        Index invertedIndex = new SimpleIndex();
        Tokenizer tokenizer = new DefaultTokenizer();
        SearchStrategy strategy = new BasicSearchStrategy();
        SearchEngine searchEngine = new SearchEngine(invertedIndex, tokenizer, strategy);

        try {
            searchEngine.processFiles(folderPath);

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

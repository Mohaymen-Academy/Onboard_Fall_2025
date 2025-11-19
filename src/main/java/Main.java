import index.Index;
import index.SimpleIndex;
import search.BasicSearchStrategy;
import search.SearchStrategy;
import tokenizer.DefaultTokenizer;
import tokenizer.Tokenizer;

import java.io.IOException;
import java.util.*;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String folderPath = "/Users/mohammadhosseinsurani/Downloads/SoftwareBooksDataset-2cd9f22c39e9982e287ed4b473f78878";
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

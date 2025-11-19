import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String folderPath = "/Users/mohammadhosseinsurani/Downloads/SoftwareBooksDataset-2cd9f22c39e9982e287ed4b473f78878";
        SearchEngine searchEngine = SearchEngineConfig.createSearchEngine();

        try {
            Files.list(Paths.get(folderPath))
                    .filter(path -> path.toString().endsWith(".txt"))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            List<String> lines = Files.readAllLines(path);
                            for (String line : lines) {
                                String[] words = searchEngine.getTokenizer().tokenize(line);
                                for (String word : words) {
                                    word = searchEngine.getNormalizer().normalize(word);
                                    if (!word.isBlank()) {
                                        searchEngine.getIndex().add(word, path.getFileName().toString());
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

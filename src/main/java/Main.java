import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String folderPath = "C:\\Users\\alire\\Documents\\Mohaymen";
        SearchEngine searchEngine = SearchEngine.createDefault();

        try {
            Files.list(Paths.get(folderPath))
                    .filter(path -> path.toString().endsWith(".txt"))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            List<String> contents = Files.readAllLines(path);
                            for (String content : contents) {
                                searchEngine.addDocument(path.getFileName().toString(), content);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter word to search: ");
            String query = scanner.nextLine().toLowerCase();

            Set<String> result = searchEngine.search(query);

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

import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String folderPath = "/Users/mohammadhosseinsurani/Downloads/SoftwareBooksDataset-2cd9f22c39e9982e287ed4b473f78878";
        Map<String, Set<String>> invertedIndex = new HashMap<>();
        Set<String> allFiles = new HashSet<>();

        try {
            processFiles(folderPath, invertedIndex, allFiles);

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter word to search: ");
            String searchWord = scanner.nextLine().toLowerCase();

            String[] words = searchWord.split("\\s+");
            List<String> mustWords = new ArrayList<>();
            List<String> orWords = new ArrayList<>();
            List<String> notWords = new ArrayList<>();
            processSearchWords(words, mustWords, orWords, notWords);

            Set<String> result = performSearch(mustWords, orWords, notWords, invertedIndex, allFiles);

            if (result.isEmpty()) {
                System.out.println("No document with these words.");
            } else {
                result.forEach(System.out::println);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFiles(String folderPath, Map<String, Set<String>> invertedIndex, Set<String> allFiles) throws IOException {
        Files.list(Paths.get(folderPath))
                .filter(path -> path.toString().endsWith(".txt"))
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    allFiles.add(path.getFileName().toString());
                    try {
                        List<String> lines = Files.readAllLines(path);
                        for (String line : lines) {
                            String[] words = line.split("\\W+");
                            for (String word : words) {
                                word = word.toLowerCase();
                                invertedIndex.putIfAbsent(word, new HashSet<>());
                                invertedIndex.get(word).add(path.getFileName().toString());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void processSearchWords(String[] words, List<String> mustWords, List<String> orWords, List<String> notWords) {
        for (String word : words) {
            if (word.startsWith("+")) {
                orWords.add(word.substring(1));
            } else if (word.startsWith("-")) {
                notWords.add(word.substring(1));
            } else {
                mustWords.add(word);
            }
        }
    }

    private static Set<String> performSearch(List<String> mustWords, List<String> orWords, List<String> notWords,
                                             Map<String, Set<String>> invertedIndex, Set<String> allFiles) {
        Set<String> result = new HashSet<>();

        if (!mustWords.isEmpty()) {
            result = new HashSet<>(invertedIndex.getOrDefault(mustWords.get(0), Set.of()));
            for (int i = 1; i < mustWords.size(); i++) {
                result.retainAll(invertedIndex.getOrDefault(mustWords.get(i), Set.of()));
            }
        }

        if (!orWords.isEmpty()) {
            Set<String> orSet = new HashSet<>();
            for (String word : orWords) {
                orSet.addAll(invertedIndex.getOrDefault(word, Set.of()));
            }
            if (mustWords.isEmpty()) {
                result = orSet;
            } else {
                result.retainAll(orSet);
            }
        }

        if (orWords.isEmpty()) {
            result = new HashSet<>(allFiles);
        }

        for (String word : notWords) {
            result.removeAll(invertedIndex.getOrDefault(word, Set.of()));
        }

        return result;
    }
}

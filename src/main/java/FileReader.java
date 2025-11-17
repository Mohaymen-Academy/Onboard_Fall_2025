import java.nio.file.*;
import java.io.IOException;
import java.util.List;

public class FileReader {
    public List<String> readLines(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
}

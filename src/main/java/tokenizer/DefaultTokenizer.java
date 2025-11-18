package tokenizer;

public class DefaultTokenizer implements Tokenizer {
    @Override
    public String[] tokenize(String line) {
        return line.split("\\W+");
    }
}

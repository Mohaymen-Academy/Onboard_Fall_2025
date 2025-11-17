public class CustomTokenizer implements Tokenizer {
    @Override
    public String[] tokenize(String line) {
        return line.split("[,\\.\\s]+");
    }
}

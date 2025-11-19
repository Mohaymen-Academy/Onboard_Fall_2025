package normalizer;

public class LowerCaseNormalizer implements Normalizer {
    @Override
    public String normalize(String token) {
        if (token == null) {
            return "";
        }
        return token.toLowerCase().trim();
    }
}

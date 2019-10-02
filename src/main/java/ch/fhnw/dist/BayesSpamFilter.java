package ch.fhnw.dist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;

public class BayesSpamFilter {
    private final Map<String, Double> corpus;

    public BayesSpamFilter(Map<String, Double> corpus) {
        this.corpus = corpus;
    }

    static BayesSpamFilter load(Path corpusFile) throws IOException {
        //TODO maybe add a better split character
        var corpus = Files.readAllLines(corpusFile).stream().map(s -> s.split("=")).collect(Collectors.toMap(strings -> strings[0], strings -> Double.valueOf(strings[1])));
        return new BayesSpamFilter(corpus);
    }

    boolean isSpam(String text) {
        return scoreText(text) > 0.5;
    }

    double scoreText(String text) {
        //TODO implement scoring algorithm
        return 0.5;
    }
}

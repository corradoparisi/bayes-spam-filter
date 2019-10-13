package ch.fhnw.dist;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import static ch.fhnw.dist.BayesSpamFilterTrain.WHITESPACE;

class BayesSpamFilter {
    private final Map<String, Double> corpus;

    BayesSpamFilter(Map<String, Double> corpus) {
        this.corpus = corpus;
    }

    boolean isSpam(String text) {
        return scoreText(text) > BayesSpamFilterTrain.SPAM_RECOGNITION_THRESHOLD;
    }

    // first wikipedia formula
    private double scoreText(String s) {

        var scores = Arrays.stream(s.toLowerCase().split(WHITESPACE))
                .parallel()
                .map(corpus::get)
                .filter(Objects::nonNull)
                .mapToDouble(i -> (1 - i) / i).reduce(1, (a, b) -> a * b);
        return 1 / (1 + scores);
    }
}

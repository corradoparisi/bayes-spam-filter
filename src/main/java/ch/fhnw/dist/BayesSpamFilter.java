package ch.fhnw.dist;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
        List<Double> scores = Arrays.stream(s.toLowerCase().split(WHITESPACE))
                .parallel()
                .map(corpus::get)
                .filter(Objects::nonNull).collect(Collectors.toList());
        double reduce = scores.stream().mapToDouble(i -> i).reduce(1, (a, b) -> a * b);
        double reduce2 = scores.stream().mapToDouble(i -> 1 - i).reduce(1, (a, b) -> a * b);
        return reduce / (reduce + reduce2);
    }
}

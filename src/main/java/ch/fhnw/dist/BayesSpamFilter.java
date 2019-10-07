package ch.fhnw.dist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static ch.fhnw.dist.BayesSpamFilterTrain.cleanStr;

class BayesSpamFilter {
    private final Map<String, Double> corpus;

    public BayesSpamFilter(Map<String, Double> corpus) {
        this.corpus = corpus;
    }

    static BayesSpamFilter load(Path corpusFile) throws IOException {
        //TODO maybe add a better split character
        var corpus = Files.readAllLines(corpusFile).stream().map(s -> {
            String splitChar = "=";
            return s.split(splitChar);
        }).collect(Collectors.toMap(strings -> strings[0], strings -> Double.valueOf(strings[1])));
        return new BayesSpamFilter(corpus);
    }

    boolean isSpam(String text) {
        return scoreText(text) > 0.5;
    }

    double scoreText(String s) {
        //TODO not sure if this is correct?
        List<Double> scores = Arrays.stream(cleanStr(s).split("\\s+"))
                .parallel()
                .map(corpus::get)
                .filter(Objects::nonNull).collect(Collectors.toList());
        double reduce = scores.stream().mapToDouble(i -> i).reduce(1, (a, b) -> a * b);
        double reduce2 = scores.stream().mapToDouble(i -> 1 - i).reduce(1, (a, b) -> a * b);
        return reduce2 / reduce;
    }
}

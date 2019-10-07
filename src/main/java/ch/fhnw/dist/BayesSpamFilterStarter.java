package ch.fhnw.dist;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class BayesSpamFilterStarter {
    private static final Path data = Paths.get("data");

    public static void main(String[] args) throws Exception {
        Map<String, Double> train = BayesSpamFilterTrain.train(data);
        BayesSpamFilterTrain.validate(data.resolve("ham-anlern.zip"),data.resolve("spam-anlern.zip"))
    }
}

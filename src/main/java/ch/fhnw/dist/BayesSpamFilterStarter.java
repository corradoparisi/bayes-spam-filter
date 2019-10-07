package ch.fhnw.dist;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BayesSpamFilterStarter {
    private static final Path data = Paths.get("data");
    private static String hamZipTrainData = "ham-anlern.zip";
    private static String spamZipTrainData = "spam-anlern.zip";

    public static void main(String[] args) throws Exception {
        BayesSpamFilter bayesSpamFilter = BayesSpamFilterTrain.train(data.resolve(hamZipTrainData), data.resolve(spamZipTrainData));
        BayesSpamFilterTrain.validate(bayesSpamFilter, data.resolve(hamZipTrainData),data.resolve(spamZipTrainData));
    }
}

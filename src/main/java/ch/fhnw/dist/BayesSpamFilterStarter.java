package ch.fhnw.dist;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BayesSpamFilterStarter {
    private static final Path data = Paths.get("data");

    private static final Path hamZipTrainData = data.resolve("ham-anlern.zip");
    private static final Path hamCallibration = data.resolve("ham-kallibrierung.zip");
    private static final Path hamTest = data.resolve("ham-test.zip");

    private static final Path spamZipTrainData = data.resolve("spam-anlern.zip");
    private static final Path spamCallibration = data.resolve("spam-kallibrierung.zip");
    private static final Path spamTest = data.resolve("spam-test.zip");

    public static void main(String[] args) throws Exception {
        BayesSpamFilter bayesSpamFilter = BayesSpamFilterTrain.train(hamZipTrainData,spamZipTrainData);
        BayesSpamFilterTrain.printValidationStatistics(bayesSpamFilter, hamZipTrainData, spamZipTrainData);

        BayesSpamFilterTrain.printValidationStatistics(bayesSpamFilter, hamCallibration, spamCallibration);
        BayesSpamFilterTrain.printValidationStatistics(bayesSpamFilter, hamTest, spamTest);
    }
}

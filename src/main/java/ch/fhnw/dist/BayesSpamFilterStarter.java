package ch.fhnw.dist;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BayesSpamFilterStarter {
    private static final Path data = Paths.get("data");

    private static final Path hamZipTrainData = data.resolve("ham-anlern.zip");
    private static final Path hamCalibration = data.resolve("ham-kallibrierung.zip");
    private static final Path hamTest = data.resolve("ham-test.zip");

    private static final Path spamZipTrainData = data.resolve("spam-anlern.zip");
    private static final Path spamCalibration = data.resolve("spam-kallibrierung.zip");
    private static final Path spamTest = data.resolve("spam-test.zip");

    public static void main(String[] args) throws Exception {
        BayesSpamFilterTrain.DEBUG_CORPUS = false;
        BayesSpamFilterTrain.PR_S = 0.5;
        BayesSpamFilter.THRESHOLD = 0.90;
        System.out.println("BayesSpamFilterTrain.PR_S = " + BayesSpamFilterTrain.PR_S);
        System.out.println("BayesSpamFilter.THRESHOLD = " + BayesSpamFilter.THRESHOLD);
        BayesSpamFilter bayesSpamFilter = BayesSpamFilterTrain.train(hamZipTrainData, spamZipTrainData);
        System.out.println("Training Set: ");
        BayesSpamFilterTrain.printValidationStatistics(bayesSpamFilter, hamZipTrainData, spamZipTrainData);
        System.out.println();
        System.out.println("Validation Set");
        BayesSpamFilterTrain.printValidationStatistics(bayesSpamFilter, hamCalibration, spamCalibration);
        System.out.println();
        System.out.println("Test Set");
        BayesSpamFilterTrain.printValidationStatistics(bayesSpamFilter, hamTest, spamTest);
    }
}

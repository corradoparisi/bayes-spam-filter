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
        // set to true, for more detailed console output
        BayesSpamFilterTrain.DebugHelper.isDebug = false;

        // for these 2 variables a higher value means a greater probability of being a *spam* document
        // and a lower value the returned probability of the algorithm (value between 0 and 1)  of being a *ham* document.
        BayesSpamFilterTrain.DebugHelper.spamFilterRecognitionValue = 0.7;
        BayesSpamFilterTrain.DebugHelper.hamFilterRecognitionValue = 0.4;

        // hypothesis that there are as many spam than ham documents overall
        // even though there are way more spam than ham documents, it is recommended
        // to leave this value as is, since it would be worse to categorise accidentally
        // a ham document as a spam.
        BayesSpamFilterTrain.PR_S = 0.5;
        BayesSpamFilterTrain.PR_H = 1.0 - BayesSpamFilterTrain.PR_S;

        BayesSpamFilterTrain.SPAM_RECOGNITION_THRESHOLD = 0.9;

        System.out.println("BayesSpamFilterTrain.PR_S = " + BayesSpamFilterTrain.PR_S);
        System.out.println("BayesSpamFilter.THRESHOLD = " + BayesSpamFilterTrain.SPAM_RECOGNITION_THRESHOLD);
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

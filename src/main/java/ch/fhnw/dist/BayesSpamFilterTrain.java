package ch.fhnw.dist;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

class BayesSpamFilterTrain {

    static final String WHITESPACE = "\\s+";
    static Double PR_S = 0.5;
    static Double SPAM_RECOGNITION_THRESHOLD = 0.5;
    static Double PR_H = 0.5;

    static BayesSpamFilter train(Path hamPath, Path spamPath) throws Exception {
        var ham = documentProbability(hamPath);
        var spam = documentProbability(spamPath);
        var keySet = ham.keySet();
        keySet = new HashSet<>(keySet);
        keySet.addAll(spam.keySet());
        var collect = keySet.parallelStream().map(key -> {
            var Pr_WH = get(key, ham);
            var Pr_WS = get(key, spam);
            return new Tuple2<>(key, Pr_WS * PR_S / (Pr_WS * PR_S + Pr_WH * PR_H));
        }).collect(Collectors.toMap(t -> t.t1, t -> t.t2));
        if (DebugHelper.isDebug) {
            collect.entrySet().stream()
                    .filter(stringDoubleEntry -> stringDoubleEntry.getValue() > DebugHelper.spamFilterRecognitionValue || stringDoubleEntry.getValue() < DebugHelper.hamFilterRecognitionValue)
                    .forEach((keyValue) -> System.out.println(keyValue.getKey() + keyValue.getValue()));
        }

        return new BayesSpamFilter(collect);
    }

    static void printValidationStatistics(BayesSpamFilter bayesSpamFilter, Path ham, Path spam) throws Exception {
        var hamCount = validate(bayesSpamFilter, ham);
        var spamCount = validate(bayesSpamFilter, spam);
        var truePositives = spamCount.t2; // is spam, classified correctly
        var falsePositives = hamCount.t2; // no spam, classified wrongly

        var trueNegatives = hamCount.t1 - hamCount.t2; // no spam, classified correctly
        var falseNegatives = spamCount.t1 - spamCount.t2; // is spam, classified wrongly

        var precision = truePositives.doubleValue() / (truePositives + falsePositives);
        var recall = truePositives.doubleValue() / (truePositives + falseNegatives);
        var accuracy = (truePositives.doubleValue() + trueNegatives) / (truePositives.doubleValue() + trueNegatives + falseNegatives + falsePositives);
        System.out.println("Document count " + hamCount);
        System.out.println("Spam count " + spamCount);

        System.out.println("True positives " + truePositives);
        System.out.println("False negatives " + falseNegatives);
        System.out.println("False positives " + falsePositives);
        System.out.println("True negatives " + trueNegatives);

        // see the following link for
        // an explanation of precision and recall: https://en.wikipedia.org/wiki/Precision_and_recall
        System.out.println("Precision " + precision);
        System.out.println("Recall " + recall);
        System.out.println("Accuracy " + accuracy);
    }

    private static double get(String key, Map<String, Double> ham) {
        return Math.max(0.000_000_1, ham.getOrDefault(key, 0.000_000_1));
    }

    private static long count(Path zip) throws Exception {
        return StreamSupport.stream(FileSystems.newFileSystem(zip, ClassLoader.getPlatformClassLoader())
                .getRootDirectories()
                .spliterator(), true)
                .flatMap(root -> ExOptional.orElse(() -> Files.walk(root), Stream::empty))
                .count();
    }

    private static Map<String, Long> documentFrequency(Path zip) throws Exception {
        return StreamSupport.stream(FileSystems.newFileSystem(zip, ClassLoader.getPlatformClassLoader())
                .getRootDirectories()
                .spliterator(), true)
                .flatMap(root -> ExOptional.orElse(() -> Files.walk(root), Stream::empty))
                .map(path -> ExOptional.orElse(() -> Files.readString(path), () -> ""))
                .map(String::toLowerCase)
                // we could use a string hash instead of regex split of whitespace (performance improvement)
                .map(s -> s.split(WHITESPACE))
                .flatMap(strings -> Arrays.stream(strings).distinct())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private static Map<String, Double> documentProbability(Path zip) throws Exception {
        var count = count(zip);
        return documentFrequency(zip)
                .entrySet()
                .parallelStream()
                .map(stringLongEntry -> new Tuple2<>(stringLongEntry.getKey(), (stringLongEntry.getValue().doubleValue() / count)))
                .collect(Collectors.toMap(t -> t.t1, t -> t.t2));
    }

    /**
     * Returns a Tuple2 where t1 is the documentCount and t2 the spamCount.
     */
    private static Tuple2<Long, Long> validate(BayesSpamFilter bayesSpamFilter, Path zip) throws Exception {
        var documentCount = count(zip);
        var spamCount = StreamSupport.stream(FileSystems.newFileSystem(zip, ClassLoader.getPlatformClassLoader())
                .getRootDirectories()
                .spliterator(), true)
                .flatMap(root -> ExOptional.orElse(() -> Files.walk(root), Stream::empty))
                .map(path -> ExOptional.orElse(() -> Files.readString(path), () -> ""))
                .filter(bayesSpamFilter::isSpam)
                .count();
        return new Tuple2<>(documentCount, spamCount);
    }

    static class DebugHelper {
        static boolean isDebug = false;
        static double spamFilterRecognitionValue = 0.55;
        static double hamFilterRecognitionValue = 0.45;
    }
}

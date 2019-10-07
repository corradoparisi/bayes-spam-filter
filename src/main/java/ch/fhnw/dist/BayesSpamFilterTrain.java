package ch.fhnw.dist;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BayesSpamFilterTrain {
    private static final Double PR_S = 0.5;
    private static final Double PR_H = 1.0 - PR_S;

    static String cleanStr(String s) {
//        return Normalizer
//                .normalize((s.toLowerCase()), Normalizer.Form.NFD)
//                .replaceAll("[^\\p{ASCII}]", "")
//                .replaceAll("[^\\dA-Za-z ]", "");
        return s.toLowerCase();
    }

    public static BayesSpamFilter train(Path hamPath, Path spamPath) throws Exception {
        var ham = documentProbability(hamPath);
        var spam = documentProbability(spamPath);
        var keySet = ham.keySet();
        keySet.addAll(spam.keySet());
        return new BayesSpamFilter(
                keySet.parallelStream().map(key -> {
                    var hamD = get(key, ham) * PR_H;
                    var spamD = get(key, spam) * PR_S;
                    return new Tuple2<>(key, spamD / (spamD * hamD));
                }).collect(Collectors.toMap(t -> t.t1, t -> t.t2))
        );
    }

    private static double get(String key, Map<String, Double> ham) {
        return Math.max(0.01, ham.getOrDefault(key, 0.0));
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
                .map(BayesSpamFilterTrain::cleanStr)
                //TODO maybe use a hash for perfomance etc.
                .map(s -> s.split("\\s+"))
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

    public static Tuple2<Double, Double> validate(BayesSpamFilter bayesSpamFilter, Path ham, Path spam) throws Exception {
        double documentCount = validate(bayesSpamFilter, ham);
        double spamCount = validate(bayesSpamFilter, spam);
        System.out.println("Document count " + documentCount);
        System.out.println("Spam count " + spamCount);
        return new Tuple2<>(documentCount, spamCount);
    }

    // TODO document
    private static double validate(BayesSpamFilter bayesSpamFilter, Path zip) throws Exception {
        double documentCount = count(zip);
        double spamCount = StreamSupport.stream(FileSystems.newFileSystem(zip, ClassLoader.getPlatformClassLoader())
                .getRootDirectories()
                .spliterator(), true)
                .flatMap(root -> ExOptional.orElse(() -> Files.walk(root), Stream::empty))
                .map(path -> ExOptional.orElse(() -> Files.readString(path), () -> ""))
                .filter(bayesSpamFilter::isSpam)
                .count();
        return spamCount / documentCount;
    }
}

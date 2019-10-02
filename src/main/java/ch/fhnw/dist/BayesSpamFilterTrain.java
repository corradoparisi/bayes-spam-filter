package ch.fhnw.dist;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BayesSpamFilterTrain {

    static String cleanStr(String s) {
        return Normalizer
                .normalize((s.toLowerCase()), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^\\dA-Za-z ]", "");
    }

    public static void main(String[] args) throws Exception {
        train();
    }

    public static void train() throws Exception {
        //TODO generate corpus and write to file
        //TODO log precission and recall
        //TODO maybe use murmur hash or something similar to hash the words to increase performance etc.
        documentFrequency(Paths.get("../ham-anlern.zip")).forEach((s, aLong) -> System.out.println(s + " : " + aLong));
        documentFrequency(Paths.get("../spam-anlern.zip")).forEach((s, aLong) -> System.out.println(s + " : " + aLong));
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
                .map(s -> s.split("\\s+"))
                .flatMap(strings -> Arrays.stream(strings).distinct())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private static Map<String, Double> documentProbability(Path zip) throws Exception {
        var count = count(zip);
        return documentFrequency(zip)
                .entrySet()
                .parallelStream()
                .map(stringLongEntry -> new Tuple<>(stringLongEntry.getKey(), (stringLongEntry.getValue().doubleValue() / count)))
                .collect(Collectors.toMap(stringLongTuple -> stringLongTuple.a, stringLongTuple -> stringLongTuple.b));
    }

}

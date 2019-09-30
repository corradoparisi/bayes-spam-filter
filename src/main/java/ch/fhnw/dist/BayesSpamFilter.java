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

public class BayesSpamFilter {
    private static final boolean parallel = true;

    static String cleanStr(String s) {
        return Normalizer
                .normalize((s.toLowerCase()), Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^\\dA-Za-z ]", "");
    }

    public static void main(String[] args) throws Exception {
        documentFrequency(Paths.get("../ham-anlern.zip")).forEach((s, aLong) -> System.out.println(s + " : " + aLong));
        documentFrequency(Paths.get("../spam-anlern.zip")).forEach((s, aLong) -> System.out.println(s + " : " + aLong));
    }

    static Map<String, Long> documentFrequency(Path zip) throws Exception {
        return StreamSupport.stream(FileSystems.newFileSystem(zip, ClassLoader.getPlatformClassLoader())
                .getRootDirectories()
                .spliterator(), parallel)
                .flatMap(root -> ExOptional.orElse(() -> Files.walk(root), Stream::empty))
                .map(path -> ExOptional.orElse(() -> Files.readString(path), () -> ""))
                .map(BayesSpamFilter::cleanStr)
                .map(s -> s.split(" "))
                .flatMap(strings -> Arrays.stream(strings).distinct())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}

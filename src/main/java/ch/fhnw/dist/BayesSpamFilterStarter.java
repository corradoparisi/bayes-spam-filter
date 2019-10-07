package ch.fhnw.dist;

import java.nio.file.Path;
import java.nio.file.Paths;

public class BayesSpamFilterStarter {
    private static final Path data = Paths.get("data");

    public static void main(String[] args) {
        Path sad = data.resolve("sad");
    }
}

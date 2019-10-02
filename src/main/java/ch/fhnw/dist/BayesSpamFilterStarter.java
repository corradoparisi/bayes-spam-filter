package ch.fhnw.dist;

import java.util.Arrays;

public class BayesSpamFilterStarter {
    public static void main(String[] args) {
        if (Arrays.stream(args).anyMatch(s -> s.contains("train"))) {

        } else if (Arrays.stream(args).anyMatch(s -> s.contains("validate"))) {

        } else {
            System.out.println("use one of -train -validate");
        }
    }
}

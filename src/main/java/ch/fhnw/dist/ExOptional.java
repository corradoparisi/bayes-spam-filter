package ch.fhnw.dist;

import java.util.Optional;
import java.util.function.Supplier;

public class ExOptional {
    public static <T> Optional<T> of(ExSupplier<T> tSupplier) {
        try {
            return Optional.of(tSupplier.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> T orElse(ExSupplier<T> tSupplier, Supplier<T> orElse) {
        return of(tSupplier).orElseGet(orElse);
    }


    @FunctionalInterface
    public interface ExSupplier<T> {
        T get() throws Exception;
    }
}

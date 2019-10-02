package ch.fhnw.dist;

/**
 * java does not have tuples
 */
public class Tuple<A, B> {
    public final A a;
    public final B b;

    public Tuple(A a, B b) {
        this.a = a;
        this.b = b;
    }
}

package org.example;
import java.math.BigInteger;
import java.security.SecureRandom;
public class TestGenerator {
    private static final SecureRandom rnd = new SecureRandom();

    public static BigInteger generatePrime(int bitLength) {
        return BigInteger.probablePrime(bitLength, rnd);
    }

    public static BigInteger generateE(BigInteger phi) {
        BigInteger e;
        do {
            e = new BigInteger(phi.bitLength(), rnd);
        } while (!e.gcd(phi).equals(BigInteger.ONE) || e.compareTo(phi) >= 0);
        return e;
    }
}

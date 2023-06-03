import javafx.scene.control.TextArea;
import org.example.RSADemo;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSATest {
    @Test
    public void testEncryptionAndDecryption() {
        int bitLength = 1024;
        SecureRandom rnd = new SecureRandom();

        BigInteger p = BigInteger.probablePrime(bitLength, rnd);
        BigInteger q = BigInteger.probablePrime(bitLength, rnd);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        BigInteger e;
        do {
            e = new BigInteger(phi.bitLength(), rnd);
        } while (!e.gcd(phi).equals(BigInteger.ONE) || e.compareTo(phi) >= 0);

        RSADemo rsa = new RSADemo();
        rsa.initializeRSA(p, q, e, new TextArea());

        BigInteger message = new BigInteger("1234567890");
        BigInteger encrypted = rsa.encrypt(message);
        BigInteger decrypted = rsa.decrypt(encrypted);

        assertEquals(message, decrypted);
    }
}

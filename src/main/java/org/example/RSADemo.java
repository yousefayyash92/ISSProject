
package org.example;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.math.BigInteger;

public class RSADemo extends Application {
    private BigInteger publicKey;
    private BigInteger privateKey;
    private BigInteger modulus;

    public RSADemo() {
    }

    public void initializeRSA(BigInteger p, BigInteger q, BigInteger e, TextArea resultArea) {
        resultArea.setText("");
        if (!p.isProbablePrime(100) || !q.isProbablePrime(100)) {
            resultArea.setText("Error: p and q must be prime numbers.");
            return;
        }

        if (p.compareTo(BigInteger.ZERO) <= 0 || q.compareTo(BigInteger.ZERO) <= 0 || e.compareTo(BigInteger.ZERO) <= 0) {
            resultArea.setText("Error: p, q, and e must be positive.");
            return;
        }

        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        if (e.compareTo(phi) >= 0) {
            resultArea.setText("Error: e must be less than (p-1)(q-1).");
            return;
        }

        if (!phi.gcd(e).equals(BigInteger.ONE)) {
            resultArea.setText("Error: e must be coprime to (p-1)(q-1).");
            return;
        }

        this.modulus = p.multiply(q);
        this.publicKey = e;
        this.privateKey = e.modInverse(phi);
    }

    public BigInteger encrypt(BigInteger message) {
        return message.modPow(publicKey, modulus);
    }

    public BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(privateKey, modulus);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("RSA Demo");

        int bitLength = 1024;
        BigInteger p = TestGenerator.generatePrime(bitLength);
        BigInteger q = TestGenerator.generatePrime(bitLength);
        BigInteger e = TestGenerator.generateE((p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE)));

        TextArea resultArea = new TextArea();
        this.initializeRSA(p, q, e, resultArea);

        TextField messageField = new TextField();
        messageField.setPromptText("Enter a message to encrypt");

        TextField decryptField = new TextField();
        decryptField.setPromptText("Enter a message to decrypt");

        Label pLabel = new Label("p: ");
        TextField pField = new TextField(p.toString());
        Label qLabel = new Label("q: ");
        TextField qField = new TextField(q.toString());
        Label eLabel = new Label("e: ");
        TextField eField = new TextField(e.toString());

        Button encryptBtn = new Button();
        encryptBtn.setText("Encrypt");
        encryptBtn.setOnAction(event -> {
            BigInteger encrypted = this.encrypt(new BigInteger(messageField.getText().getBytes()));
            messageField.setText("Encrypted message: " + encrypted.toString());
        });

        Button decryptBtn = new Button();
        decryptBtn.setText("Decrypt");
        decryptBtn.setOnAction(event -> {
            BigInteger decrypted = this.decrypt(new BigInteger(decryptField.getText()));
            decryptField.setText("Decrypted message: " + new String(decrypted.toByteArray()));
        });

        Button initBtn = new Button();
        initBtn.setText("Initialize RSA");
        initBtn.setOnAction(event -> {
            this.initializeRSA(new BigInteger(pField.getText()), new BigInteger(qField.getText()), new BigInteger(eField.getText()), resultArea);
        });

        VBox vbox = new VBox(pLabel, pField, qLabel, qField, eLabel, eField, initBtn, messageField, encryptBtn, decryptField, decryptBtn, resultArea);
        Scene scene = new Scene(vbox, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package com.frankmoley.crypto.asymmetric;

import org.junit.jupiter.api.Test;

import javax.xml.bind.DatatypeConverter;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

class AsymmetricEncryptionUtilsTest {

    @Test
    void generateRSAKeyPair() throws Exception{
        KeyPair keyPair = AsymmetricEncryptionUtils.generateRSAKeyPair();
        assertNotNull(keyPair);
        System.out.println("Private Key: " + DatatypeConverter.printHexBinary(keyPair.getPrivate().getEncoded()));
        System.out.println("Public Key:  " + DatatypeConverter.printHexBinary(keyPair.getPublic().getEncoded()));
    }

    @Test
    void testRSACryptoRoutine() throws Exception{
        KeyPair keyPair = AsymmetricEncryptionUtils.generateRSAKeyPair();
        String plainText = "This is the text we are going to hide in plain sight";
        byte[] cipherText = AsymmetricEncryptionUtils.performRSAEncryption(plainText, keyPair.getPrivate());
        assertNotNull(cipherText);
        System.out.println(DatatypeConverter.printHexBinary(cipherText));
        String decryptedText = AsymmetricEncryptionUtils.performRSADecryption(cipherText, keyPair.getPublic());
        assertEquals(plainText, decryptedText);
    }
}
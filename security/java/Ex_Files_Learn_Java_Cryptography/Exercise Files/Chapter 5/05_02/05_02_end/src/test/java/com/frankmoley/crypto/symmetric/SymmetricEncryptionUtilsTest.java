package com.frankmoley.crypto.symmetric;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

import static org.junit.jupiter.api.Assertions.*;

class SymmetricEncryptionUtilsTest {

    @Test
    void createAESKey() throws Exception {
        SecretKey key = SymmetricEncryptionUtils.createAESKey();
        assertNotNull(key);
        System.out.println(DatatypeConverter.printHexBinary(key.getEncoded()));
    }

    @Test
    void testAESCyrptoRoutine() throws Exception{
        SecretKey key = SymmetricEncryptionUtils.createAESKey();
        byte[] initializationVector = SymmetricEncryptionUtils.createInitializationVector();
        String plainText = "This is the text we are going to hide in plain sight";
        byte[] cipherText = SymmetricEncryptionUtils.performAESEncyption(plainText, key, initializationVector);
        assertNotNull(cipherText);
        System.out.println(DatatypeConverter.printHexBinary(cipherText));
        String decryptedText = SymmetricEncryptionUtils.performAESDecryption(cipherText, key, initializationVector);
        assertEquals(plainText, decryptedText);
    }
}
package com.frankmoley.crypto.keystore;

import javax.crypto.SecretKey;
import java.security.KeyStore;

public class KeyStoreUtils {

    private static final String SECRET_KEY_KEYSTORE_TYPE = "JCEKS";

    public static KeyStore createPrivateKeyJavaKeyStore(String keystorePassword, String keyAlias, SecretKey secretKey, String secretKeyPassword) throws Exception{
        KeyStore keyStore = KeyStore.getInstance(SECRET_KEY_KEYSTORE_TYPE);
        keyStore.load(null, keystorePassword.toCharArray());
        KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(secretKeyPassword.toCharArray());
        KeyStore.SecretKeyEntry privateKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
        keyStore.setEntry(keyAlias, privateKeyEntry, entryPassword);
        return keyStore;
    }
}

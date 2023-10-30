/**
 * Copyright (c) 2023  The Stellar Cartographers' Guild.
 *
 * This work is licensed under the terms of the MIT license.
 * For a copy, see <https://opensource.org/licenses/MIT>.
 */
package space.tscg.operation.encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import space.tscg.TSCGServer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EncryptDecrypt
{
    private static final String PRIVATE_KEY_FILENAME = "key.private";
    private static final String PRIVATE_KEY_PATHNAME = "/" + PRIVATE_KEY_FILENAME;
    private static final String PUBLIC_KEY_FILENAME = "key.public";
    private static final String PUBLIC_KEY_PATHNAME = "/" + PUBLIC_KEY_FILENAME;

    static String encode(String toEncode) {
        if(TSCGServer.TESTING) { return toEncode; }
        
        try {
            PublicKey publicKey = loadPublicKey();

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] bytes = cipher.doFinal(toEncode.getBytes(StandardCharsets.UTF_8));
            return new String(Base64.getEncoder().withoutPadding().encode(bytes));
        } catch (Exception e) { e.printStackTrace(); return "EncodedFailure"; }
    }

    public static String decode(String toDecode) {
        if(TSCGServer.TESTING) { return toDecode; }
        
        try {
            PrivateKey privateKey = loadPrivateKey();
    
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
    
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(toDecode));
            return new String(bytes);
        } catch (Exception e) { e.printStackTrace(); return "DecodeFailure"; }
    }

    private static PublicKey loadPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File tryFile = new File(PUBLIC_KEY_FILENAME);
        byte[] publicKeyBytes;
        try(InputStream inStream = new FileInputStream(tryFile)) {
            publicKeyBytes = inStream.readAllBytes();
        } catch (FileNotFoundException e) {
            publicKeyBytes = EncryptDecrypt.class.getResourceAsStream(PUBLIC_KEY_PATHNAME).readAllBytes();
        }
        KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return publicKeyFactory.generatePublic(publicKeySpec); 
    }

    private static PrivateKey loadPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        File tryFile = new File(PRIVATE_KEY_FILENAME);
        byte[] privateKeyBytes;
        try(InputStream inStream = new FileInputStream(tryFile)) {
            privateKeyBytes = inStream.readAllBytes();
        } catch (FileNotFoundException e) {
            privateKeyBytes = EncryptDecrypt.class.getResourceAsStream(PRIVATE_KEY_PATHNAME).readAllBytes();
        }
        KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return privateKeyFactory.generatePrivate(privateKeySpec);
    }
}

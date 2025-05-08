package pt.community.java.splitwise_like.oauth.utils;

import org.bouncycastle.util.io.pem.PemReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class PemUtils {

    public static RSAPrivateKey readPrivateKey(String location) throws Exception {
        InputStream is = PemUtils.class.getResourceAsStream(location);
        try (PemReader reader = new PemReader(new InputStreamReader(is))) {
            byte[] content = reader.readPemObject().getContent();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(content);
            return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        }
    }

    public static RSAPublicKey readPublicKey(String location) throws Exception {
        InputStream is = PemUtils.class.getResourceAsStream(location);
        try (PemReader reader = new PemReader(new InputStreamReader(is))) {
            byte[] content = reader.readPemObject().getContent();
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(content);
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
        }
    }
}
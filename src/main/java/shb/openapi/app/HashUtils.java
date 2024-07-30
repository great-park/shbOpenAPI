package shb.openapi.app;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;
import shb.openapi.global.constant.ResponseCode;
import shb.openapi.global.exception.GeneralException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Component
public class HashUtils {
    private final static String HASH_ALGORITHM = "HmacSHA256";

    public static String tokenHash(String API_KEY, String SECRET_KEY, long timestamp) {
        String client_hash = "";

        try {
            Mac mac = Mac.getInstance(HASH_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), HASH_ALGORITHM);
            mac.init(secretKey);

            client_hash = Base64.encodeBase64String(mac.doFinal((timestamp + "|" +API_KEY).getBytes(StandardCharsets.UTF_8)));
            client_hash = client_hash.replaceAll("\r\n|\n", "");
            client_hash = URLEncoder.encode(client_hash, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new GeneralException(ResponseCode.HASH_ERROR, e);
        }
        log.info("tokenHash : client_hash: {}", client_hash);
        return client_hash;
    }

    public static String bodyHash(String clientSecret, String strHttpBody) {
        String client_hash = "";

        try {
            Mac mac = Mac.getInstance(HASH_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(clientSecret.getBytes(), HASH_ALGORITHM);
            mac.init(secretKey);
            client_hash = Base64.encodeBase64String(mac.doFinal(strHttpBody.getBytes(StandardCharsets.UTF_8)));
            client_hash = client_hash.replaceAll(System.lineSeparator(), "");

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new GeneralException(ResponseCode.HASH_ERROR, e);
        }
        log.info("bodyHash : client_hash: {}", client_hash);
        return client_hash;
    }
}

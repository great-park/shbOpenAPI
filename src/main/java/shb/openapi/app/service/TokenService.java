package shb.openapi.app.service;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import shb.openapi.app.dto.TokenRequest;
import shb.openapi.app.dto.TokenResponse;
import shb.openapi.global.constant.ResponseCode;
import shb.openapi.global.exception.GeneralException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import static shb.openapi.app.HashUtils.bodyHash;
import static shb.openapi.app.HashUtils.tokenHash;

@Slf4j
@Service
public class TokenService {
    private static final String DEV_TOKEN_URL = "https://shbapi.shinhan.com:6443/v1/oauth/partner/token"; // access token 발급 url (2-legged)
    private static final String SCOPE = "oob";
    private static final String GRANT_TYPE = "client_credentials";
    private static final String AUTHORIZATION_PREFIX = "Bearer ";

    public TokenResponse getAccessToken(TokenRequest tokenRequest) {
        final long TIMESTAMP = System.currentTimeMillis() / 1000;
        final String API_KEY = tokenRequest.getClientId();
        final String SECRET_KEY = tokenRequest.getClientSecret();
        final String CLIENT_HASH = tokenHash(API_KEY, SECRET_KEY, TIMESTAMP);

        JSONObject result = callApi(API_KEY, TIMESTAMP, CLIENT_HASH);
        String accessToken = (String) result.get("access_token");
        log.info("Access token: {}", accessToken);

        return TokenResponse.builder()
                .accessToken(AUTHORIZATION_PREFIX + accessToken)
                .expiresIn((String)result.get("expires_in"))
                .build();
    }

    private JSONObject callApi(String apiKey, long timestamp, String clientHash) {
        StringBuilder result = new StringBuilder();
        OutputStream os = null;
        BufferedReader reader = null;

        try {
            //SSL Context 생성
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // SSL 연결
            URL url = new URL(DEV_TOKEN_URL);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            // http header 세팅
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            // http body 세팅
            String httpBody = "client_id=" + apiKey;
            httpBody += "&scope="+ SCOPE;
            httpBody += "&grant_type=" + GRANT_TYPE;
            httpBody += "&client_hash=" + clientHash;
            httpBody += "&timestamp=" + timestamp;

            // 요청 송신
            byte[] buf = httpBody.getBytes(StandardCharsets.UTF_8);
            os = conn.getOutputStream();
            os.write(buf, 0, buf.length);
            os.flush();

            // 응답 수신
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            String buff = "";
            while ((buff = reader.readLine()) != null) {
                result.append(buff);
            }
        } catch (Exception e) {
            throw new GeneralException(ResponseCode.CALL_API_ERROR, e);
        }
        JSONObject jsonObject = new JSONObject(result.toString());

        return (JSONObject) jsonObject.get("dataBody");
    }

    public String getBodyHash(String clientSecret, Object testBody) {
        log.info("testBody: {}", testBody);
        return bodyHash(clientSecret, testBody.toString());
    }
}


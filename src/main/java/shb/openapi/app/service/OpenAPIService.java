package shb.openapi.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import shb.openapi.app.dto.CallApiDto;
import shb.openapi.global.constant.ResponseCode;
import shb.openapi.global.dto.ApiDataResponse;
import shb.openapi.global.exception.GeneralException;

import static shb.openapi.app.HashUtils.bodyHash;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAPIService {
    private static final String OPEN_API_ENDPOINT = "https://shbapi.shinhan.com:6443";

    public ApiDataResponse<JSONObject> callApi(CallApiDto callApiDto) {
        final String accessToken = callApiDto.getAccessToken();
        final String API_KEY = callApiDto.getClientId();
        final String SECRET_KEY = callApiDto.getClientSecret();
        final String TEST_URL = OPEN_API_ENDPOINT + callApiDto.getRequestUri();
        final String requestBody = callApiDto.getRequestBody();

        String hashKey = bodyHash(SECRET_KEY, requestBody);
        JSONObject openApiResponse;
        try {
            openApiResponse = httpPostRequest(accessToken, API_KEY, hashKey, requestBody, TEST_URL);
        } catch (ParseException e) {
            throw new GeneralException(ResponseCode.JSON_PARSING_ERROR, e);
        } catch (RestClientException e) {
            throw new GeneralException(ResponseCode.CALL_API_ERROR, e);
        }
        return ApiDataResponse.of(openApiResponse);
    }

    private static JSONObject httpPostRequest(String accessToken, String API_KEY, String hashKey, String requestBody, String TEST_URL) throws ParseException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("Accept", "application/json; charset=UTF-8");
        headers.set("Authorization", accessToken);
        headers.set("charset", "UTF-8");
        headers.set("apikey", API_KEY);
        headers.set("hsKey", hashKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(TEST_URL, HttpMethod.POST, entity, String.class);
        log.info("response: {}", response.getBody());

        return (JSONObject) new JSONParser().parse(response.getBody());
    }
}

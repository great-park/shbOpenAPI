package shb.openapi.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import shb.openapi.app.dto.CallApiDto;
import shb.openapi.app.service.OpenAPIService;
import shb.openapi.global.dto.ApiDataResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/v1/**", "/v2/**"})
public class OpenAPIController {
    private final OpenAPIService openApiService;

    /**
     * Open API 테스트 (조회성 거래만 실시할 것)
     */
    @PostMapping
    public ApiDataResponse<JSONObject> callOpenApi(
            @RequestHeader HttpHeaders header,
            @RequestBody String StrRequestBody,
            HttpServletRequest httpServletRequest
    ) {
        return openApiService.callApi(CallApiDto.builder()
                .requestBody(StrRequestBody)
                .requestUri(httpServletRequest.getRequestURI())
                .accessToken(header.getFirst("Authorization"))
                .clientId(header.getFirst("ClientId"))
                .clientSecret(header.getFirst("ClientSecret"))
                .build());
    }
}

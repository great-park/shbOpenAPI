package shb.openapi.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shb.openapi.app.dto.TokenRequest;
import shb.openapi.app.dto.TokenResponse;
import shb.openapi.app.service.TokenService;
import shb.openapi.global.dto.ApiDataResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class TokenController {
    private final TokenService tokenService;

    /**
    * Open API 외부업체 AccessToken 생성 테스트 (2-legged)
    */
    @PostMapping("/partner")
    public ApiDataResponse<TokenResponse> getAccessToken(
            @Validated @RequestBody TokenRequest tokenRequest
    ) {
        return ApiDataResponse.of(
                tokenService.getAccessToken(tokenRequest)
        );
    }

    /**
     * Api Call을 위한 body hash 값 생성 테스트
     */
    @PostMapping("/body")
    public ApiDataResponse<String> getAccessToken(
            @RequestBody Object testBody,
            @RequestParam String clientSecret
    ) {
        return ApiDataResponse.of(
                tokenService.getBodyHash(clientSecret, testBody)
        );
    }
}

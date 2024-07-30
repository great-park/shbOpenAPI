package shb.openapi.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenRequest {
    @NotNull(message = "clientId를 입력해주세요.")
    private String clientId;

    @NotNull(message = "clientSecret를 입력해주세요.")
    private String clientSecret;
}

package shb.openapi.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallApiDto {
    private String requestBody;
    private String accessToken;
    private String requestUri;
    private String clientId;
    private String clientSecret;
}

package shb.openapi.global.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import shb.openapi.global.exception.GeneralException;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    OK(0, HttpStatus.OK, "Ok"),

    JSON_PARSING_ERROR(2000, HttpStatus.BAD_REQUEST, "JSON parsing error"),
    HASH_ERROR(2001, HttpStatus.BAD_REQUEST, "Hashing error"),
    CALL_API_ERROR(2002, HttpStatus.BAD_REQUEST, "Call API error"),

    // 4xxx : Common Client Error
    BAD_REQUEST(4000, HttpStatus.BAD_REQUEST, "Bad request"),
    NOT_FOUND(4001, HttpStatus.NOT_FOUND, "Requested resource is not found"),
    VALIDATION_ERROR(4002, HttpStatus.BAD_REQUEST, "Validation error"),

    // 5xxx : Common Server Error
    INTERNAL_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(5001, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),

    ;

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public static ResponseCode valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new GeneralException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(responseCode -> responseCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return ResponseCode.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return ResponseCode.INTERNAL_ERROR;
                    } else {
                        return ResponseCode.OK;
                    }
                });
    }

    public String getMessage(Throwable e) {
        return this.getMessage(e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }

}
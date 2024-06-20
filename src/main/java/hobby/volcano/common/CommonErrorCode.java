package hobby.volcano.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."),
    JWT_EXPIRATION(HttpStatus.UNAUTHORIZED, "jwt 토큰이 만료되었습니다."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED, "부적절한 jwt 토큰이 입력되었습니다."),
    JWT_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "jwt 토큰값은 필수입니다.")
    ;


    private final HttpStatus httpStatus;
    private final String message;
}



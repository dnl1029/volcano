package hobby.volcano.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomEnum {
    USER_ID("userId"),
    JWT_TOKEN("jwtToken"),
    SYSTEM("SYSTEM"),
    ADMIN_JWT_KEY("admin_jwt_key");


    private final String content;
}

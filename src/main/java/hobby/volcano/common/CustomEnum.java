package hobby.volcano.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomEnum {
    USER_ID("userId"),
    JWT_TOKEN("jwtToken"),
    SYSTEM("SYSTEM");


    private final String content;
}

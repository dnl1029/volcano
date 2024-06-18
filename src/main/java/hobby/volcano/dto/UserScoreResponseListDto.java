package hobby.volcano.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserScoreResponseListDto {

    private List<UserScoreResponseDto> scores;
    private Integer userId;
    private String userName;

    @Builder
    public UserScoreResponseListDto(List<UserScoreResponseDto> scores, Integer userId, String userName) {
        this.scores = scores;
        this.userId = userId;
        this.userName = userName;
    }
}

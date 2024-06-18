package hobby.volcano.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RankingResponseDtoList {

    private List<RankingResponseDto> rankings;

    @Builder
    public RankingResponseDtoList(List<RankingResponseDto> rankings) {
        this.rankings = rankings;
    }
}

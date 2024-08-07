package hobby.volcano.repository;

import hobby.volcano.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findByUserId(Integer userId);
    Optional<Score> findByWorkDtAndUserIdAndGameNum(String workDt, Integer userId, int tempGameNum);

    Optional<Score> findByWorkDtAndUserIdAndGameNumAndLaneNumAndLaneOrder(String workDt, Integer userId, int tempGameNum, Integer laneNum, Integer laneOrder);

    // workDt를 기준으로 점수를 조회하는 메서드
    List<Score> findByWorkDt(String workDt);
}

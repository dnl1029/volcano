package hobby.volcano.repository;

import hobby.volcano.entity.ScoreBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreBackupRepository extends JpaRepository<ScoreBackup, Long> {

}

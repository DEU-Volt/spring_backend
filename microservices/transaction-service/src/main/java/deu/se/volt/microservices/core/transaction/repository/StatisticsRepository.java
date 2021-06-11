package deu.se.volt.microservices.core.transaction.repository;

import deu.se.volt.microservices.core.transaction.entity.DailyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface StatisticsRepository extends JpaRepository<DailyStatistics, Long> {

    Optional<DailyStatistics> findDailyStatisticsByModelNameAndLocalDate(String modelName, LocalDate localDate);
    List<DailyStatistics> findDailyStatisticsByModelName(String modelName);


}

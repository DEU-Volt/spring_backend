package deu.se.volt.microservices.core.transaction.repository;

import deu.se.volt.microservices.core.transaction.entity.DailyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StatisticsRepository extends JpaRepository<DailyStatistics, Long> {


}

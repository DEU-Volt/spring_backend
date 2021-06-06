package deu.se.volt.microservices.core.transaction.service;

import deu.se.volt.microservices.core.transaction.entity.DailyStatistics;
import deu.se.volt.microservices.core.transaction.repository.StatisticsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;


    public DailyStatistics save(DailyStatistics dailyStatistics) {
        return statisticsRepository.save(dailyStatistics);
    }


}

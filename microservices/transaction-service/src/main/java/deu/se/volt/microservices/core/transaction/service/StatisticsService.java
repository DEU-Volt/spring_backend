package deu.se.volt.microservices.core.transaction.service;

import deu.se.volt.microservices.core.transaction.entity.DailyStatistics;
import deu.se.volt.microservices.core.transaction.repository.StatisticsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@AllArgsConstructor
public class StatisticsService {

    private final StatisticsRepository statisticsRepository;


    public DailyStatistics loadDailyStatisticsByModelNameAndLocalDate(String modelName, LocalDate localDate) throws NoSuchElementException{
        return statisticsRepository.findDailyStatisticsByModelNameAndLocalDate(modelName, localDate).orElseThrow();

    }

    public List<DailyStatistics> loadDailyStatisticsByModelName(String modelName) {
        return statisticsRepository.findDailyStatisticsByModelName(modelName);
    }

    public DailyStatistics save(DailyStatistics dailyStatistics) {
        return statisticsRepository.save(dailyStatistics);
    }

}

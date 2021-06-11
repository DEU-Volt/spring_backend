package deu.se.volt.microservices.core.transaction.controller;

import deu.se.volt.microservices.core.transaction.entity.DailyStatistics;
import deu.se.volt.microservices.core.transaction.service.StatisticsService;
import deu.se.volt.microservices.core.transaction.util.DefaultResponse;
import deu.se.volt.microservices.core.transaction.util.ResponseMessage;
import deu.se.volt.microservices.core.transaction.util.StatusCode;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    /*
    GET / 모델명으로 통계 조회 / Return : List<Statistics>
    */
    @ApiOperation(value = "모델명으로 조회", tags = "통계 관리",
            httpMethod = "GET",
            notes = "모델명을 사용하여 통계 목록을 조회할 때 사용되는 API 입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")

    @GetMapping("/transaction/statistics/{modelName}")
    public ResponseEntity getDailyStatisticsByModelName(@PathVariable("modelName") @Valid final String modelName) {
        var statistics = statisticsService.loadDailyStatisticsByModelName(modelName);

        if (statistics.isEmpty()) {
            Map<String, String> map = new HashMap<>();
            map.put("result", "false");

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_DAILY_STATISTICS,
                    map), HttpStatus.OK);
        } else {
            Map<String, List<DailyStatistics>> map = new HashMap<>();
            map.put("result", statistics);
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.STATISTICS_SUCCESS,
                    map), HttpStatus.OK);
        }
    }

    /*
    GET / 모델명, 날짜로 통계 조회 / Return : List<Statistics>
    */
    @ApiOperation(value = "모델명, 날짜로 조회", tags = "통계 관리",
            httpMethod = "GET",
            notes = "모델명과 날짜를 사용하여 일일 통계치를 조회할 때 사용되는 API 입니다."
    )
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "Bearer access_token")
    @GetMapping("/transaction/statistics/{modelName}/{localDate}")
    public ResponseEntity getDailyStatisticsByModelNameAndLocalDate(@PathVariable("modelName")@Valid final String modelName
                                                                    ,@PathVariable("localDate")@DateTimeFormat(pattern="yyyy-MM-dd")@Valid final LocalDate localDate) {
        try {
            var statistics = statisticsService.loadDailyStatisticsByModelNameAndLocalDate(modelName, localDate);
            Map<String, DailyStatistics> map = new HashMap<>();
            map.put("result", statistics);
            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.OK,
                    ResponseMessage.STATISTICS_SUCCESS,
                    map), HttpStatus.OK);
        }
        catch (NoSuchElementException noSuchElementException) {
            Map<String, String> map = new HashMap<>();
            map.put("result", "false");

            return new ResponseEntity(DefaultResponse.res(
                    StatusCode.NOT_FOUND,
                    ResponseMessage.NOT_FOUND_DAILY_STATISTICS,
                    map), HttpStatus.OK);
        }



    }
}

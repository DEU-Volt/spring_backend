package deu.se.volt.microservices.core.transaction.service;


import deu.se.volt.microservices.core.transaction.entity.Transaction;
import deu.se.volt.microservices.core.transaction.repository.TransactionRepository;
import deu.se.volt.microservices.core.transaction.response.ProductResponse;
import deu.se.volt.microservices.core.transaction.response.UserResponse;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> loadYesterdayAllTransactions() {
        log.info("loadYesterdayAllTransactions : called");
        return transactionRepository.findTransactionsByCreatedAtBetweenOrderByModelNameAscCreatedAtAsc
                (LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0,0))
                        ,LocalDateTime.of(LocalDate.now().minusDays(1),LocalTime.of(23,59)));
//        return transactionRepository.findTransactionsByCreatedAtBetweenOrderByModelNameAscCreatedAtAsc
//                (LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0))
//                        ,LocalDateTime.of(LocalDate.now(),LocalTime.of(23,59)));
    }

    public List<Transaction> loadTransactionsByModelName(String modelName){
        return transactionRepository.findTransactionsByModelName(modelName);
    }

    public List<Transaction> loadTransactionsByUsername(String userName){
        return transactionRepository.findTransactionsByBuyerOrSeller(userName, userName);
    }


    public Transaction save(String accessToken, Transaction transaction) throws NotFoundException{
        try {
            if (!checkExistProduct(accessToken, transaction.getModelName())) {
                throw new NotFoundException(transaction.getModelName());
            }
            else if (!checkExistUser(accessToken, transaction.getSeller())) {
                throw  new NotFoundException(transaction.getSeller());
            }
            else if (!checkExistUser(accessToken, transaction.getBuyer())) {
                throw new NotFoundException(transaction.getBuyer());
            }

        } catch (NullPointerException | RestClientException exception) {
            log.error(exception.toString());
        }
        transactionRepository.save(transaction);
        return transaction;
    }

    /*
        ?????????????????? ????????? ??????????????? ????????? ??????????????? ??????
     */
    private boolean checkExistProduct(String accessToken, String modelName) throws RestClientException, NullPointerException {
        var header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, accessToken);
        var response = new RestTemplate().exchange("http://localhost:18081/product/model/" + modelName
                , HttpMethod.GET, new HttpEntity(header), ProductResponse.class);

        return Objects.requireNonNull(response.getBody()).getStatusCode() == 200;
    }

    /*
        ??????????????? ????????? ??????????????? ????????? ??????????????? ??????
     */
    private boolean checkExistUser(String accessToken, String userName) throws RestClientException, NullPointerException {
        var header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, accessToken);
        var response = new RestTemplate().exchange("http://localhost:9000/user/id?username=" + userName
                , HttpMethod.GET, new HttpEntity(header), UserResponse.class);

        return Objects.requireNonNull(response.getBody()).getStatusCode() == 200;
    }
}


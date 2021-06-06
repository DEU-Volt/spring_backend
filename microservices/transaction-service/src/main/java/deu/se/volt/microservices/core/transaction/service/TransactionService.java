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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;

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
        상품서비스에 상품이 존재하는지 여부를 확인해주는 함수
     */
    private boolean checkExistProduct(String accessToken, String modelName) throws RestClientException, NullPointerException {
        var header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, accessToken);
        var response = new RestTemplate().exchange("http://localhost:18081/product/model/" + modelName
                , HttpMethod.GET, new HttpEntity(header), ProductResponse.class);

        return Objects.requireNonNull(response.getBody()).getStatusCode() == 200;
    }

    /*
        인증서버에 유저가 존재하는지 여부를 확인해주는 함수
     */
    private boolean checkExistUser(String accessToken, String userName) throws RestClientException, NullPointerException {
        var header = new HttpHeaders();
        header.add(HttpHeaders.AUTHORIZATION, accessToken);
        var response = new RestTemplate().exchange("http://localhost:9000/user/id?username=" + userName
                , HttpMethod.GET, new HttpEntity(header), UserResponse.class);

        return Objects.requireNonNull(response.getBody()).getStatusCode() == 200;
    }
}

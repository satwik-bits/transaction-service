package com.transaction.management.service.impl;

import com.transaction.management.constants.Constants;
import com.transaction.management.entity.AccountUserAuth;
import com.transaction.management.entity.Transactions;
import com.transaction.management.enums.Permission;
import com.transaction.management.kafka.producer.NotificationServiceProducer;
import com.transaction.management.repository.AccountsRepository;
import com.transaction.management.repository.TransactionRepository;
import com.transaction.management.request.TransactionRequest;
import com.transaction.management.entity.Accounts;
import com.transaction.management.response.FraudResponse;
import com.transaction.management.response.NotificationResponse;
import com.transaction.management.service.TransactionService;
import com.transaction.management.validations.TransactionRequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionRequestValidator transactionRequestValidator;

    @Autowired
    private NotificationServiceProducer notificationServiceProducer;

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${user.account.url}")
    private String userAccountUrl;

    @Value("${kafka.transaction.topic}")
    private String topic;



    @Override
    public void processTransaction(Long userId, TransactionRequest transactionRequest) throws Exception {
        //Make REST call to fetch user details to User Service

        AccountUserAuth accountUserAuth = getAccountByUserId(userId);

        if(!accountUserAuth.getAccountEnabled()){
            throw new Exception("User Account is Blocked, no Transaction Possible!!");
        }
        Permission type = Permission.valueOf(transactionRequest.getTransactionType());
        transactionRequestValidator.validateTransactionRequest(transactionRequest);
        Set<Permission> allowedPermissions = accountUserAuth.getUser().getPermissions();
        if(allowedPermissions.contains(Permission.valueOf(transactionRequest.getTransactionType()))){
            switch (type) {
                case Permission.DEPOSIT:
                    Optional<Accounts> accountsDepositOptional = accountsRepository.findByAccountNumber(transactionRequest.getSenderAccount());
                    if(Objects.equals(makeRestCallToFraud(transactionRequest).getStatus(), Constants.SUCCESSFUL)) {
                        notificationServiceProducer.send(topic, String.valueOf(userId), getNotificationResponse(userId, Constants.SUCCESSFUL));
                    }
                    else{
                        notificationServiceProducer.send(topic, String.valueOf(userId), getNotificationResponse(userId, Constants.FAILED));
                        throw new Exception("Fraud Check failed");
                    }
                    if (accountsDepositOptional.isPresent()) {
                        Accounts accounts = accountsDepositOptional.get();
                        accounts.setBalance(accounts.getBalance() + transactionRequest.getAmount());
                        accountsRepository.save(accounts);
                    }

                    saveTransaction(userId, transactionRequest);
                    break;

                case Permission.TRANSFER:
                    Optional<Accounts> senderOptional = accountsRepository.findByAccountNumber(transactionRequest.getSenderAccount());
                    Optional<Accounts> receiverOptional = accountsRepository.findByAccountNumber(transactionRequest.getReceiverAccount());
                    if (senderOptional.isPresent() && receiverOptional.isPresent()) {
                        Accounts senderAccount = senderOptional.get();
                        Accounts receiverAccount = receiverOptional.get();
                        if(Objects.equals(makeRestCallToFraud(transactionRequest).getStatus(), Constants.SUCCESSFUL)) {
                            notificationServiceProducer.send(topic, String.valueOf(userId), getNotificationResponse(userId, Constants.SUCCESSFUL));
                        }
                        else{
                            notificationServiceProducer.send(topic, String.valueOf(userId), getNotificationResponse(userId, Constants.FAILED));
                            throw new Exception("Fraud Check failed");
                        }
                        if(checkBalance(senderAccount.getBalance(), transactionRequest.getAmount())) {
                            senderAccount.setBalance(senderAccount.getBalance() - transactionRequest.getAmount());
                            receiverAccount.setBalance(receiverAccount.getBalance() + transactionRequest.getAmount());
                        }
                        else{
                            notificationServiceProducer.send(topic, String.valueOf(userId), getNotificationResponse(userId, Constants.FAILED));
                            throw new Exception("Transfer not possible with this amount!!");
                        }
                        saveTransaction(userId, transactionRequest);
                        notificationServiceProducer.send(topic, "key", getNotificationResponse(userId, Constants.SUCCESSFUL));
                        accountsRepository.save(senderAccount);
                        accountsRepository.save(receiverAccount);
                    }
                    break;

                case Permission.WITHDRAWAL:
                    Optional<Accounts> accountsWithdrawalOptional = accountsRepository.findByAccountNumber(transactionRequest.getSenderAccount());
                    if (accountsWithdrawalOptional.isPresent()) {
                        Accounts accounts = accountsWithdrawalOptional.get();
                        if(Objects.equals(makeRestCallToFraud(transactionRequest).getStatus(), Constants.SUCCESSFUL)) {
                            notificationServiceProducer.send(topic, String.valueOf(userId), getNotificationResponse(userId, Constants.SUCCESSFUL));
                        }
                        else{
                            notificationServiceProducer.send(topic, String.valueOf(userId), getNotificationResponse(userId, Constants.FAILED));
                            throw new Exception("Fraud Check failed");
                        }
                        if(checkBalance(accounts.getBalance(), transactionRequest.getAmount())) {
                            accounts.setBalance(accounts.getBalance() - transactionRequest.getAmount());
                        }
                        else{
                            notificationServiceProducer.send(topic, "key", getNotificationResponse(userId, Constants.FAILED));
                            throw new Exception("Withdrawal not possible with this amount!!");
                        }
                        notificationServiceProducer.send(topic, "key", getNotificationResponse(userId, Constants.SUCCESSFUL));
                        saveTransaction(userId, transactionRequest);
                        accountsRepository.save(accounts);
                    }
                    break;

                default:
                    throw new Exception("No Other Permission possible!!");
            }
        }
    }

    private AccountUserAuth getAccountByUserId(Long userId) {
        String url = userAccountUrl + userId; // replace localhost:8080 if needed

        ResponseEntity<AccountUserAuth> response = restTemplate.getForEntity(url, AccountUserAuth.class);

        return response.getBody();
    }

    private boolean checkBalance(Double accountBalance, Double transactionBalance){
        return accountBalance > transactionBalance;
    }

    private void saveTransaction(Long userId, TransactionRequest transactionRequest){
        Transactions transactions = new Transactions();
        transactions.setType(transactionRequest.getTransactionType());
        transactions.setStatus(Constants.SUCCESSFUL);
        transactions.setTimestamp(LocalDateTime.now());
        transactions.setAccNoFrom(transactionRequest.getSenderAccount());
        transactions.setAccNoTo(transactionRequest.getReceiverAccount());
        transactions.setUserId(userId);
        transactionRepository.save(transactions);
    }

    private NotificationResponse getNotificationResponse(Long userId, String status){
        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setUser_id(userId);
        notificationResponse.setStatus(status);
        return notificationResponse;
    }

    private FraudResponse makeRestCallToFraud(TransactionRequest transactionRequest){
        String fraudApiUrl = "http://localhost:8082/api/fraud/check";

        ResponseEntity<FraudResponse> fraudResponse = restTemplate.postForEntity(
                fraudApiUrl,
                transactionRequest,
                FraudResponse.class
        );

// Step 2: Handle fraud response
        return fraudResponse.getBody();
    }

}

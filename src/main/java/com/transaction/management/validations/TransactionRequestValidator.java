package com.transaction.management.validations;

import com.transaction.management.request.TransactionRequest;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionRequestValidator {

    public void validateTransactionRequest(TransactionRequest transactionRequest) throws Exception {
        if(StringUtils.isBlank(transactionRequest.getTransactionType())){
            throw new Exception("Provide valid transaction type!!");
        }
        if(transactionRequest.getAmount()==null || transactionRequest.getAmount()<0 ){
            throw new Exception("Provide valid amount!!");
        }
        if(StringUtils.isBlank(transactionRequest.getReceiverAccount())){
            throw new Exception("Provided Receiver account details cannot be null/blank");
        }
        else{
            if(transactionRequest.getReceiverAccount().length()!=10){
                throw new Exception("Receiver amount should be maximum of 10 characters!!");
            }
        }
        if(StringUtils.isBlank(transactionRequest.getSenderAccount())){
            throw new Exception("Provided Sender account details cannot be null/blank");
        }
        else{
            if(transactionRequest.getSenderAccount().length()!=10){
                throw new Exception("Sender amount should be maximum of 10 characters!!");
            }
        }
        if(transactionRequest.getRequestedDate().isAfter(LocalDateTime.now())){
            throw new Exception("Date cannot be after current time");
        }
    }
}

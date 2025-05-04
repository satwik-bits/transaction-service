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

        if(StringUtils.isNotBlank(transactionRequest.getAccNoTo())) {

            if ((transactionRequest.getAccNoTo().length() != 12) && (transactionRequest.getAccNoTo().length() != 16)) {
                throw new Exception("Receiver account should be maximum of 12 or 16 characters!!");
            }
        }

        if(StringUtils.isBlank(transactionRequest.getAccNoFrom())){
            throw new Exception("Provided Sender account details cannot be null/blank");
        }
        else{
            if((transactionRequest.getAccNoFrom().length()!=12) && (transactionRequest.getAccNoFrom().length()!=16) ){
                throw new Exception("Sender account should be maximum of 12 or 16 characters!!");
            }
        }
        if(transactionRequest.getTimestamp().isAfter(LocalDateTime.now())){
            throw new Exception("Date cannot be after current time");
        }
    }
}

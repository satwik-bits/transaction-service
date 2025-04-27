package com.transaction.management.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {

    private String username;

    private String email;

    private String password;

    private String userId;

    private String accNo;

    private String ifsc_code;

    private String accountType;

    private String bankName;

    private String currency;

}

package com.transaction.management.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserResponse {

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

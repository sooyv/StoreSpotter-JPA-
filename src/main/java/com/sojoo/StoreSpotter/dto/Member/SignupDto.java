package com.sojoo.StoreSpotter.dto.Member;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SignupDto {
    private String name;
    private String email;
    private String password;
    private String checkPassword;
    private String phone;
}



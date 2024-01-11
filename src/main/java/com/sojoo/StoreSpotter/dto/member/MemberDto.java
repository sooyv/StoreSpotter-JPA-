package com.sojoo.StoreSpotter.dto.member;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MemberDto {
    private String name;
    private String email;
    private String password;
    private String checkPassword;
    private String phone;
}



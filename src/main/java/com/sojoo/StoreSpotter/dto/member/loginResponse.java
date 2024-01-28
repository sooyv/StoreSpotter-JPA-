package com.sojoo.StoreSpotter.dto.member;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class loginResponse {
    private String email;
    private String password;
}

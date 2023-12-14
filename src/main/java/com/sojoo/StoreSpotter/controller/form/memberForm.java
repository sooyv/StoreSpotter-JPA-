package com.sojoo.StoreSpotter.controller.form;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class memberForm {
//    @NotEmpty(message = "회원 이름은 필수입니다.") //필수 입력값
    private String name;
    private String email;
    private String password;
    private String phone;
}

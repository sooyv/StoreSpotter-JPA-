package com.sojoo.StoreSpotter.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPwdDto {

    private String currentPwd;
    private String changePwd;
    private String changeChkPwd;


    public UserPwdDto(String currentPwd, String changePwd, String changeChkPwd){
        this.currentPwd = currentPwd;
        this.changePwd = changePwd;
        this.changeChkPwd = changeChkPwd;
    }

}

package com.sojoo.StoreSpotter.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sojoo.StoreSpotter.entity.user.Authority;
import com.sojoo.StoreSpotter.entity.user.User;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @NotNull
    @Size(min = 3, max = 6)
    private String mailCode;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Size(min = 3, max = 100)
    private String checkPassword;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private String phone;

    @NotNull
    @Size(min = 3, max = 50)
    private String nickname;

    private Authority authority;

}
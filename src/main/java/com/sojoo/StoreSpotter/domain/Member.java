package com.sojoo.StoreSpotter.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter @Setter
@Entity
public class Member {
    @Id
    private Long memberId;
    private String memberName;
    private String memberEmail;
    private String memberPassword;
    private String memberPhone;
}

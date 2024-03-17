package com.sojoo.StoreSpotter.entity.user;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authority")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @Column(name = "authority_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorityId;

    @Column(name = "authority_name", length = 50)
    private String authorityName;

//    @OneToMany
//    private List<User> userList = new ArrayList<>();
}
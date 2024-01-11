package com.sojoo.StoreSpotter.entity.apiToDb;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Industry {

    @Id
    @Column(name="indust_id")
    private String industId;       // 업종 코드

    @Column(name = "indust_name")
    private String industName;     // 업종명
}

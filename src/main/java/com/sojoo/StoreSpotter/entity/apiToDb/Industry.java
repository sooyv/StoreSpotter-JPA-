package com.sojoo.StoreSpotter.entity.apiToDb;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Industry {

    @Id
    @Column(name="indust_id")
    private String industId;

    @Column(name = "indust_name", nullable = false)
    private String industName;

    public Industry (String industId, String industName){
        this.industId = industId;
        this.industName = industName;
    }

}

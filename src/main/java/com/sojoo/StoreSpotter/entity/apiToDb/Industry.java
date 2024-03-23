package com.sojoo.StoreSpotter.entity.apiToDb;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Industry {

    @Id
    @Column(name="indust_id")
    private String industId;

    @Column(name = "indust_name", nullable = false)
    private String industName;
}

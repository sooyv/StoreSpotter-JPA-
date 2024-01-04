package com.sojoo.StoreSpotter.dto.apiToDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Region {
    @Id
    @Column(name="region_id")
    private Integer regionId;      // 지역 코드

    @Column(name="region_name")
    private String regionName;     // 지역명
}

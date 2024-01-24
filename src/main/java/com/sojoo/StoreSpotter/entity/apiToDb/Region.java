package com.sojoo.StoreSpotter.entity.apiToDb;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

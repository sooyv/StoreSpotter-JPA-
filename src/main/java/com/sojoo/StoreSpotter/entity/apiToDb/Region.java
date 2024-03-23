package com.sojoo.StoreSpotter.entity.apiToDb;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Region {
    @Id
    @Column(name="region_id")
    private Integer regionId;

    @Column(name="region_name", nullable = false)
    private String regionName;


}

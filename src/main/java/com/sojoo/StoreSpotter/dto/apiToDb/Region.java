package com.sojoo.StoreSpotter.dto.apiToDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region {
    private Integer region_id;      // 지역 코드
    private String region_name;     // 지역명
}

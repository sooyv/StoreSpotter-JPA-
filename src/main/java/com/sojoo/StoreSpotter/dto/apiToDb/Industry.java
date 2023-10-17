package com.sojoo.StoreSpotter.dto.apiToDb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Industry {
    private String indust_id;       // 업종 코드
    private String indust_name;     // 업종명
}

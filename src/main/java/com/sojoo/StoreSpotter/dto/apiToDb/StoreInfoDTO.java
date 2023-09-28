package com.sojoo.StoreSpotter.dto.apiToDb;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreInfoDTO {
    private Long bizesId;
    private String bizesNm;
    private String rdnmAdr;
}
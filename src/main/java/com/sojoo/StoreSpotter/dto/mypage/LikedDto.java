package com.sojoo.StoreSpotter.dto.mypage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import com.sojoo.StoreSpotter.entity.mypage.Liked;
import lombok.*;

@NoArgsConstructor
@Getter
public class LikedDto {

    @JsonProperty("likedName")
    private String likedName;
    private Double dist;
    private String address;
    private String center;
    private Industry industry;

    public LikedDto(Liked liked) {
        this.likedName = liked.getLikedName();
        this.dist = liked.getDist();
        this.address = liked.getAddress();
        this.center = liked.getCenter();
        this.industry = liked.getIndustry();
    }
}

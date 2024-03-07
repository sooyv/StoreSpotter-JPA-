package com.sojoo.StoreSpotter.dto.mypage;

import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import com.sojoo.StoreSpotter.entity.myPage.Liked;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LikedDto {

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

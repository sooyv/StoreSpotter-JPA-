package com.sojoo.StoreSpotter.dto.mypage;

import com.sojoo.StoreSpotter.entity.mypage.Liked;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikedRequest {
    private String likedName;
    private Double dist;
    private String address;
    private String center;
    private String industry;

}

package com.sojoo.StoreSpotter.entity.mypage;

import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.entity.apiToDb.Industry;
import com.sojoo.StoreSpotter.entity.apiToDb.Region;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "liked")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Liked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liked_id")
    private Long likedId;

    @Column(name = "liked_name", unique = true)
    private String likedName;

    @Column(name = "dist", nullable = false)
    private Double dist;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "center", nullable = false)
    private String center;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indust_id")
    private Industry industry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Liked(String likedName, Double dist, String address, String center, Industry industry, Region region, User user){
        this.likedName = likedName;
        this.dist = dist;
        this.address = address;
        this.center = center;
        this.industry = industry;
        this.region = region;
        this.user = user;
        user.getLikedList().add(this);
    }

    public void UpdateLikedName(String likedName){
        this.likedName = likedName;
    }

}

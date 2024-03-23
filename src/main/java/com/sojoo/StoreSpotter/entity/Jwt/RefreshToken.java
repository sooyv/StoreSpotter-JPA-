package com.sojoo.StoreSpotter.entity.Jwt;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.awt.image.LookupOp;
import java.sql.Ref;
import java.util.logging.LoggingPermission;

/**
 * 사용 여부 확인 후 삭제
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long user_id;
    private String refresh_token;

    public RefreshToken(Long user_id, String refresh_token) {
        this.user_id = user_id;
        this.refresh_token = refresh_token;
    }
}

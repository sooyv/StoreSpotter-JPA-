package com.sojoo.StoreSpotter.entity.apiToDb;

import com.sojoo.StoreSpotter.entity.storePair.ConveniencePair;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Entity
@Table(name = "convenience_store")
@SuperBuilder
@NoArgsConstructor()
public class ConvenienceStore extends StoreInfo {
    // ConvenienceStore에 특화된 속성들을 추가


}
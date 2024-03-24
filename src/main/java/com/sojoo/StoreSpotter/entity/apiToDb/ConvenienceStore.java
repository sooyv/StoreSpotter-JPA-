package com.sojoo.StoreSpotter.entity.apiToDb;

import com.sojoo.StoreSpotter.entity.storePair.ConveniencePair;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;

@Entity
@Table(name = "convenience_store")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConvenienceStore extends StoreInfo {


}
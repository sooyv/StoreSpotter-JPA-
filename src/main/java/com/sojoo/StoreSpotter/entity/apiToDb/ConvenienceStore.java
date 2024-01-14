package com.sojoo.StoreSpotter.entity.apiToDb;

import com.sojoo.StoreSpotter.entity.storePair.ConveniencePair;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "convenience_store")
public class ConvenienceStore extends StoreInfo {
    // ConvenienceStore에 특화된 속성들을 추가

}
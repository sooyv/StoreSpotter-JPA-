package com.sojoo.StoreSpotter.entity.apiToDb;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "convenience_store")
public class ConvenienceStore extends StoreInfo {
    // ConvenienceStore에 특화된 속성들을 추가
}
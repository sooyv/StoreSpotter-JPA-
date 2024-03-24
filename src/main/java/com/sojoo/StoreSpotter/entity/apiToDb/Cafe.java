package com.sojoo.StoreSpotter.entity.apiToDb;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cafe")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe extends StoreInfo{


}

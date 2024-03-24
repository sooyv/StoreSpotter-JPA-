package com.sojoo.StoreSpotter.entity.storePair;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cafe_pair")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CafePair extends PairData{
}

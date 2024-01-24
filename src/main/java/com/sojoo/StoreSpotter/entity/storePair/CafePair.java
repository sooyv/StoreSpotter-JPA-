package com.sojoo.StoreSpotter.entity.storePair;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cafe_pair")
<<<<<<< HEAD
public class CafePair extends PairData {
=======
@SuperBuilder
@NoArgsConstructor()
public class CafePair extends PairData{
>>>>>>> 0ffc609f808c3114ee9584847f628cd49f20ef61
}

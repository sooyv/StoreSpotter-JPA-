package com.sojoo.StoreSpotter.entity.storePair;

import com.sojoo.StoreSpotter.entity.apiToDb.ConvenienceStore;
import com.sojoo.StoreSpotter.entity.apiToDb.StoreInfo;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "convenience_pair")
@SuperBuilder
@NoArgsConstructor
public class ConveniencePair extends PairData {


}

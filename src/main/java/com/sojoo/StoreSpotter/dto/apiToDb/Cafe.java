package com.sojoo.StoreSpotter.dto.apiToDb;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cafe")
public class Cafe extends StoreInfo{
}

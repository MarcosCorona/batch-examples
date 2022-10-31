package com.bosonit.itemReaders.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Integer productID;
    private String productName;
    private String productDesc;
    private BigDecimal price;
    private Integer unit;
}

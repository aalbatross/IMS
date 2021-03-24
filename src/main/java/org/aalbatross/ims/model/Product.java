package org.aalbatross.ims.model;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
  @Id @GeneratedValue private long partNumber;

  @Column(nullable = false)
  private String partName;

  @Column(nullable = false)
  private String manufacturer;

  @Column(nullable = false)
  private double cost;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ProductCategory productCategory;
}

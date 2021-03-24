package org.aalbatross.ims.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
public class ImportProductIssue {
  private long lineNo;
  private Product product;
  private String issue;
}

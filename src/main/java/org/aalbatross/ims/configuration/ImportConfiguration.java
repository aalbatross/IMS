package org.aalbatross.ims.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "import.file")
@Getter
@Setter
public class ImportConfiguration {
  private String uploadStore;
}

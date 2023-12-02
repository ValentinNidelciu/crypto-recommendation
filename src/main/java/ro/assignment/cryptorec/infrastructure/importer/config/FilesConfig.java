package ro.assignment.cryptorec.infrastructure.importer.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("cryptorec")
@Getter
@Setter
public class FilesConfig {
    private List<String> filesLocation;
}

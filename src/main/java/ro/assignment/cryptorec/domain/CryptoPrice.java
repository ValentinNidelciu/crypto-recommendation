package ro.assignment.cryptorec.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@Getter
public class CryptoPrice {
    @Id
    private String id;
    private String symbol;
    private Double price;
    private Long timestamp;
}

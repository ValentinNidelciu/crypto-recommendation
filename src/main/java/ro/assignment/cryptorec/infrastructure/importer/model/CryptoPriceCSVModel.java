package ro.assignment.cryptorec.infrastructure.importer.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import ro.assignment.cryptorec.domain.CryptoPrice;

import java.util.List;
import java.util.UUID;

@Getter
public class CryptoPriceCSVModel {
    @CsvBindByName(column = "timestamp")
    private String timestamp;

    @CsvBindByName(column = "symbol")
    private String symbol;

    @CsvBindByName(column = "price")
    private Double price;


    public static List<CryptoPrice> toDomain(final List<CryptoPriceCSVModel> csvModels) {
        return csvModels
                .stream()
                .map(csvModel -> new CryptoPrice(
                        UUID.randomUUID().toString(),
                        csvModel.getSymbol(),
                        csvModel.getPrice(),
                        Long.valueOf(csvModel.getTimestamp())
                ))
                .toList();
    }
}

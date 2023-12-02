package ro.assignment.cryptorec.exposition.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ro.assignment.cryptorec.domain.CryptoPrice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CryptoPriceView {
    private String id;
    private String symbol;
    private Double price;
    private LocalDateTime time;


    public static List<CryptoPriceView> toView(final List<CryptoPrice> cryptoPrices) {
        return cryptoPrices
                .stream()
                .map(CryptoPriceView::toView)
                .collect(Collectors.toList());
    }

    public static CryptoPriceView toView(final CryptoPrice cryptoPrice) {
        return new CryptoPriceView(
                cryptoPrice.getId(),
                cryptoPrice.getSymbol(),
                cryptoPrice.getPrice(),
                LocalDateTime.ofInstant(Instant.ofEpochMilli(cryptoPrice.getTimestamp()), ZoneId.systemDefault())
        );
    }
}

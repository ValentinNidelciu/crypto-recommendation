package ro.assignment.cryptorec.exposition.views;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ro.assignment.cryptorec.domain.CryptoPrice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
        if(Objects.nonNull(cryptoPrice)) {
            return new CryptoPriceView(
                    cryptoPrice.getId(),
                    cryptoPrice.getSymbol(),
                    cryptoPrice.getPrice(),
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(cryptoPrice.getTimestamp()), ZoneId.systemDefault())
            );
        }
        return new CryptoPriceView();
    }
}

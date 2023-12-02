package ro.assignment.cryptorec.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import ro.assignment.cryptorec.domain.CryptoPrice;

import java.util.List;

public interface CryptoPriceRepository extends MongoRepository<CryptoPrice, String> {
    CryptoPrice findTopBySymbolOrderByTimestampAsc(final String symbol);

    CryptoPrice findTopBySymbolOrderByTimestampDesc(final String symbol);

    CryptoPrice findTopBySymbolOrderByPriceAsc(final String symbol);

    CryptoPrice findTopBySymbolOrderByPriceDesc(final String symbol);

    List<CryptoPrice> findByTimestampBetween(final Long startOfDayInEpochMillis, final Long endOfDayInEpochMillis);
}

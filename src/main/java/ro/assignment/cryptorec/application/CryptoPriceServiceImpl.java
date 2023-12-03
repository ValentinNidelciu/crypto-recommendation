package ro.assignment.cryptorec.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ro.assignment.cryptorec.application.exception.ComputeNormalizedRangeException;
import ro.assignment.cryptorec.application.exception.DataNotAvailableException;
import ro.assignment.cryptorec.domain.CryptoPrice;
import ro.assignment.cryptorec.exposition.views.HighestNormalizedRangeView;
import ro.assignment.cryptorec.exposition.views.NormalizedRangeView;
import ro.assignment.cryptorec.infrastructure.importer.CryptoPriceImporter;
import ro.assignment.cryptorec.infrastructure.persistence.CryptoPriceRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CryptoPriceServiceImpl implements CryptoPriceService {
    private CryptoPriceImporter cryptoPriceImporter;
    private CryptoPriceRepository cryptoPriceRepository;

    public void importPrices() {
        this.cryptoPriceImporter.importPrices();
    }

    public List<CryptoPrice> getAll() {
        return this.cryptoPriceRepository.findAll();
    }

    public CryptoPrice getOldestPrice(final String symbol) {
        final CryptoPrice result = this.cryptoPriceRepository.findTopBySymbolOrderByTimestampAsc(symbol);
        this.checkCryptoPriceResult(result);

        return result;
    }

    public CryptoPrice getNewestPrice(final String symbol) {
        final CryptoPrice result = this.cryptoPriceRepository.findTopBySymbolOrderByTimestampDesc(symbol);
        this.checkCryptoPriceResult(result);

        return result;
    }

    public CryptoPrice getMinPrice(final String symbol) {
        final CryptoPrice result = this.cryptoPriceRepository.findTopBySymbolOrderByPriceAsc(symbol);
        this.checkCryptoPriceResult(result);

        return result;
    }

    public CryptoPrice getMaxPrice(final String symbol) {
        final CryptoPrice result = this.cryptoPriceRepository.findTopBySymbolOrderByPriceDesc(symbol);
        this.checkCryptoPriceResult(result);

        return result;
    }


    public List<NormalizedRangeView> getAllSortedDescendingByNormalizedRange() {
        final List<CryptoPrice> allPrices = this.getAll();

        final Map<String, Double> normalizedRangeBySymbol = this.computeNormalizedRanges(allPrices);

        return normalizedRangeBySymbol.entrySet()
                .stream()
                .map(entry -> new NormalizedRangeView(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingDouble(NormalizedRangeView::getNormalizedRange).reversed())
                .toList();
    }

    public HighestNormalizedRangeView getHighestNormalizedRangeForDate(final LocalDate date) {
        final Long startOfDayInMilliseconds = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
        final Long endOfDayInMilliseconds = date.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        final List<CryptoPrice> pricesForDate = this.cryptoPriceRepository.findByTimestampBetween(startOfDayInMilliseconds, endOfDayInMilliseconds);
        final Map<String, Double> normalizedRangeBySymbol = this.computeNormalizedRanges(pricesForDate);

        final Map.Entry<String, Double> maxEntry = normalizedRangeBySymbol.entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .orElseThrow(ComputeNormalizedRangeException::new);

        return new HighestNormalizedRangeView(maxEntry.getKey(), maxEntry.getValue(), date);
    }

    private Map<String, Double> computeNormalizedRanges(final List<CryptoPrice> prices) {
        final Map<String, Double> normalizedPriceBySymbol = new HashMap<>();

        if(Objects.isNull(prices)) {
            throw new ComputeNormalizedRangeException();
        }

        prices.stream()
                .collect(Collectors.groupingBy(CryptoPrice::getSymbol))
                .forEach((symbol, listOfPrices) -> {
                    final Double minPrice = Collections.min(listOfPrices, Comparator.comparingDouble(CryptoPrice::getPrice)).getPrice();
                    final Double maxPrice = Collections.max(listOfPrices, Comparator.comparingDouble(CryptoPrice::getPrice)).getPrice();
                    final Double normalizedRange = (maxPrice - minPrice) / minPrice;
                    normalizedPriceBySymbol.put(symbol, normalizedRange);
                });

        return normalizedPriceBySymbol;
    }

    private void checkCryptoPriceResult(final CryptoPrice cryptoPrice) {
        if(Objects.isNull(cryptoPrice)) {
            throw new DataNotAvailableException();
        }
    }

}

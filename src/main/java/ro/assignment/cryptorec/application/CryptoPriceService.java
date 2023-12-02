package ro.assignment.cryptorec.application;

import ro.assignment.cryptorec.domain.CryptoPrice;
import ro.assignment.cryptorec.exposition.views.HighestNormalizedRangeView;
import ro.assignment.cryptorec.exposition.views.NormalizedRangeView;

import java.time.LocalDate;
import java.util.List;

public interface CryptoPriceService {

    /**
     *
     * This method calls the CryptoPriceImporter's importPrices() method
     *
     */
    void importPrices();


    /**
     * This method will fetch the list of all price entries.
     *
     * @return a list of CryptoPrice objects
     */
    List<CryptoPrice> getAll();

    /**
     * This method will fetch the oldest price entry for a specific symbol.
     *
     * @param symbol - represents the cryptocurrency's symbol
     *
     * @return a CryptoPrice object
     */
    CryptoPrice getOldestPrice(final String symbol);

    /**
     * This method will fetch the newest price entry for a specific symbol.
     *
     * @param symbol - represents the cryptocurrency's symbol
     *
     * @return a CryptoPrice object
     */
    CryptoPrice getNewestPrice(final String symbol);

    /**
     * This method will fetch the minimum price entry for a specific symbol.
     *
     * @param symbol - represents the cryptocurrency's symbol
     *
     * @return a CryptoPrice object
     */
    CryptoPrice getMinPrice(final String symbol);

    /**
     * This method will fetch the maximum price entry for a specific symbol.
     *
     * @param symbol - represents the cryptocurrency's symbol
     *
     * @return a CryptoPrice object
     */
    CryptoPrice getMaxPrice(final String symbol);

    /**
     * This method will fetch the list of all normalized range price entries for all symbols sorted descending by the
     * normalized range.
     *
     * @return a NormalizedRangeView object
     */
    List<NormalizedRangeView> getAllSortedDescendingByNormalizedRange();


    /**
     * This method will fetch the highest normalized range for a specific date. All symbols are included for this query.
     *
     * @param date - The date when the query should be applied.
     *
     * @return a HighestNormalizedRangeView object
     */
    HighestNormalizedRangeView getHighestNormalizedRangeForDate(final LocalDate date);
}

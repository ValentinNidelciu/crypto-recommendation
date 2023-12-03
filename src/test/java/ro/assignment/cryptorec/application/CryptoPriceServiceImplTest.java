package ro.assignment.cryptorec.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.assignment.cryptorec.application.exception.ComputeNormalizedRangeException;
import ro.assignment.cryptorec.application.exception.DataNotAvailableException;
import ro.assignment.cryptorec.domain.CryptoPrice;
import ro.assignment.cryptorec.exposition.views.HighestNormalizedRangeView;
import ro.assignment.cryptorec.exposition.views.NormalizedRangeView;
import ro.assignment.cryptorec.infrastructure.importer.CryptoPriceImporter;
import ro.assignment.cryptorec.infrastructure.persistence.CryptoPriceRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoPriceServiceImplTest {

    @Mock
    private CryptoPriceImporter cryptoPriceImporter;

    @Mock
    private CryptoPriceRepository cryptoPriceRepository;

    @InjectMocks
    private CryptoPriceServiceImpl cryptoPriceService;

    private final List<CryptoPrice> cryptoPrices = List.of(
            new CryptoPrice("random_id", "BTC", 20000.02, Instant.now().toEpochMilli() - 1000),
            new CryptoPrice("random_id-2", "XRP", 15.32, Instant.now().toEpochMilli()  - 1200),
            new CryptoPrice("random_id-3", "ETH", 4201.32, Instant.now().toEpochMilli()  - 1400),
            new CryptoPrice("random_id-4", "BTC", 22000.05, Instant.now().toEpochMilli()  - 1600),
            new CryptoPrice("random_id-5", "ETH", 4301.32, Instant.now().toEpochMilli()  - 1800),
            new CryptoPrice("random_id-5", "XRP", 17.35, Instant.now().toEpochMilli()  - 2000)
    );


    @Test
    void whenImportingPrices_thenCryptoPriceImporterMustBeCalledOneTime() {
        assertDoesNotThrow(() -> this.cryptoPriceService.importPrices());
        verify(this.cryptoPriceImporter, times(1)).importPrices();
    }

    @Test
    void givenASetOfCryptoPrices_whenTryingToFetchAll_thenAllResultsMustBeReturned() {
        when(this.cryptoPriceRepository.findAll()).thenReturn(this.cryptoPrices);

        assertEquals(this.cryptoPrices, this.cryptoPriceService.getAll());
    }

    @Test
    void givenASetOfCryptoPrices_whenTryingToFetchTheOldestPriceOfASymbol_thenTheOldestPriceMustBeReturned() {
        final CryptoPrice oldestPrice = new CryptoPrice("random_id", "BTC", 25000.12, Instant.now().toEpochMilli() - 1000);

        when(this.cryptoPriceRepository.findTopBySymbolOrderByTimestampAsc(Mockito.anyString())).thenReturn(oldestPrice);

        assertEquals(this.cryptoPriceService.getOldestPrice("BTC"), oldestPrice);
    }

    @Test
    void givenNoPriceEntriesForASpecificSymbol_whenTryingToFetchTheOldestPrice_thenAnExceptionMustBeReceived() {
        final CryptoPrice nullPrice = null;

        when(this.cryptoPriceRepository.findTopBySymbolOrderByTimestampAsc(Mockito.anyString())).thenReturn(nullPrice);

        assertThrows(DataNotAvailableException.class, () -> this.cryptoPriceService.getOldestPrice("BTC"));
    }

    @Test
    void givenASetOfCryptoPrices_whenTryingToFetchTheNewestPriceOfASymbol_thenTheNewestPriceMustBeReturned() {
        final CryptoPrice newestPrice = new CryptoPrice("random_id", "XRP", 25200.12, Instant.now().toEpochMilli());

        when(this.cryptoPriceRepository.findTopBySymbolOrderByTimestampDesc(Mockito.anyString())).thenReturn(newestPrice);

        assertEquals(this.cryptoPriceService.getNewestPrice("XRP"), newestPrice);
    }

    @Test
    void givenNoPriceEntriesForASpecificSymbol_whenTryingToFetchTheNewestPrice_thenAnExceptionMustBeReceived() {
        final CryptoPrice nullPrice = null;

        when(this.cryptoPriceRepository.findTopBySymbolOrderByTimestampDesc(Mockito.anyString())).thenReturn(nullPrice);

        assertThrows(DataNotAvailableException.class, () -> this.cryptoPriceService.getNewestPrice("BTC"));
    }


    @Test
    void givenASetOfCryptoPrices_whenTryingToFetchTheMinimumPriceOfASymbol_thenTheMinimumPriceMustBeReturned() {
        final CryptoPrice minPrice = new CryptoPrice("random_id", "DOGE", 1.5, Instant.now().toEpochMilli());

        when(this.cryptoPriceRepository.findTopBySymbolOrderByPriceAsc(Mockito.anyString())).thenReturn(minPrice);

        assertEquals(this.cryptoPriceService.getMinPrice("XRP"), minPrice);
    }

    @Test
    void givenNoPriceEntriesForASpecificSymbol_whenTryingToFetchTheMinimumPrice_thenAnExceptionMustBeReceived() {
        final CryptoPrice nullPrice = null;

        when(this.cryptoPriceRepository.findTopBySymbolOrderByPriceAsc(Mockito.anyString())).thenReturn(nullPrice);

        assertThrows(DataNotAvailableException.class, () -> this.cryptoPriceService.getMinPrice("BTC"));
    }


    @Test
    void givenASetOfCryptoPrices_whenTryingToFetchTheMaximumPriceOfASymbol_thenTheMaximumPriceMustBeReturned() {
        final CryptoPrice maxPrice = new CryptoPrice("random_id", "BTC", 50000.2, Instant.now().toEpochMilli());

        when(this.cryptoPriceRepository.findTopBySymbolOrderByPriceDesc(Mockito.anyString())).thenReturn(maxPrice);

        assertEquals(this.cryptoPriceService.getMaxPrice("XRP"), maxPrice);
    }

    @Test
    void givenNoPriceEntriesForASpecificSymbol_whenTryingToFetchTheMaximumPrice_thenAnExceptionMustBeReceived() {
        final CryptoPrice nullPrice = null;

        when(this.cryptoPriceRepository.findTopBySymbolOrderByPriceDesc(Mockito.anyString())).thenReturn(nullPrice);

        assertThrows(DataNotAvailableException.class, () -> this.cryptoPriceService.getMaxPrice("BTC"));
    }

    @Test
    void givenASetOfCryptoPrices_whenTryingToFetchAllEntriesSortedDescendingByNormalizedRange_thenTheCorrectListMustBeReceived() {
        final List<NormalizedRangeView> expectedResult = List.of(
                new NormalizedRangeView("XRP", 0.1325),
                new NormalizedRangeView("BTC", 0.1000),
                new NormalizedRangeView("ETH", 0.0238)
        );

        when(this.cryptoPriceRepository.findAll()).thenReturn(this.cryptoPrices);

        final List<NormalizedRangeView> actualResult = this.cryptoPriceService.getAllSortedDescendingByNormalizedRange();
        assertEquals(expectedResult.size(), actualResult.size());

        // Checking if the result is the same up to 4 decimal places
        assertTrue(
                Math.abs(expectedResult.get(0).getNormalizedRange() - actualResult.get(0).getNormalizedRange()) < 0.0001
        );
        assertTrue(
                Math.abs(expectedResult.get(1).getNormalizedRange() - actualResult.get(1).getNormalizedRange()) < 0.0001
        );
        assertTrue(
                Math.abs(expectedResult.get(2).getNormalizedRange() - actualResult.get(2).getNormalizedRange()) < 0.0001
        );
    }

    @Test
    void givenASetOfCryptoPricesThatIncludesZero_whenTryingToComputeTheNormalizedRange_thenAnExceptionMustBeReceived() {
        final List<CryptoPrice> cryptoPricesThatIncludesZero = List.of(
                new CryptoPrice("random_id", "BTC", 20000.02, Instant.now().toEpochMilli() - 1000),
                new CryptoPrice("random_id-2", "XRP", 15.32, Instant.now().toEpochMilli()  - 1200),
                new CryptoPrice("random_id-3", "DOGE", 0.0, Instant.now().toEpochMilli()  - 1400)
        );

        when(this.cryptoPriceRepository.findByTimestampBetween(Mockito.anyLong(), Mockito.anyLong())).thenReturn(cryptoPricesThatIncludesZero);

        assertThrows(ComputeNormalizedRangeException.class, () -> this.cryptoPriceService.getHighestNormalizedRangeForDate(LocalDate.now()));
    }

    @Test
    void givenNoPricesData_whenTryingToFetchAllEntriesSortedDescendingByNormalizedRange_thenAnExceptionMustBeReceived() {
        when(this.cryptoPriceRepository.findAll()).thenReturn(null);
        assertThrows(ComputeNormalizedRangeException.class, () -> this.cryptoPriceService.getAllSortedDescendingByNormalizedRange());
    }


    @Test
    void givenASetOfCryptoPrices_whenTryingToFetchTheHighestNormalizedRangeForASpecificDay_thenTheCorrectAnswerMustBeReceived() {
        final LocalDate now = LocalDate.now();
        final HighestNormalizedRangeView expectedResult = new HighestNormalizedRangeView("XRP", 0.1325, now);

        when(this.cryptoPriceRepository.findByTimestampBetween(Mockito.anyLong(), Mockito.anyLong())).thenReturn(this.cryptoPrices);

        final HighestNormalizedRangeView actualResult = this.cryptoPriceService.getHighestNormalizedRangeForDate(now);

        assertEquals(expectedResult.getSymbol(), actualResult.getSymbol());
        assertEquals(expectedResult.getDate(), actualResult.getDate());
        assertTrue(Math.abs(expectedResult.getHighestNormalizedRange() - actualResult.getHighestNormalizedRange()) < 0.0001);
    }


    @Test
    void givenNoPricesData_whenTryingToFetchTheHighestNormalizedRangeForASpecificDay_thenAnExceptionMustBeReceived() {
        when(this.cryptoPriceRepository.findByTimestampBetween(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);

        assertThrows(ComputeNormalizedRangeException.class, () -> this.cryptoPriceService.getHighestNormalizedRangeForDate(LocalDate.now()));
    }




}
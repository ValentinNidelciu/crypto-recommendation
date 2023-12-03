package ro.assignment.cryptorec.infrastructure.importer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.assignment.cryptorec.infrastructure.exceptions.PriceImportException;
import ro.assignment.cryptorec.infrastructure.importer.config.FilesConfig;
import ro.assignment.cryptorec.infrastructure.persistence.CryptoPriceRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoPriceCSVImporterTest {

    @Mock
    private CryptoPriceRepository cryptoPriceRepository;

    @Mock
    private FilesConfig filesConfig;

    @InjectMocks
    private CryptoPriceCSVImporter cryptoPriceCSVImporter;

    @Test
    void whenImportPricesMethodIsCalled_thenAllExistingPricesMustBeDeletedBeforehand() {
        assertDoesNotThrow(() -> this.cryptoPriceCSVImporter.importPrices());

        verify(this.cryptoPriceRepository, times(1)).deleteAll();
    }

    @Test
    void whenImportPricesMethodIsCalled_thenCryptoPriceRepositorySaveMethodMustBeCalledOnceForEveryDataFile() {
        final List<String> filesLocation = List.of(
                "static/prices/BTC_values.csv",
                "static/prices/DOGE_values.csv",
                "static/prices/ETH_values.csv",
                "static/prices/LTC_values.csv",
                "static/prices/XRP_values.csv"
        );

        final int noOfDataFiles = filesLocation.size();
        when(this.filesConfig.getFilesLocation()).thenReturn(filesLocation);

        assertDoesNotThrow(() -> this.cryptoPriceCSVImporter.importPrices());
        verify(this.cryptoPriceRepository, times(noOfDataFiles)).saveAll(Mockito.any());
    }

    @Test
    void givenAnInvalidFileLocation_whenTryingToImport_thenAnExceptionMustBeReceived() {
        final List<String> filesLocation = List.of(
                "static/invalid_path.csv"
        );
        when(this.filesConfig.getFilesLocation()).thenReturn(filesLocation);

        assertThrows(PriceImportException.class, () -> this.cryptoPriceCSVImporter.importPrices());
    }
}
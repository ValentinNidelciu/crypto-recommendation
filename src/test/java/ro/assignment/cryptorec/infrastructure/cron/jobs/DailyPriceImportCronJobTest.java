package ro.assignment.cryptorec.infrastructure.cron.jobs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.assignment.cryptorec.infrastructure.importer.CryptoPriceImporter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class DailyPriceImportCronJobTest {
    @Mock
    private CryptoPriceImporter cryptoPriceImporter;

    @InjectMocks
    private DailyPriceImportCronJob dailyPriceImportCronJob;

    @Test
    void whenTheScheduledMethodIsTriggered_thenCryptoPriceImporterMustBeCalledOneTime() {
        assertDoesNotThrow(() -> this.dailyPriceImportCronJob.reimportPrices());
        verify(this.cryptoPriceImporter, times(1)).importPrices();
    }
}
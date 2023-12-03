package ro.assignment.cryptorec.infrastructure.cron.jobs;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.assignment.cryptorec.infrastructure.importer.CryptoPriceImporter;

@Component
@AllArgsConstructor
public class DailyPriceImportCronJob {
    private CryptoPriceImporter cryptoPriceImporter;

    @Scheduled(cron = "@daily")
    public void reimportPrices() {
        this.cryptoPriceImporter.importPrices();
    }

}

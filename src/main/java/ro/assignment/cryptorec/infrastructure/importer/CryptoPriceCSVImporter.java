package ro.assignment.cryptorec.infrastructure.importer;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ro.assignment.cryptorec.domain.CryptoPrice;
import ro.assignment.cryptorec.infrastructure.exceptions.PriceImportException;
import ro.assignment.cryptorec.infrastructure.importer.config.FilesConfig;
import ro.assignment.cryptorec.infrastructure.importer.model.CryptoPriceCSVModel;
import ro.assignment.cryptorec.infrastructure.persistence.CryptoPriceRepository;

import java.io.InputStreamReader;
import java.util.List;

@Component
@AllArgsConstructor
public class CryptoPriceCSVImporter implements CryptoPriceImporter {

    private FilesConfig filesConfig;
    private CryptoPriceRepository cryptoPriceRepository;

    @Override
    public void importPrices() {
        this.cryptoPriceRepository.deleteAll();
        this.filesConfig.getFilesLocation().forEach(this::importPrice);
    }

    private void importPrice(final String fileLocation) {
        final ClassPathResource resource = new ClassPathResource(fileLocation);

        try {
            final List<CryptoPriceCSVModel> cryptoPrices = new CsvToBeanBuilder<CryptoPriceCSVModel>(new InputStreamReader(resource.getInputStream()))
                    .withType(CryptoPriceCSVModel.class)
                    .build()
                    .parse();
            this.persistPrices(cryptoPrices);
        } catch (final Exception ex) {
            ex.printStackTrace();
            throw new PriceImportException(ex.getMessage());
        }
    }

    private void persistPrices(final List<CryptoPriceCSVModel> cryptoPriceCSVModels) {
        final List<CryptoPrice> cryptoPrices = CryptoPriceCSVModel.toDomain(cryptoPriceCSVModels);
        this.cryptoPriceRepository.saveAll(cryptoPrices);
    }
}

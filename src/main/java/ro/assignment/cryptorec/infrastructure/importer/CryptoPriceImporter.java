package ro.assignment.cryptorec.infrastructure.importer;

public interface CryptoPriceImporter {

    /**
     *
     * This method is used to import the prices from the static .csv files located in the resources/static/prices folder.
     * Whenever this method is called, all existing data is being removed and is replaced by the data defined in the .csv files.
     * If you want to add/ remove data files, this can be done in application.yml under the cryptorec.files-location property.
     * In the future, the application can be extended to also import data with different formats (e.g. JSON, XML etc)
     * This method can be manually triggered by making a http post request to /crypto-prices.
     * This method is being used by a cron job to reimport the data on a daily basis (every day at midnight).
     *
     */
    void importPrices();

}

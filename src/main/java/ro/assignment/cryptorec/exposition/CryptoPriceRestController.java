package ro.assignment.cryptorec.exposition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.assignment.cryptorec.application.CryptoPriceService;
import ro.assignment.cryptorec.exposition.views.CryptoPriceView;
import ro.assignment.cryptorec.exposition.views.HighestNormalizedRangeView;
import ro.assignment.cryptorec.exposition.views.NormalizedRangeView;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/crypto-prices")
public class CryptoPriceRestController {
    private CryptoPriceService cryptoPriceService;


    @Operation(summary = "Import/ Reimport the data from the static .csv files")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The import was successful"),
            @ApiResponse(responseCode = "500", description = "The import could not be realised due to a server error")
    })
    @PostMapping("/import")
    public ResponseEntity<Void> importPrices() {
        this.cryptoPriceService.importPrices();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Fetch all the price entries for all available symbols")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the prices for all available symbols"),
            @ApiResponse(responseCode = "500", description = "A server error has occurred")
    })
    @GetMapping
    public ResponseEntity<List<CryptoPriceView>> getALl() {
        return ResponseEntity.ok(CryptoPriceView.toView(this.cryptoPriceService.getAll()));
    }

    @Operation(summary = "Fetch the oldest price record available for a specific symbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The oldest price was found"),
            @ApiResponse(responseCode = "500", description = "A server error has occurred"),
            @ApiResponse(responseCode = "400", description = "An invalid request param has been received")
    })
    @GetMapping("/oldest")
    public ResponseEntity<CryptoPriceView> getOldestPrice(final @RequestParam("symbol") String symbol) {
        return ResponseEntity.ok(CryptoPriceView.toView(this.cryptoPriceService.getOldestPrice(symbol)));
    }


    @Operation(summary = "Fetch the newest price record available for a specific symbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The newest price was found"),
            @ApiResponse(responseCode = "500", description = "A server error has occurred"),
            @ApiResponse(responseCode = "400", description = "An invalid request param has been received")
    })
    @GetMapping("/newest")
    public ResponseEntity<CryptoPriceView> getNewestPrice(final @RequestParam("symbol") String symbol) {
        return ResponseEntity.ok(CryptoPriceView.toView(this.cryptoPriceService.getNewestPrice(symbol)));
    }


    @Operation(summary = "Fetch the minimum price record available for a specific symbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The minimum price was found"),
            @ApiResponse(responseCode = "500", description = "A server error has occurred"),
            @ApiResponse(responseCode = "400", description = "An invalid request param has been received")
    })
    @GetMapping("/min-price")
    public ResponseEntity<CryptoPriceView> getMinPrice(final @RequestParam("symbol") String symbol) {
        return ResponseEntity.ok(CryptoPriceView.toView(this.cryptoPriceService.getMinPrice(symbol)));
    }


    @Operation(summary = "Fetch the maximum price record available for a specific symbol")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The maximum price was found"),
            @ApiResponse(responseCode = "500", description = "A server error has occurred"),
            @ApiResponse(responseCode = "400", description = "An invalid request param has been received")
    })
    @GetMapping("/max-price")
    public ResponseEntity<CryptoPriceView> getMaxPrice(final @RequestParam("symbol") String symbol) {
        return ResponseEntity.ok(CryptoPriceView.toView(this.cryptoPriceService.getMaxPrice(symbol)));

    }



    @Operation(summary = "Fetch the highest normalized range value for all symbols for a specific date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The highest normalized range value was found for the specific date"),
            @ApiResponse(responseCode = "500", description = "A server error has occurred"),
            @ApiResponse(responseCode = "400", description = "An invalid request param has been received")
    })
    @GetMapping("/highest-normalized-range")
    public ResponseEntity<HighestNormalizedRangeView> getHighestNormalizedRange(final @RequestParam("date") LocalDate date) {
        return ResponseEntity.ok(this.cryptoPriceService.getHighestNormalizedRangeForDate(date));
    }



    @Operation(summary = "Fetch the descending sorted list of normalized range values for all symbols")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The list was computed successfully"),
            @ApiResponse(responseCode = "500", description = "A server error has occurred")
    })
    @GetMapping("/normalized-range-list")
    public ResponseEntity<List<NormalizedRangeView>> getAllSortedDescendingByNormalizedRange() {
        return ResponseEntity.ok(this.cryptoPriceService.getAllSortedDescendingByNormalizedRange());
    }
}

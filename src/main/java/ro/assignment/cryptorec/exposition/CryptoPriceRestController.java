package ro.assignment.cryptorec.exposition;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.assignment.cryptorec.application.CryptoPriceService;
import ro.assignment.cryptorec.domain.CryptoPrice;
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


    @PostMapping("/import")
    public ResponseEntity<Void> importPrices() {
        this.cryptoPriceService.importPrices();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CryptoPriceView>> getALl() {
        return ResponseEntity.ok(CryptoPriceView.toView(this.cryptoPriceService.getAll()));
    }

    @GetMapping("/oldest")
    public ResponseEntity<CryptoPriceView> getOldestPrice(final @RequestParam("symbol") String symbol) {
        return ResponseEntity.ok(CryptoPriceView.toView(this.cryptoPriceService.getOldestPrice(symbol)));
    }

    @GetMapping("/newest")
    public ResponseEntity<CryptoPriceView> getNewestPrice(final @RequestParam("symbol") String symbol) {
        return ResponseEntity.ok(CryptoPriceView.toView(this.cryptoPriceService.getNewestPrice(symbol)));
    }

    @GetMapping("/min-price")
    public ResponseEntity<CryptoPriceView> getMinPrice(final @RequestParam("symbol") String symbol) {
        return ResponseEntity.ok(CryptoPriceView.toView(this.cryptoPriceService.getMinPrice(symbol)));
    }

    @GetMapping("/max-price")
    public ResponseEntity<CryptoPriceView> getMaxPrice(final @RequestParam("symbol") String symbol) {
        return ResponseEntity.ok(CryptoPriceView.toView(this.cryptoPriceService.getMaxPrice(symbol)));

    }

    @GetMapping("/highest-normalized-range")
    public ResponseEntity<HighestNormalizedRangeView> getHighestNormalizedRange(final @RequestParam("date") LocalDate date) {
        return ResponseEntity.ok(this.cryptoPriceService.getHighestNormalizedRangeForDate(date));
    }

    @GetMapping("/normalized-range-list")
    public ResponseEntity<List<NormalizedRangeView>> getAllSortedDescendingByNormalizedRange() {
        return ResponseEntity.ok(this.cryptoPriceService.getAllSortedDescendingByNormalizedRange());
    }
}

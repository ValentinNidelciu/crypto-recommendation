package ro.assignment.cryptorec.exposition.views;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class HighestNormalizedRangeView {
    private String symbol;
    private Double highestNormalizedRange;
    private LocalDate date;
}

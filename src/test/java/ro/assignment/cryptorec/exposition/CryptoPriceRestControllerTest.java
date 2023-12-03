package ro.assignment.cryptorec.exposition;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ro.assignment.cryptorec.application.CryptoPriceService;
import ro.assignment.cryptorec.domain.CryptoPrice;
import ro.assignment.cryptorec.exposition.views.CryptoPriceView;
import ro.assignment.cryptorec.exposition.views.HighestNormalizedRangeView;
import ro.assignment.cryptorec.exposition.views.NormalizedRangeView;
import ro.assignment.cryptorec.infrastructure.exceptions.PriceImportException;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CryptoPriceRestController.class)
class CryptoPriceRestControllerTest {
    @MockBean
    private CryptoPriceService cryptoPriceService;

    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private MockMvc mockMvc;

    private final List<CryptoPrice> cryptoPrices = List.of(
            new CryptoPrice("random_id", "BTC", 20000.02, Instant.now().toEpochMilli() - 1000),
            new CryptoPrice("random_id-2", "XRP", 15.32, Instant.now().toEpochMilli()  - 1200),
            new CryptoPrice("random_id-3", "ETH", 4201.32, Instant.now().toEpochMilli()  - 1400),
            new CryptoPrice("random_id-4", "BTC", 22000.05, Instant.now().toEpochMilli()  - 1600),
            new CryptoPrice("random_id-5", "ETH", 4301.32, Instant.now().toEpochMilli()  - 1800),
            new CryptoPrice("random_id-5", "XRP", 17.35, Instant.now().toEpochMilli()  - 2000)
    );

    @Test
    void whenCallingTheImportPricesEndpoint_thenA200ResponseMustBeReceived() throws Exception {
        this.mockMvc.perform(post("/crypto-prices/import"))
                .andExpect(status().isOk());
    }

    @Test
    void givenAnExceptionThrownByCryptoPriceService_whenCallingTheImportPricesEndpoint_thenA500ResponseMustBeReceived() throws Exception {
        doThrow(PriceImportException.class).when(this.cryptoPriceService).importPrices();

        this.mockMvc.perform(post("/crypto-prices/import"))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void givenADataSetOfCryptoPrices_whenCallingTheGetAllEndpoint_thenA200ResponseWithExpectedDataMustBeReceived() throws Exception {
        when(this.cryptoPriceService.getAll()).thenReturn(this.cryptoPrices);

        final MvcResult result = this.mockMvc.perform(get("/crypto-prices"))
                .andExpect(status().isOk())
                .andReturn();
         final String content = result.getResponse().getContentAsString();
         final List<CryptoPriceView> views = this.mapper.readValue(content, new TypeReference<>() {});

         assertEquals(views.size(), this.cryptoPrices.size());
         assertEquals(views.get(0).getPrice(), this.cryptoPrices.get(0).getPrice());
    }

    @Test
    void givenADataSetOfCryptoPrices_whenCallingTheGetOldestPriceEndpoint_thenA200ResponseWithExpectedDataMustBeReceived() throws Exception {
        when(this.cryptoPriceService.getOldestPrice(Mockito.anyString())).thenReturn(this.cryptoPrices.get(5));

        final MvcResult result = this.mockMvc.perform(get("/crypto-prices/oldest?symbol=XRP"))
                .andExpect(status().isOk())
                .andReturn();

        final String content = result.getResponse().getContentAsString();
        final CryptoPriceView view = this.mapper.readValue(content, CryptoPriceView.class);

        assertEquals(this.cryptoPrices.get(5).getPrice(), view.getPrice());
        assertEquals(this.cryptoPrices.get(5).getSymbol(), view.getSymbol());
    }

    @Test
    void whenCallingTheGetOldestPriceEndpointWithoutSpecifyingASymbol_thenA400ResponseMustBeReceived() throws Exception {
        this.mockMvc.perform(get("/crypto-prices/oldest"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenADataSetOfCryptoPrices_whenCallingTheGetNewestPriceEndpoint_thenA200ResponseWithExpectedDataMustBeReceived() throws Exception {
        when(this.cryptoPriceService.getNewestPrice(Mockito.anyString())).thenReturn(this.cryptoPrices.get(0));

        final MvcResult result = this.mockMvc.perform(get("/crypto-prices/newest?symbol=BTC"))
                .andExpect(status().isOk())
                .andReturn();

        final String content = result.getResponse().getContentAsString();
        final CryptoPriceView view = this.mapper.readValue(content, CryptoPriceView.class);

        assertEquals(this.cryptoPrices.get(0).getPrice(), view.getPrice());
        assertEquals(this.cryptoPrices.get(0).getSymbol(), view.getSymbol());
    }

    @Test
    void whenCallingTheGetNewestPriceEndpointWithoutSpecifyingASymbol_thenA400ResponseMustBeReceived() throws Exception {
        this.mockMvc.perform(get("/crypto-prices/newest"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenADataSetOfCryptoPrices_whenCallingTheGetMinimumPriceEndpoint_thenA200ResponseWithExpectedDataMustBeReceived() throws Exception {
        when(this.cryptoPriceService.getMinPrice(Mockito.anyString())).thenReturn(this.cryptoPrices.get(1));

        final MvcResult result = this.mockMvc.perform(get("/crypto-prices/min-price?symbol=XRP"))
                .andExpect(status().isOk())
                .andReturn();

        final String content = result.getResponse().getContentAsString();
        final CryptoPriceView view = this.mapper.readValue(content, CryptoPriceView.class);

        assertEquals(this.cryptoPrices.get(1).getPrice(), view.getPrice());
        assertEquals(this.cryptoPrices.get(1).getSymbol(), view.getSymbol());
    }

    @Test
    void whenCallingTheGetMinimumPriceEndpointWithoutSpecifyingASymbol_thenA400ResponseMustBeReceived() throws Exception {
        this.mockMvc.perform(get("/crypto-prices/min-price"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void givenADataSetOfCryptoPrices_whenCallingTheGetMaximumPriceEndpoint_thenA200ResponseWithExpectedDataMustBeReceived() throws Exception {
        when(this.cryptoPriceService.getMaxPrice(Mockito.anyString())).thenReturn(this.cryptoPrices.get(3));

        final MvcResult result = this.mockMvc.perform(get("/crypto-prices/max-price?symbol=BTC"))
                .andExpect(status().isOk())
                .andReturn();

        final String content = result.getResponse().getContentAsString();
        final CryptoPriceView view = this.mapper.readValue(content, CryptoPriceView.class);

        assertEquals(this.cryptoPrices.get(3).getPrice(), view.getPrice());
        assertEquals(this.cryptoPrices.get(3).getSymbol(), view.getSymbol());
    }

    @Test
    void whenCallingTheGetMaximumPriceEndpointWithoutSpecifyingASymbol_thenA400ResponseMustBeReceived() throws Exception {
        this.mockMvc.perform(get("/crypto-prices/max-price"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenCallingTheGetHighestNormalizedRangeForASpecificDate_thenA200ResponseWithExpectedDataMustBeReceived() throws Exception {
        final LocalDate date = LocalDate.now();
        final HighestNormalizedRangeView expected = new HighestNormalizedRangeView("BTC", 0.923, date);

        when(this.cryptoPriceService.getHighestNormalizedRangeForDate(Mockito.any())).thenReturn(expected);

        final MvcResult result = this.mockMvc.perform(get("/crypto-prices/highest-normalized-range?date=2023-12-03"))
                .andExpect(status().isOk())
                .andReturn();

        final String content = result.getResponse().getContentAsString();
        final HighestNormalizedRangeView actual = this.mapper.readValue(content, HighestNormalizedRangeView.class);

        assertEquals(expected.getHighestNormalizedRange(), actual.getHighestNormalizedRange());
        assertEquals(expected.getSymbol(), actual.getSymbol());
    }

    @Test
    void whenCallingTheGetHighestNormalizedRangeEndpointWithoutSpecifyingADate_thenA400ResponseMustBeReceived() throws Exception {
        this.mockMvc.perform(get("/crypto-prices/highest-normalized-range"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void whenCallingTheGetAllSortedDescendingByNormalizedRangeEndpoint_thenA200ResponseMustBeReceivedWithExpectedData() throws Exception {
        final List<NormalizedRangeView> expected = List.of(
                new NormalizedRangeView("BTC", 0.9231),
                new NormalizedRangeView("XRP", 0.2231)
        );

        when(this.cryptoPriceService.getAllSortedDescendingByNormalizedRange()).thenReturn(expected);

        final MvcResult result = this.mockMvc.perform(get("/crypto-prices/normalized-range-list"))
                .andExpect(status().isOk())
                .andReturn();

        final String content = result.getResponse().getContentAsString();
        final List<NormalizedRangeView> actual = this.mapper.readValue(content, new TypeReference<>() {});

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.get(0).getNormalizedRange(), actual.get(0).getNormalizedRange());
        assertEquals(expected.get(0).getSymbol(), actual.get(0).getSymbol());
    }


}
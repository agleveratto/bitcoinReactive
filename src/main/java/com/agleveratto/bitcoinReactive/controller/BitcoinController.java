package com.agleveratto.bitcoinReactive.controller;

import com.agleveratto.bitcoinReactive.entities.Bitcoin;
import com.agleveratto.bitcoinReactive.utils.BitcoinUtils;
import com.agleveratto.bitcoinReactive.response.Response;
import com.agleveratto.bitcoinReactive.service.BitcoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Bitcoin Rest controller Api
 */
@RestController
@RequestMapping(value = "v1/api/bitcoinReactive")
public class BitcoinController {

    /* Service of Bitcoin */
    BitcoinService bitcoinService;

    /**
     * Constructor injection dependency
     *
     * @param bitcoinService object
     */
    @Autowired
    public BitcoinController(BitcoinService bitcoinService) {
        this.bitcoinService = bitcoinService;
    }

    /**
     * Endpoint to retrieve a price of bitcoinReactive in date
     *
     * @param createdAt to find bitcoinReactive price
     * @return a Mono Doble with value or Mono Error with a custom exception
     */
    @GetMapping(value = "/priceByDate")
    public Mono<Double> retrievePriceByDate(@RequestParam("createdAt") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                         LocalDateTime createdAt){
        /* Find Bitcoin price given a createdAt value */
        return bitcoinService.retrieveMonoBitcoinFromCreatedAt(createdAt).map(Bitcoin::getLprice);
    }

    /**
     * Endpoint to retrieve average and percentage differential between the average and max value given Flux Bitcoin
     * created between since date and until date
     *
     * @param sinceDate since createdAt value
     * @param untilDate until createdAt value
     * @return a custom Response with required values (average value and percentage differential)
     */
    @GetMapping(value = "/priceBetweenDates")
    public Response retrievePriceBetweenDates(@RequestParam("sinceDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                LocalDateTime sinceDate,
                                              @RequestParam("untilDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                               LocalDateTime untilDate) {
        /* Find Bitcoins prices between two dates */
        Flux<Bitcoin> bitcoinFlux = bitcoinService.retrieveFluxBitcoinFromDates(sinceDate, untilDate);
        /* Use Bitcoin utils */
        BitcoinUtils bitcoinUtils = new BitcoinUtils();
        /* Obtain average value */
        Double averagePrice = bitcoinUtils.getAverageValue(bitcoinFlux);
        /* Obtain max value */
        Double maxPrice = bitcoinUtils.getMaxValue(bitcoinFlux);
        /* Obtain porcentual differencial value */
        Double diff = bitcoinUtils.getDifferentialValue(averagePrice, maxPrice);

        return new Response(averagePrice, String.format("%.2f", diff).concat("%"));
    }
}

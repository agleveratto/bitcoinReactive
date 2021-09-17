package com.agleveratto.bitcoinReactive.utils;

import com.agleveratto.bitcoinReactive.entities.Bitcoin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class BitcoinUtilsTest {

    BitcoinUtils bitcoinUtils = new BitcoinUtils();

    static Bitcoin bitcoin, bitcoin2;

    @BeforeAll
    static void setup(){
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        bitcoin = new Bitcoin();
        bitcoin.setLprice(48165.0);
        bitcoin.setCurr1("BTC");
        bitcoin.setCurr2("USD");
        bitcoin.setCreatedAt(now);

        bitcoin2 = new Bitcoin();
        bitcoin2.setLprice(48165.5);
        bitcoin2.setCurr1("BTC");
        bitcoin2.setCurr2("USD");
        bitcoin2.setCreatedAt(now);
    }

    @Test
    void givenFluxBitcoin_thenReturnAverageValue(){
        Flux<Bitcoin> bitcoinFlux = Flux.just(bitcoin, bitcoin2);
        Double expected = (bitcoin.getLprice() + bitcoin2.getLprice()) / 2;
        Double response = bitcoinUtils.getAverageValue(bitcoinFlux);
        assertThat(response).isEqualTo(expected);
    }

    @Test
    void givenFluxBitcoin_thenReturnMaxValue(){
        Flux<Bitcoin> bitcoinFlux = Flux.just(bitcoin, bitcoin2);
        Double response = bitcoinUtils.getMaxValue(bitcoinFlux);
        assertThat(response).isEqualTo(bitcoin2.getLprice());
    }

    @Test
    void givenAverageValueAndMaxValue_thenReturnPercentageDifferantialValue(){
        Flux<Bitcoin> bitcoinFlux = Flux.just(bitcoin, bitcoin2);
        Double averageValue = bitcoinUtils.getAverageValue(bitcoinFlux);
        Double maxValue = bitcoinUtils.getMaxValue(bitcoinFlux);
        Double expected = ((averageValue * 100 / maxValue) - 100) * -1;
        Double response = bitcoinUtils.getDifferentialValue(averageValue, maxValue);
        assertThat(response).isEqualTo(expected);
    }
}

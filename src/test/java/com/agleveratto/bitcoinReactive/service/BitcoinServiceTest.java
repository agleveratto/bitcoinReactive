package com.agleveratto.bitcoinReactive.service;

import com.agleveratto.bitcoinReactive.entities.Bitcoin;
import com.agleveratto.bitcoinReactive.service.impl.BitcoinServiceImpl;
import com.agleveratto.bitcoinReactive.repository.BitcoinRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BitcoinServiceTest {

    @InjectMocks
    BitcoinServiceImpl bitcoinService;

    @Mock
    BitcoinRepository bitcoinRepository;

    static Bitcoin bitcoin, bitcoin2;
    static LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

    @BeforeAll
    static void setup(){
        bitcoin = new Bitcoin();
        bitcoin.setLprice(48165.0);
        bitcoin.setCurr1("BTC");
        bitcoin.setCurr2("USD");
        bitcoin.setCreatedAt(now);

        bitcoin2 = new Bitcoin();
        bitcoin2.setLprice(48165.0);
        bitcoin2.setCurr1("BTC");
        bitcoin2.setCurr2("USD");
        bitcoin2.setCreatedAt(now);
    }

    @Test
    void givenADate_thenReturnBitcoinPrice(){
        when(bitcoinRepository.findByCreatedAt(now)).thenReturn(Mono.just(bitcoin));
        Mono<Bitcoin> response = bitcoinService.retrieveMonoBitcoinFromCreatedAt(now);
        assertThat(response.block()).isEqualTo(bitcoin);
    }

    @Test
    void givenADate_thenReturnAnError(){
        when(bitcoinRepository.findByCreatedAt(now)).thenReturn(Mono.empty());
        Mono<Bitcoin> response = bitcoinService.retrieveMonoBitcoinFromCreatedAt(now);
        StepVerifier.create(response)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void givenASinceAndUntilCreatedAtDates_thenReturnBitcoinPrice(){
        when(bitcoinRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(LocalDateTime.MIN, LocalDateTime.MAX))
                .thenReturn(Flux.just(bitcoin, bitcoin2));
        Flux<Bitcoin> response = bitcoinService.retrieveFluxBitcoinFromDates(LocalDateTime.MIN, LocalDateTime.MAX);
        assertThat(response.blockFirst()).isEqualTo(bitcoin);
        assertThat(response.blockLast()).isEqualTo(bitcoin2);
    }

    @Test
    void givenASinceAndUntilCreatedAtDates_thenReturnAnError(){
        when(bitcoinRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(LocalDateTime.MIN, LocalDateTime.MAX))
                .thenReturn(Flux.empty());
        Flux<Bitcoin> response = bitcoinService.retrieveFluxBitcoinFromDates(LocalDateTime.MIN, LocalDateTime.MAX);
        StepVerifier.create(response)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}

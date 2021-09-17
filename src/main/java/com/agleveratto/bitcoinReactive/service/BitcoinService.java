package com.agleveratto.bitcoinReactive.service;

import com.agleveratto.bitcoinReactive.entities.Bitcoin;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Service to connect to repository for Bitcoin entity
 */
public interface BitcoinService {
    /**
     * find price of Bitcoin given a date
     * @param createdAt date to created row on DB
     * @return a Mono Bitcoin if exists or a Mono Error with custom exception
     */
    Mono<Bitcoin> retrieveMonoBitcoinFromCreatedAt(LocalDateTime createdAt);

    /**
     * find Flux of Bitcoin given two dates
     * @param sinceDate since createdAt value
     * @param untilDate until createdAt value
     * @return a Flux Bitcoin if exists or a Flux Error with custom exception
     */
    Flux<Bitcoin> retrieveFluxBitcoinFromDates(LocalDateTime sinceDate, LocalDateTime untilDate);
}

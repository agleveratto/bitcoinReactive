package com.agleveratto.bitcoinReactive.service.impl;

import com.agleveratto.bitcoinReactive.entities.Bitcoin;
import com.agleveratto.bitcoinReactive.repository.BitcoinRepository;
import com.agleveratto.bitcoinReactive.service.BitcoinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Implementation of {@link BitcoinService}
 */
@Service
@Slf4j
public class BitcoinServiceImpl implements BitcoinService {

    /* Repository object */
    BitcoinRepository bitcoinRepository;

    /**
     * Constructor injection dependencies
     * @param bitcoinRepository object
     */
    @Autowired
    public BitcoinServiceImpl(BitcoinRepository bitcoinRepository) {
        this.bitcoinRepository = bitcoinRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mono<Bitcoin> retrieveMonoBitcoinFromCreatedAt(LocalDateTime createdAt){
        return bitcoinRepository.findByCreatedAt(createdAt)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Price bitcoinReactive not found for date: " + createdAt)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Flux<Bitcoin> retrieveFluxBitcoinFromDates(LocalDateTime sinceDate, LocalDateTime untilDate) {
        return bitcoinRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(sinceDate, untilDate)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Price bitcoinReactive not found between: " + sinceDate + " and " + untilDate)));
    }
}

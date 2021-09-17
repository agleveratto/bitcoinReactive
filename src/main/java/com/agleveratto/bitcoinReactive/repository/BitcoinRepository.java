package com.agleveratto.bitcoinReactive.repository;

import com.agleveratto.bitcoinReactive.entities.Bitcoin;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Repository for manipulate Bitcoin data from/to DB
 */
@Repository
public interface BitcoinRepository extends ReactiveCrudRepository<Bitcoin, Long> {

    /**
     * Find Bitcoin by Created At
     * @param createdAt to find
     * @return a Mono Bitcoin
     */
    Mono<Bitcoin> findByCreatedAt(LocalDateTime createdAt);

    /**
     * Find Bitcoin list between two dates
     * @param sinceDate since createdAt value
     * @param untilDate until createdAt value
     * @return a list of Bitcoin
     */
    Flux<Bitcoin> findByCreatedAtGreaterThanEqualAndCreatedAtLessThanEqual(LocalDateTime sinceDate, LocalDateTime untilDate);
}

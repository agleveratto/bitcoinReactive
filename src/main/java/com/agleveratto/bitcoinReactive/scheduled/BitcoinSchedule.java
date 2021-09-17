package com.agleveratto.bitcoinReactive.scheduled;

import com.agleveratto.bitcoinReactive.entities.Bitcoin;
import com.agleveratto.bitcoinReactive.repository.BitcoinRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Schedule to obtain a Bitcoin price
 */
@Component
@Slf4j
public class BitcoinSchedule {

    /* repository object */
    BitcoinRepository bitcoinRepository;

    /* gson to format rest template response to entity */
    Gson gson = new Gson();

    /**
     * Constructor to inject object
     * @param bitcoinRepository to autowired
     */
    @Autowired
    public BitcoinSchedule(BitcoinRepository bitcoinRepository) {
        this.bitcoinRepository = bitcoinRepository;
    }

    /**
     * Task to get a bitcoinReactive price every 10 secs
     */
    @Scheduled(fixedRate = 10000)
    public void getBitcoinValue() {
        /* create web client */
        WebClient webClient = WebClient.create();

        /* URL */
        String URI = "https://cex.io/api/last_price/BTC/USD";

        /* call external api to obtain a bitcoinReactive object */
        String bitcoinMono = webClient.get()
                .uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class).block();

        log.info("getting bitcoinReactive value to String: " + bitcoinMono);

        /* get actual time and truncate to secs */
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        /* mapper String response to Bitcoin entity */
        Bitcoin bitcoin = gson.fromJson(bitcoinMono, Bitcoin.class);
        /* set actual time to bitcoinReactive */
        bitcoin.setCreatedAt(now);

        log.info("getting bitcoinReactive value to object: " + bitcoin);

        /*save bitcoinReactive to database*/
        Mono<Object> saved = bitcoinRepository.save(bitcoin).map(btc -> Mono.just(btc));
        saved.subscribe(btc -> log.info("bitcoinReactive saved: " + btc.toString()));
    }
}

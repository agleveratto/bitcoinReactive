package com.agleveratto.bitcoinReactive.scheduled;

import com.agleveratto.bitcoinReactive.BitcoinReactiveApplication;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringJUnitConfig(BitcoinReactiveApplication.class)
public class BitcoinScheduleTest {

    /*inject bean with this annotation to check the number of times that the scheduled method is called */
    @SpyBean
    private BitcoinSchedule bitcoinSchedule;

    @Test
    void whenWaitOneMinute_thenScheduledIsCalledAtLeastSixTimes(){
        await().atMost(Duration.ONE_MINUTE)
                .untilAsserted(() ->
                        verify(bitcoinSchedule, atLeast(6))
                                .getBitcoinValue());
    }
}

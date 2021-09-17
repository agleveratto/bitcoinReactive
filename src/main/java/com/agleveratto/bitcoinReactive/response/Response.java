package com.agleveratto.bitcoinReactive.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private Double averageBitcoinPrice;
    private String diffBitcoinPrice;
}

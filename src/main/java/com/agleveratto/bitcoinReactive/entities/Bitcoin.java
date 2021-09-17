package com.agleveratto.bitcoinReactive.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Bitcoin {
    @Id
    private Long id;
    private Double lprice;
    private String curr1;
    private String curr2;
    private LocalDateTime createdAt;

}
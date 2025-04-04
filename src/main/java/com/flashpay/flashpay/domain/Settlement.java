package com.flashpay.flashpay.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Order order;

    private int amount;

    private LocalDateTime settledAt;

    @PrePersist
    public void onCreate() {
        this.settledAt = LocalDateTime.now();
    }
}
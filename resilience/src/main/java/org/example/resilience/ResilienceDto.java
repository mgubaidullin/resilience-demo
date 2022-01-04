package org.example.resilience;

import java.math.BigDecimal;

public class ResilienceDto {

    private Long clientId;
    private BigDecimal amount;
    private String description;

    public ResilienceDto() {
    }

    public ResilienceDto(Long clientId, BigDecimal amount, String description) {
        this.clientId = clientId;
        this.amount = amount;
        this.description = description;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

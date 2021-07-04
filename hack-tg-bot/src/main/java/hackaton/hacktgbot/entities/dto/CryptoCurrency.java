package hackaton.hacktgbot.entities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import hackaton.hacktgbot.entities.Figis;
import lombok.*;

import java.math.BigDecimal;

@Data
public class CryptoCurrency {
    private String id;
    private Long rank;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Figis symbol;
    private String name;
    private BigDecimal supply;
    private BigDecimal maxSupply;
    private BigDecimal marketCapUsd;
    private BigDecimal volumeUsd24Hr;
    private BigDecimal priceUsd;
    private BigDecimal changePercent24Hr;
    private BigDecimal vwap24Hr;
    private String explorer;
}

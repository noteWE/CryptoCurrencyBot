package hackaton.hacktgbot.entities;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Event {
    private Long triggerId;
    private String chatId;
    private Figis figi;
    private BigDecimal price;
}

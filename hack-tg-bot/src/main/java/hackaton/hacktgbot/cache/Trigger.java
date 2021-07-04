package hackaton.hacktgbot.cache;

import hackaton.hacktgbot.entities.Figis;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Trigger {
    private Figis figi;
    private BigDecimal percent;
}

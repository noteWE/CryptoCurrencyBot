package hackaton.hacktgbot.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity(name = "Triggers")
public class Trigger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long triggerId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String id;
    @Enumerated(EnumType.STRING)
    private Figis figi;
    private BigDecimal currentPrice;
    private BigDecimal targetPrice;
    private Boolean telegramNotification;
}

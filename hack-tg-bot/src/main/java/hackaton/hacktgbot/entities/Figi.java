package hackaton.hacktgbot.entities;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "Figis")
@Data
public class Figi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long figiId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING)
    private Figis figi;
}

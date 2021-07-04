package hackaton.hacktgbot.entities;

import hackaton.hacktgbot.bots.BotState;
import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private Long chatId;
    private Integer streamMessageId;
    private BotState botState;
    @Transient
    private UserData userData;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Figi> figis;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Trigger> triggers;
}

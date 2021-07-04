package hackaton.hacktgbot.cache;

import hackaton.hacktgbot.entities.UserData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long chatId;
    private Integer streamMessageId;
    private UserData userData;
}

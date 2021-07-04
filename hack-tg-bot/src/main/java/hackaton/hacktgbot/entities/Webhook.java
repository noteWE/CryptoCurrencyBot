package hackaton.hacktgbot.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Webhook {
    private String url;

    private Trigger trigger;
    private Event event;
}

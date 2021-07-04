package hackaton.hacktgbot.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserData {
    private List<Figis> figis;
    private List<Trigger> triggers;
}

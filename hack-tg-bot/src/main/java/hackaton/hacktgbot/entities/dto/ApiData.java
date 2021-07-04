package hackaton.hacktgbot.entities.dto;

import lombok.*;

import java.util.List;

@Data
public class ApiData {
    private List<CryptoCurrency> data;
    private Long timestamp;
}

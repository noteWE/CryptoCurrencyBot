package hackaton.hacktgbot.services;

import hackaton.hacktgbot.entities.Figis;
import hackaton.hacktgbot.entities.dto.ApiData;

public interface CoinService {
    ApiData getCurrencyByFigi(Figis figi);
}

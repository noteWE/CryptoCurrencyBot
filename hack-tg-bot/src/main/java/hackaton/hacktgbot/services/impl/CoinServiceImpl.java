package hackaton.hacktgbot.services.impl;

import hackaton.hacktgbot.entities.Figis;
import hackaton.hacktgbot.entities.dto.ApiData;
import hackaton.hacktgbot.services.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CoinServiceImpl implements CoinService {

    private final RestTemplate restTemplate;
    @Value("${coin.service.assets.url}")
    private String assetsApiUrl;

    @Override
    public ApiData getCurrencyByFigi(Figis figi) {

        ResponseEntity<ApiData> response = restTemplate.getForEntity(
                String.format(assetsApiUrl, figi.name()), ApiData.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return response.getBody();
        } else {
            throw new RuntimeException("Не удалось совершить запрос");
        }
    }

}

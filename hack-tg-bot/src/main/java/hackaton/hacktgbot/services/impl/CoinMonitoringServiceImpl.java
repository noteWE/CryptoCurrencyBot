package hackaton.hacktgbot.services.impl;

import hackaton.hacktgbot.cache.User;
import hackaton.hacktgbot.entities.Event;
import hackaton.hacktgbot.entities.Trigger;
import hackaton.hacktgbot.entities.dto.ApiData;
import hackaton.hacktgbot.services.CoinMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@EnableAsync
@Slf4j
public class CoinMonitoringServiceImpl implements CoinMonitoringService {

    @Value("${coin.service.assets.url}")
    private String urlWithParams;
    private String myUrl = "http://localhost:8080/service/update";
    private final RestTemplate template;
    private final MySqlUserService userCacheService;

    @Async
    @Scheduled(fixedDelay = 5000)
    @Override
    public void updateCoinInfo() {
        Set<User> users = userCacheService.getAllUsersTrigger();
        log.info("Users:" + users.size());
        for (User user : users) {
            List<Trigger> userTriggers = userCacheService.getAllUserTriggers(user.getChatId());
            log.info("Triggers: " + userTriggers.size());
            for (Trigger trigger : userTriggers) {
                processTrigger(user.getChatId(), trigger);
            }
        }
    }

    public void processTrigger(Long chatId, Trigger trigger) {
        ResponseEntity<ApiData> data = template.getForEntity(String.format(urlWithParams, trigger.getFigi().name()), ApiData.class);
        if (data.getStatusCode().equals(HttpStatus.OK)) {
            ApiData apiData = data.getBody();
            if (apiData != null) {
                BigDecimal price = apiData.getData()
                        .stream()
                        .findFirst().get()
                        .getPriceUsd();
                log.info("Price" + price + " " + trigger.getTargetPrice());
                if (price.compareTo(trigger.getTargetPrice()) <= 0) {
                    log.info("trigger accepted");
                    log.info("User: " + chatId.toString());
                    template.postForEntity(myUrl, Event.builder()
                            .triggerId(trigger.getTriggerId())
                            .chatId(chatId.toString())
                            .figi(trigger.getFigi())
                            .price(price)
                            .build(), Event.class);
                }
            }
        }
    }
}

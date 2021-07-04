package hackaton.hacktgbot.controllers;

import hackaton.hacktgbot.bots.TriggerUpdatable;
import hackaton.hacktgbot.entities.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/service")
@Slf4j
public class ServiceController {

    private final TriggerUpdatable bot;

    @PostMapping("/update")
    private void getUpdateFromWebhook(@RequestBody Event event) {
        log.info("User accepted: " + event.getChatId());
        bot.triggerUpdate(event);
    }
}

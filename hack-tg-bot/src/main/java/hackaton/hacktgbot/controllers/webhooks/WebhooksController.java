package hackaton.hacktgbot.controllers.webhooks;

import hackaton.hacktgbot.entities.Webhook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class WebhooksController {

    @PostMapping("/set_webhook")
    public ResponseEntity<Webhook> setWebhook(@RequestBody Webhook webhook) {
        return null;
    }

    @DeleteMapping("/remove_webhook")
    public ResponseEntity<String> removeWebhook(@RequestBody Webhook webhook) {
        return null;
    }

    @GetMapping("/urls/{url}")
    public ResponseEntity<Webhook> getWebhookByUrl(@PathVariable(name = "url") String url) {
        return null;
    }
}

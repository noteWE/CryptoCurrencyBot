package hackaton.hacktgbot.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import hackaton.hacktgbot.services.TelegramBotService;

@Service
@Slf4j
public class TelegramBotServiceImpl implements TelegramBotService {

    @Override
    public SendMessage handleUpdate(Update update) {

        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    public SendMessage handleInputMessage(Message message) {
        String trimCommand = message.getText();

        switch (trimCommand) {
            case "/subscribe":
                return SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("Вы хотите подписаться")
                        .build();
            case "/unsub":
                return SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("Вы хотите отписаться")
                        .build();
            case "/trigger":
                return SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text("Вы хотите подписаться на событие")
                        .build();
            default:
                throw new RuntimeException("Неизвестная команда");
        }
    }
}

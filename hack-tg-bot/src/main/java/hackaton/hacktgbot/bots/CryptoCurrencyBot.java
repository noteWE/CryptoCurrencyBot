package hackaton.hacktgbot.bots;

import hackaton.hacktgbot.cache.User;
import hackaton.hacktgbot.entities.UserData;
import hackaton.hacktgbot.entities.Event;
import hackaton.hacktgbot.entities.dto.CryptoCurrency;
import hackaton.hacktgbot.services.CoinService;
import lombok.extern.slf4j.Slf4j;
import hackaton.hacktgbot.objects.comands.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import hackaton.hacktgbot.services.UserCacheService;
import hackaton.hacktgbot.services.impl.UserInMemoryCache;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@EnableAsync
public class CryptoCurrencyBot extends TelegramLongPollingCommandBot implements UpdateSendable, TriggerUpdatable {

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String token;

    private final CoinService service;
    private final UserCacheService userCacheService;
    private final NoneCommand noneCommand;

    public CryptoCurrencyBot(CoinService service, @Qualifier("mySqlUserService") UserCacheService userCacheService) {
        log.info("Bot created");
        this.service = service;
        this.userCacheService = userCacheService;
        this.noneCommand = new NoneCommand(userCacheService, service);

        register(new StartCommand("start", "Старт", userCacheService));
        register(new SubscribeCommand("subscribe", "Подписаться на оповещения о курсе криптовалюты", userCacheService));
        register(new TriggerCommand("trigger", "Отписаться от оповещений", userCacheService));
        register(new UnsubCommand("unsub", "падение курса валюты C от текущего на X%", userCacheService));
        register(new ListsubCommand("listsub", "список подписок", userCacheService));
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {

    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        var message = update.getMessage();

        if (message != null && message.hasText()) {
            String answer = noneCommand.execute(message.getChatId(), message.getText());
            sendAnswer(message.getChatId(), answer);
        }
    }

    public String generateMessageText(CryptoCurrency currency, LocalDateTime time) {
        return String.format("Курс криптовалюты %s к USD: %f\nВремя: %s",
                currency.getSymbol(),
                currency.getPriceUsd(),
                time.format(DateTimeFormatter.ISO_DATE_TIME));
    }

    public void triggerUpdate(Event event) {
        try {
            execute(SendMessage.builder()
                    .chatId(event.getChatId())
                    .text(String.format("Криптовалюта %s упала до %f", event.getFigi().name(),
                            event.getPrice()))
                    .build());
            Long chatId = Long.parseLong(event.getChatId());
            UserData data = userCacheService.getUserData(chatId);
            data.setTriggers(null);
            Integer messageId = userCacheService.getUserMessageId(chatId);
            if (messageId != null) {
                execute(DeleteMessage.builder()
                        .chatId(chatId.toString())
                        .messageId(messageId)
                        .build());
                userCacheService.changeUserMessageId(chatId, null);
            }
            userCacheService.deleteUserTrigger(chatId, event.getTriggerId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Async
    @Scheduled(fixedDelay = 5000)
    public void sendUpdate() {
        var usersToSend = userCacheService.getAllUsersSend();

        for(User user : usersToSend) {
            try {
                var data = service.getCurrencyByFigi(user.getUserData().getFigis().stream().findFirst().get());
                CryptoCurrency currency = data.getData().stream().findFirst().get();
                LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(data.getTimestamp()),
                        ZoneId.systemDefault());
                if (user.getStreamMessageId() == null) {
                    var message = execute(SendMessage.builder()
                            .chatId(user.getChatId().toString())
                            .text(generateMessageText(currency, dateTime))
                            .build());
                    userCacheService.changeUserMessageId(user.getChatId(), message.getMessageId());
                } else {
                    execute(EditMessageText.builder()
                            .chatId(user.getChatId().toString())
                            .messageId(user.getStreamMessageId())
                            .text(generateMessageText(currency, dateTime))
                            .build());
                }
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendAnswer(Long chatId, String answer) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(answer)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotToken() {
        return token;
    }
}

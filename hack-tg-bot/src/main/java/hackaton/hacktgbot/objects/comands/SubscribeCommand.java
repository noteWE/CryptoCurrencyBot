package hackaton.hacktgbot.objects.comands;

import hackaton.hacktgbot.bots.BotState;
import hackaton.hacktgbot.entities.UserData;
import hackaton.hacktgbot.entities.Figis;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import hackaton.hacktgbot.services.UserCacheService;

public class SubscribeCommand extends BotCommand {

    private final UserCacheService userCacheService;

    public SubscribeCommand(String commandIdentifier, String description,
                            UserCacheService userCacheService) {
        super(commandIdentifier, description);
        this.userCacheService = userCacheService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            UserData userData = userCacheService.getUserData(chat.getId());
            Integer messageId = userCacheService.getUserMessageId(chat.getId());
            if (userData != null && userData.getFigis() != null && !userData.getFigis().isEmpty()) {
                absSender.execute(SendMessage.builder()
                        .chatId(chat.getId().toString())
                        .text(String.format("Вы уже подписанны на криптовалюту: %s.\n" +
                                "Отписаться /unsub", userData.getFigis().stream().findFirst().get().name()))
                        .build());
                absSender.execute(DeleteMessage.builder()
                        .chatId(chat.getId().toString())
                        .messageId(messageId)
                        .build());
                userCacheService.changeUserMessageId(chat.getId(), null);
                return;
            }
            absSender.execute(SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .text("Вы хотите следить за изменением курса криптовалюты. " +
                            "Укажите FIGI этой криптовалюты")
                    .build());
            StringBuilder builder = new StringBuilder();
            builder.append("СПИСОК ВАЛЮТ\n");
            for (Figis figi : Figis.values()) {
                builder.append(figi.name() + "\n");
            }
            absSender.execute(SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .text(builder.toString())
                    .build());
            userCacheService.changeUserState(chat.getId(), BotState.ASK_FIGI);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

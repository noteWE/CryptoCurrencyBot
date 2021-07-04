package hackaton.hacktgbot.objects.comands;

import hackaton.hacktgbot.bots.BotState;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import hackaton.hacktgbot.services.UserCacheService;

public class TriggerCommand extends BotCommand {

    private final UserCacheService userCacheService;

    public TriggerCommand(String commandIdentifier, String description,
                          UserCacheService userCacheService) {
        super(commandIdentifier, description);
        this.userCacheService = userCacheService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            Integer messageId = userCacheService.getUserMessageId(chat.getId());
            if (messageId != null) {
                absSender.execute(DeleteMessage.builder()
                        .chatId(chat.getId().toString())
                        .messageId(messageId)
                        .build());
                userCacheService.changeUserMessageId(chat.getId(), null);
            }
            absSender.execute(SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .text("Вы хотите подписать на падение курса криптовалюты. " +
                            "Введите название и процент.")
                    .build());
            userCacheService.changeUserState(chat.getId(), BotState.ASK_TRIGGER);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

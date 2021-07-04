package hackaton.hacktgbot.objects.comands;

import hackaton.hacktgbot.bots.BotState;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import hackaton.hacktgbot.services.UserCacheService;

public class UnsubCommand extends BotCommand {

    private final UserCacheService userCacheService;

    public UnsubCommand(String commandIdentifier, String description,
                        UserCacheService userCacheService) {
        super(commandIdentifier, description);
        this.userCacheService = userCacheService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .text("Вы больше не отслеживаете курс криптовалюты!")
                    .build());
            userCacheService.deleteUserData(chat.getId());
            userCacheService.changeUserState(chat.getId(), BotState.WAITING);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

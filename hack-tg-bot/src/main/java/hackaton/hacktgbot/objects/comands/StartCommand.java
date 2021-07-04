package hackaton.hacktgbot.objects.comands;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import hackaton.hacktgbot.services.UserCacheService;

public class StartCommand extends BotCommand {

    private UserCacheService userCacheService;

    public StartCommand(String commandIdentifier, String description, UserCacheService userCacheService) {
        super(commandIdentifier, description);
        this.userCacheService = userCacheService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            absSender.execute(SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .text(String.format("Привет, %s, функционал нашего бота.", user.getFirstName()))
                    .build());
            absSender.execute(SendMessage.builder()
                    .chatId(chat.getId().toString())
                    .text("/subscribe - Подписаться на оповещения о курсе криптовалюты\n" +
                            "/unsub - Отписаться от оповещений\n" +
                            "/trigger - падение курса валюты C от текущего на X%\n" +
                            "/listsub - список подписок")
                    .build());
            userCacheService.addUser(chat.getId());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

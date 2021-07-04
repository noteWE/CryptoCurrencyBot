package hackaton.hacktgbot.objects.comands;

import hackaton.hacktgbot.entities.Trigger;
import hackaton.hacktgbot.entities.UserData;
import hackaton.hacktgbot.services.UserCacheService;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


public class ListsubCommand extends BotCommand {

    private final UserCacheService service;

    public ListsubCommand(String commandIdentifier, String description, UserCacheService service) {
        super(commandIdentifier, description);
        this.service = service;
    }

    @Override
    @Transactional
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ВАШИ ПОДПИСКИ\n");
            List<Trigger> triggerList = service.getAllUserTriggers(chat.getId());
            if (triggerList != null) {
                for (Trigger trigger : triggerList) {
                    stringBuilder.append(String.format("Вы подписанный на событие: " +
                            "понижение цены %s до %f\n", trigger.getFigi().name(), trigger.getTargetPrice()));
                }
                absSender.execute(SendMessage.builder()
                        .chatId(chat.getId().toString())
                        .text(stringBuilder.toString())
                        .build());
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

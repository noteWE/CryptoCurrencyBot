package hackaton.hacktgbot.services;

import hackaton.hacktgbot.bots.BotState;
import hackaton.hacktgbot.cache.User;
import hackaton.hacktgbot.entities.Trigger;
import hackaton.hacktgbot.entities.UserData;

import java.util.List;
import java.util.Set;

public interface UserCacheService {

    void addUser(Long chatId);

    void changeUserState(Long chatId, BotState state);

    void changeUserMessageId(Long chatId, Integer messageId);

    BotState getUserState(Long chatId);

    Integer getUserMessageId(Long chatId);

    void addUserData(Long chatId, UserData userData);

    void deleteUserData(Long chatId);

    UserData getUserData(Long chatId);

    Set<User> getAllUsersSend();

    Set<User> getAllUsersTrigger();

    void deleteUserTrigger(Long chatId, Long triggerId);

    List<Trigger> getAllUserTriggers(Long id);
}

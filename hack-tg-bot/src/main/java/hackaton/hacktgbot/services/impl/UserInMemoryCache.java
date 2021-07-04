package hackaton.hacktgbot.services.impl;

import hackaton.hacktgbot.bots.BotState;
import hackaton.hacktgbot.cache.User;
import hackaton.hacktgbot.cache.UserCache;
import hackaton.hacktgbot.entities.Trigger;
import hackaton.hacktgbot.entities.UserData;
import hackaton.hacktgbot.services.UserCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserInMemoryCache implements UserCacheService {

    private UserCache userCache = new UserCache();

    @Override
    public void addUser(Long chatId) {
        log.info("saving user");
        userCache.addUser(chatId);
    }

    @Override
    public void changeUserState(Long chatId, BotState state) {
        userCache.changeUserState(chatId, state);
    }

    @Override
    public void changeUserMessageId(Long chatId, Integer messageId) {
        userCache.setUserMessageStream(chatId, messageId);
    }

    @Override
    public BotState getUserState(Long chatId) {
        return userCache.getUserState(chatId);
    }

    @Override
    public Integer getUserMessageId(Long chatId) {
        return userCache.getUserMessageId(chatId);
    }


    @Override
    public void addUserData(Long chatId, UserData userData) {
        userCache.addUserData(chatId,userData);
    }

    @Override
    public void deleteUserData(Long chatId) {
        userCache.deleteUserData(chatId);
    }

    @Override
    public UserData getUserData(Long chatId) {
        return userCache.getUserData(chatId);
    }

    @Override
    public Set<User> getAllUsersSend() {
        return userCache.getAllUserSend();
    }

    @Override
    public Set<User> getAllUsersTrigger() {
        return userCache.getAllUserTrigger();
    }

    @Override
    public void deleteUserTrigger(Long chatId, Long triggerId) {

    }

    @Override
    public List<Trigger> getAllUserTriggers(Long id) {
        return null;
    }


}

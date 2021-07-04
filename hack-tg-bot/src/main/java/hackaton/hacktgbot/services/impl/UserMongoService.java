package hackaton.hacktgbot.services.impl;

import hackaton.hacktgbot.bots.BotState;
import hackaton.hacktgbot.cache.User;
import hackaton.hacktgbot.entities.Trigger;
import hackaton.hacktgbot.entities.UserData;
import hackaton.hacktgbot.services.UserCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/*import org.springframework.data.mongodb.cor.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;*/

import java.util.List;
import java.util.Set;

//@Service
@RequiredArgsConstructor
@Slf4j
public class UserMongoService implements UserCacheService {
    @Override
    public void addUser(Long chatId) {

    }

    @Override
    public void changeUserState(Long chatId, BotState state) {

    }

    @Override
    public void changeUserMessageId(Long chatId, Integer messageId) {

    }

    @Override
    public BotState getUserState(Long chatId) {
        return null;
    }

    @Override
    public Integer getUserMessageId(Long chatId) {
        return null;
    }

    @Override
    public void addUserData(Long chatId, UserData userData) {

    }

    @Override
    public void deleteUserData(Long chatId) {

    }

    @Override
    public UserData getUserData(Long chatId) {
        return null;
    }

    @Override
    public Set<User> getAllUsersSend() {
        return null;
    }

    @Override
    public Set<User> getAllUsersTrigger() {
        return null;
    }

    @Override
    public void deleteUserTrigger(Long chatId, Long triggerId) {

    }

    @Override
    public List<Trigger> getAllUserTriggers(Long id) {
        return null;
    }
}

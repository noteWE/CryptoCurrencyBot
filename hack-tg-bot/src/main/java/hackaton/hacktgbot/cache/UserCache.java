package hackaton.hacktgbot.cache;

import hackaton.hacktgbot.bots.BotState;
import hackaton.hacktgbot.entities.UserData;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class UserCache {
    private Map<Long, BotState> usersCache = new HashMap<>();
    private Map<Long, User> userDataCache = new HashMap<>();

    public void addUser(Long chatId) {
        usersCache.put(chatId, BotState.WAITING);
    }

    public void changeUserState(Long chatId, BotState state) {
        usersCache.put(chatId, state);
    }

    public void addUserData(Long chatId, UserData userData) {
        userDataCache.put(chatId, User.builder()
                .chatId(chatId)
                .userData(userData)
                .build());
    }

    public void deleteUserData(Long chatId) {
        userDataCache.remove(chatId);
    }

    public UserData getUserData(Long chatId) {
        User user = userDataCache.get(chatId);
        if (user == null) {
            return null;
        } else {
            return userDataCache.get(chatId).getUserData();
        }
    }

    public BotState getUserState(Long chatId) {
        return usersCache.get(chatId);
    }

    public void setUserMessageStream(Long chatId, Integer messageId) {
        userDataCache.get(chatId).setStreamMessageId(messageId);
    }

    public Set<User> getAllUserSend() {
        return userDataCache.entrySet()
                .stream()
                .filter(x -> usersCache.get(x.getKey()).equals(BotState.SENDING))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    public Set<User> getAllUserTrigger() {
        return userDataCache.entrySet()
                .stream()
                .filter(x -> x.getValue().getUserData().getTriggers() != null &&
                        !x.getValue().getUserData().getTriggers().isEmpty())
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    public Integer getUserMessageId(Long chatId) {
        User user = userDataCache.get(chatId);
        if (user != null) {
            return user.getStreamMessageId();
        } else {
            return null;
        }
    }
}

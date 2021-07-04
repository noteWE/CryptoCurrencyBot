package hackaton.hacktgbot.services.impl;

import hackaton.hacktgbot.bots.BotState;
import hackaton.hacktgbot.entities.Figi;
import hackaton.hacktgbot.entities.Trigger;
import hackaton.hacktgbot.entities.User;
import hackaton.hacktgbot.entities.UserData;
import hackaton.hacktgbot.repositories.UserRepository;
import hackaton.hacktgbot.services.UserCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MySqlUserService implements UserCacheService {

    private final UserRepository userRepository;

    @Override
    public void addUser(Long chatId) {
        log.info("saving user");
        User user = new User();
        user.setChatId(chatId);
        user = userRepository.save(user);
        log.info(user.toString());
    }

    @Override
    @Transactional
    public void changeUserState(Long chatId, BotState state) {
        User user = userRepository.findByChatId(chatId);
        user.setBotState(state);
    }

    @Override
    @Transactional
    public void changeUserMessageId(Long chatId, Integer messageId) {
        User user = userRepository.findByChatId(chatId);
        user.setStreamMessageId(messageId);
    }

    @Override
    public BotState getUserState(Long chatId) {
        return userRepository.findByChatId(chatId).getBotState();
    }

    @Override
    public Integer getUserMessageId(Long chatId) {
        return userRepository.findByChatId(chatId).getStreamMessageId();
    }

    @Override
    @Transactional
    public void addUserData(Long chatId, UserData userData) {
        User user = userRepository.findByChatId(chatId);
        user.getFigis().addAll(
                Optional.ofNullable(userData.getFigis())
                        .orElse(List.of())
                        .stream()
                        .map(x -> {
                            Figi figi = new Figi();
                            figi.setFigi(x);
                            figi.setUser(user);
                            return figi;
                        })
                        .collect(Collectors.toList()));
        user.getTriggers().addAll(
                Optional.ofNullable(userData.getTriggers())
                        .orElse(List.of())
                        .stream()
                        .filter(x -> x.getTriggerId() == null)
                        .peek(x -> x.setUser(user))
                        .collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void deleteUserData(Long chatId) {
        User user = userRepository.findByChatId(chatId);
        user.getFigis().clear();
        user.getTriggers().clear();
    }

    @Override
    @Transactional
    public UserData getUserData(Long chatId) {
        User user = userRepository.findByChatId(chatId);
        if ((user.getFigis() == null || user.getFigis().isEmpty()) &&
                (user.getTriggers() == null || user.getTriggers().isEmpty())) {
            return null;
        }
        return UserData.builder()
                .figis(user.getFigis().stream()
                        .map(Figi::getFigi)
                        .collect(Collectors.toList()))
                .triggers(user.getTriggers())
                .build();
    }

    @Override
    @Transactional
    public Set<hackaton.hacktgbot.cache.User> getAllUsersSend() {
        return userRepository.findAllByBotState(BotState.SENDING).stream()
                .map(x ->
                        hackaton.hacktgbot.cache.User.builder()
                                .userData(UserData.builder()
                                        .figis(x.getFigis().stream()
                                                .map(Figi::getFigi)
                                                .collect(Collectors.toList()))
                                        .triggers(x.getTriggers())
                                        .build())
                                .chatId(x.getChatId())
                                .streamMessageId(x.getStreamMessageId())
                                .build())
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Set<hackaton.hacktgbot.cache.User> getAllUsersTrigger() {
        List<User> users1 = userRepository.findAll();
        log.info("Users size: " + users1.size());
        return users1.stream()
                .filter(x -> !x.getTriggers().isEmpty())
                .map(x ->
                        hackaton.hacktgbot.cache.User.builder()
                                .userData(UserData.builder()
                                        .figis(x.getFigis().stream()
                                                .map(Figi::getFigi)
                                                .collect(Collectors.toList()))
                                        .triggers(x.getTriggers())
                                        .build())
                                .chatId(x.getChatId())
                                .streamMessageId(x.getStreamMessageId())
                                .build())
                .collect(Collectors.toSet());
    }

    @Transactional
    public List<Trigger> getAllUserTriggers(Long chatId) {
        User user = userRepository.findByChatId(chatId);
        if (user == null) {
            return null;
        }
        List<Trigger> triggers = user.getTriggers();
        log.info("" + triggers.size());
        return triggers;
    }

    @Override
    @Transactional
    public void deleteUserTrigger(Long chatId, Long triggerId) {
        User user = userRepository.findByChatId(chatId);
        Trigger trigger = user.getTriggers().stream()
                .filter(x -> x.getTriggerId().equals(triggerId)).findFirst().get();
        user.getTriggers().remove(trigger);
    }
}

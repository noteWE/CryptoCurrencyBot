package hackaton.hacktgbot.repositories;

import hackaton.hacktgbot.bots.BotState;
import hackaton.hacktgbot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByChatId(Long chatId);

    List<User> findAllByBotState(BotState botState);

    List<User> findAllByTriggers_Empty();

    List<User> findAllByTriggersEmpty();
}

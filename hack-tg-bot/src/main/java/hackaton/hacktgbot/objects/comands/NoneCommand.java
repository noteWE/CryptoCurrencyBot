package hackaton.hacktgbot.objects.comands;

import hackaton.hacktgbot.bots.BotState;
import hackaton.hacktgbot.entities.UserData;
import hackaton.hacktgbot.entities.Figis;
import hackaton.hacktgbot.entities.Trigger;
import hackaton.hacktgbot.services.CoinService;
import hackaton.hacktgbot.services.UserCacheService;

import java.math.BigDecimal;
import java.util.List;

public class NoneCommand {

    private final UserCacheService userCacheService;
    private final CoinService service;

    public NoneCommand(UserCacheService userCacheService, CoinService service) {
        this.userCacheService = userCacheService;
        this.service = service;
    }

    private Figis stringToFigis(String figi) {
        switch (figi) {
            case "BTC":
                return Figis.BTC;
            case "ETH":
                return Figis.ETH;
            case "BNB":
                return Figis.BNB;
            case "DOGE":
                return Figis.DOGE;
            case "DOT":
                return Figis.DOT;
            case "ADA":
                return Figis.ADA;
            default:
                return null;
        }
    }

    public String execute(Long chatId, String messageText) {
        BotState botState = userCacheService.getUserState(chatId);
        if (botState != null) {
            switch (botState) {
                default:
                case SENDING:
                case WAITING:
                    return "Вы ввели не команду. Посмотрите список команд.";
                case ASK_FIGI: {
                    UserData userData = userCacheService.getUserData(chatId);
                    Figis figi = stringToFigis(messageText.trim());
                    if (figi == null) {
                        return "Вы ввели неизвестную нам криптовалюту!";
                    }
                    if (userData != null) {
                        userData.setFigis(List.of(figi));
                    } else {
                        userData = UserData.builder()
                                .figis(List.of(figi))
                                .build();
                    }
                    userCacheService.addUserData(chatId, userData);
                    userCacheService.changeUserState(chatId, BotState.SENDING);
                    return String.format("Теперь вы следите за изменением курса криптовалюты: %s", figi.name());
                }
                case ASK_TRIGGER: {
                    String[] figiAndPercent = messageText.trim().split(" ");
                    if (figiAndPercent.length == 2) {
                        Figis figi = stringToFigis(figiAndPercent[0]);
                        BigDecimal percent = null;
                        if (figi == null) {
                            return "Вы ввели некорректные данные FIGI";
                        }
                        try {
                            percent = new BigDecimal((figiAndPercent[1]));
                            if (percent.compareTo(BigDecimal.ZERO) <= 0) {
                                return "Процент падения должен быть больше нуля";
                            }
                        } catch (NumberFormatException exception) {
                            return "Вы ввели некорректные данные процента";
                        }
                        UserData data = userCacheService.getUserData(chatId);
                        var apiData = service.getCurrencyByFigi(figi);
                        BigDecimal currentPrice = apiData.getData()
                                .stream().findFirst().get().getPriceUsd();
                        if (data == null) {
                            data = UserData.builder().build();
                        }
                        Trigger trigger = new Trigger();
                        trigger.setTelegramNotification(true);
                        trigger.setCurrentPrice(currentPrice);
                        trigger.setTargetPrice(currentPrice.multiply(BigDecimal.ONE.subtract(percent)));
                        trigger.setFigi(figi);
                        data.setTriggers(List.of(trigger));
                        userCacheService.addUserData(chatId, data);
                        if (data.getFigis() != null) {
                            userCacheService.changeUserState(chatId, BotState.SENDING);
                        } else {
                            userCacheService.changeUserState(chatId, BotState.WAITING);
                        }
                        return String.format("Вы установили триггер на событие:\nПадение курса %s, на %f%%", figi.name(), percent);
                    } else {
                        return "Вы ввели FIGI и процент в неверном формате";
                    }
                }
            }
        } else {
            return "Введите команду старт чтобы начать";
        }
    }
}

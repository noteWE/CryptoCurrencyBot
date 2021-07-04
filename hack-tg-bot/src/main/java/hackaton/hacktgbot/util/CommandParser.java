package hackaton.hacktgbot.util;

import hackaton.hacktgbot.objects.Commands;

public class CommandParser {

    private static final String COMMAND_SIGN = "/";

    public static boolean isCommand(String commandString) {
        return commandString.startsWith(COMMAND_SIGN);
    }

    public static Commands parseCommand(String commandString) {
        String trimCommand = commandString.trim();
        if (isCommand(trimCommand)) {
            switch (trimCommand) {
                case "subscribe":
                    return Commands.SUBSCRIBE;
                case "unsub":
                    return Commands.UNSUB;
                case "trigger":
                    return Commands.TRIGGER;
                default:
                    throw new RuntimeException("Неизвестная команда");
            }
        } else {
            throw new RuntimeException("Не команда");
        }
    }
}

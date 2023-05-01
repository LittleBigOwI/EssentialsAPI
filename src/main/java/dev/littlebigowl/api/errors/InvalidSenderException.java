package dev.littlebigowl.api.errors;

import org.bukkit.command.CommandSender;

import dev.littlebigowl.api.constants.Colors;

public class InvalidSenderException {
    
    private String cause;

    public InvalidSenderException() {
        this.cause = Colors.FAILURE.getChatColor() + "Invalid sender type.";
    }

    public InvalidSenderException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}

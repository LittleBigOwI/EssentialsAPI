package dev.littlebigowl.api.errors;

import org.bukkit.command.CommandSender;

import dev.littlebigowl.api.constants.Colors;

public class InvalidWorldException {
    
    private String cause;

    public InvalidWorldException() {
        this.cause = Colors.FAILURE.getChatColor() + "You aren't in the right world.";
    }

    public InvalidWorldException(String cause) {
        this.cause = Colors.FAILURE.getChatColor() + cause;
    }

    public boolean sendCause(CommandSender sender) {
        sender.sendMessage(this.cause);
        return true;
    }

}
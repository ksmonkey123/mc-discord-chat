package ch.awae.minecraft.discordchat.discord;

import java.util.Objects;
import java.util.Optional;

public class OutgoingDiscordMessage {

    private final String username;
    private final String message;

    private OutgoingDiscordMessage(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    // static factories
    public static OutgoingDiscordMessage userMessage(String username, String message) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(message);
        return new OutgoingDiscordMessage(username, message);
    }

    public static OutgoingDiscordMessage serverMessage(String message) {
        Objects.requireNonNull(message);
        return new OutgoingDiscordMessage(null, message);
    }

}

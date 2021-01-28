package ch.awae.minecraft.discordchat.minecraft;

public class ChatMessage {
    public String user;
    public String message;
    public String token;

    public ChatMessage() {
    }

    public ChatMessage(String user, String message) {
        this.user = user;
        this.message = message;
        this.token = null;
    }

    @Override
    public String toString() {
        return user + ": " + message;
    }
}

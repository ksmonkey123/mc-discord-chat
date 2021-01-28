package ch.awae.minecraft.discordchat.minecraft;

public class ChatMessage {
    public String user;
    public String message;

    public ChatMessage() {
    }

    public ChatMessage(String user, String message) {
        this.user = user;
        this.message = message;
    }
}

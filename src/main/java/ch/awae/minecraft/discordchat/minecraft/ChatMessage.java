package ch.awae.minecraft.discordchat.minecraft;

class ChatMessage {
    public String user;
    public String message;

    public ChatMessage(String user, String message) {
        this.user = user;
        this.message = message;
    }

    @Override
    public String toString() {
        return user + ": " + message;
    }
}

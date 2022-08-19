package ch.awae.minecraft.discordchat.minecraft;

class ChatMessage {
    public String token;
    public String user;
    public String message;

    public ChatMessage(String token, String user, String message) {
        this.token = token;
        this.user = user;
        this.message = message;
    }

    @Override
    public String toString() {
        return user + ": " + message;
    }
}

package ch.awae.minecraft.discordchat.discord;

import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DiscordSendingService {

    private static String[][] ESCAPE_SEQUENCES = {
            {"\\", "\\\\"},
            {"\"", "\\\""},
            {"\n", "\\\n"}
    };

    public void send(Mapping mapping, OutgoingDiscordMessage message) throws IOException {
        DiscordWebhook request = new DiscordWebhook(mapping.getDiscordWebhookUrl());

        String user = message.getUsername();
        if (user == null) {
            request.setUsername("Server Information");
        } else {
            request.setUsername(user);
            request.setAvatarUrl("https://cravatar.eu/helmhead/" + user + "/256.png");
        }
        request.setContent(escapeMessage(message.getMessage()));
        request.execute();
    }

    private String escapeMessage(String message) {
        for (String[] escapeSequence : ESCAPE_SEQUENCES) {
            message = message.replace(escapeSequence[0], escapeSequence[1]);
        }
        return message;
    }
}

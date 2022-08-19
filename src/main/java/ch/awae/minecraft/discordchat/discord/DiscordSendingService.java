package ch.awae.minecraft.discordchat.discord;

import ch.awae.minecraft.discordchat.minecraft.MinecraftSendingService;
import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class DiscordSendingService {
    private final static Logger log = Logger.getLogger(DiscordSendingService.class.getName());

    private static String[][] ESCAPE_SEQUENCES = {
            {"\\", "\\\\"},
            {"\"", "\\\""},
            {"\n", "\\n"}
    };

    public void send(Mapping mapping, OutgoingDiscordMessage message) throws IOException {
        DiscordWebhook request = new DiscordWebhook(mapping.getDiscordWebhookUrl());

        String content = escapeMessage(message.getMessage());
        request.setContent(content);
        String user = message.getUsername();
        if (user == null) {
            request.setUsername("Server Information");
            log.info("server " + mapping.getId() + ":           ==> discord: " + content);
        } else {
            request.setUsername(user);
            request.setAvatarUrl("https://cravatar.eu/helmhead/" + user + "/256.png");
            log.info("server " + mapping.getId() + ": minecraft ==> discord: " + user + ": " + content);
        }
        request.execute();
    }
    private String escapeMessage(String message) {
        for (String[] escapeSequence : ESCAPE_SEQUENCES) {
            message = message.replace(escapeSequence[0], escapeSequence[1]);
        }
        return message;
    }
}

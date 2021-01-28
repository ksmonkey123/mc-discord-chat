package ch.awae.minecraft.discordchat.discord;

import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DiscordSendingService {

    public void send(Mapping mapping, String user, String message) throws IOException {
        DiscordWebhook request = new DiscordWebhook(mapping.getDiscordWebhookUrl());
        request.setContent(message);
        request.setUsername(user);
        request.setAvatarUrl("https://cravatar.eu/helmhead/" + user + "/256.png");
        request.execute();
    }

}

package ch.awae.minecraft.discordchat.discord;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.Channel;
import discord4j.discordjson.json.WebhookData;
import discord4j.rest.entity.RestChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DiscordSendingService {

    private final String url;

    @Autowired
    public DiscordSendingService(DiscordConfiguration config, GatewayDiscordClient client) {
        this.url = client.getChannelById(config.getChannelId())
                .map(Channel::getRestChannel)
                .flatMapMany(RestChannel::getWebhooks)
                .filter(w -> !w.token().isAbsent())
                .toStream(1)
                .findAny()
                .map(this::generateWebhookURL)
                .get();
    }

    private String generateWebhookURL(WebhookData webhook) {
        return "https://discord.com/api/webhooks/" + webhook.id() + "/" + webhook.token().get();
    }

    public void send(String user, String message) throws IOException {
        DiscordWebhook request = new DiscordWebhook(url);
        request.setContent(message);
        request.setUsername(user);
        request.execute();
    }

}

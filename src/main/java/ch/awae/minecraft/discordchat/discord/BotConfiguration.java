package ch.awae.minecraft.discordchat.discord;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

@Profile("!test")
@Configuration
public class BotConfiguration {

    private final String token;

    @Autowired
    public BotConfiguration(@Value("${discord.token}") String tokenFile) throws IOException {
        token = Files.readAllLines(new File(tokenFile).toPath()).get(0);
    }

    @Bean
    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> listeners) {
        GatewayDiscordClient client = DiscordClientBuilder.create(token)
                .build()
                .login()
                .block();

        Objects.requireNonNull(client);

        for (EventListener<T> listener : listeners) {
            client.on(listener.getEventType())
                    .flatMap(listener::execute)
                    .onErrorResume(listener::handleError)
                    .subscribe();
        }

        return client;
    }
}

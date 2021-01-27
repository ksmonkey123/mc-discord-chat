package ch.awae.minecraft.discordchat.discord;

import ch.awae.minecraft.discordchat.minecraft.MinecraftSendingService;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

@Service
public class DiscordMessageHandler implements EventListener<MessageCreateEvent> {

    private static Logger log = Logger.getLogger(DiscordMessageHandler.class.getName());

    private final DiscordConfiguration config;
    private final ExecutorService async;
    private final MinecraftSendingService service;

    @Autowired
    public DiscordMessageHandler(
            DiscordConfiguration config,
            ExecutorService async,
            MinecraftSendingService service) {
        this.config = config;
        this.async = async;
        this.service = service;
    }

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> config.getChannelId().equals(message.getChannelId()))
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(this::logMessage)
                .then();
    }

    private Mono<?> logMessage(Message message) {
        String author = message.getAuthor().map(User::getUsername).orElse(null);
        log.info(author + ": " + message.getContent());
        async.submit(() -> service.send(author, message.getContent()));
        return Mono.empty();
    }
}

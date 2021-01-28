package ch.awae.minecraft.discordchat.discord;

import ch.awae.minecraft.discordchat.minecraft.MinecraftSendingService;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DiscordMessageHandler implements EventListener<MessageCreateEvent> {

    private static final Logger log = Logger.getLogger(DiscordMessageHandler.class.getName());

    private final DiscordConfig config;
    private final ExecutorService async;
    private final MinecraftSendingService service;

    @Autowired
    public DiscordMessageHandler(
            DiscordConfig config,
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
                .flatMap(this::forwardMessage)
                .then();
    }

    private Mono<?> forwardMessage(Message message) {
        String author = message.getAuthor().map(User::getUsername).orElse(null);
        log.info(author + ": " + message.getContent());

        List<String> lines = Stream.of(message.getContent().split("\n"))
                .map(line -> WordUtils.wrap(line, 255))
                .map(line -> line.split("\n"))
                .flatMap(Stream::of)
                .filter(line -> line.length() > 0)
                .collect(Collectors.toList());

        async.submit(() -> {
            for (String line : lines) {
                service.send(author, line);
            }
        });

        return Mono.empty();
    }
}

package ch.awae.minecraft.discordchat.discord;

import ch.awae.minecraft.discordchat.minecraft.MinecraftSendingService;
import ch.awae.minecraft.discordchat.minecraft.ServerStatusService;
import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import ch.awae.minecraft.discordchat.persistence.repository.MappingRepository;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import mcstatus.model.Player;
import mcstatus.model.ServerStatus;
import org.apache.commons.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DiscordMessageHandler implements EventListener<MessageCreateEvent> {

    private static final Logger log = Logger.getLogger(DiscordMessageHandler.class.getName());

    private final ExecutorService async;
    private final MinecraftSendingService service;
    private final MappingRepository mappingRepository;
    private final ServerStatusService statusService;
    private final DiscordSendingService sendingService;

    @Autowired
    public DiscordMessageHandler(
            ExecutorService async,
            MinecraftSendingService service,
            MappingRepository mappingRepository,
            DiscordSendingService sendingService,
            ServerStatusService statusService
    ) {
        this.async = async;
        this.service = service;
        this.mappingRepository = mappingRepository;
        this.sendingService = sendingService;
        this.statusService = statusService;
    }

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        return Mono.just(event)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .flatMap(this::fetchMapping)
                .flatMap(this::forwardMessage)
                .then();
    }

    private Mono<MappedMessage> fetchMapping(Message message) {
        return Mono.justOrEmpty(
                mappingRepository
                        .findByDiscordChannelId(message.getChannelId().asString())
                        .map(mapping -> new MappedMessage(mapping, message)));
    }

    private Mono<?> forwardMessage(MappedMessage mappedMessage) {
        Message message = mappedMessage.message;
        String author = message.getAuthor().map(User::getUsername).orElse(null);
        log.info(author + ": " + message.getContent());

        if (message.getContent().equals("!info")) {
            async.submit(() -> printServerStatus(mappedMessage.mapping));
        } else {
            List<String> lines = Stream.of(message.getContent().split("\n"))
                    .map(line -> WordUtils.wrap(line, 255))
                    .map(line -> line.split("\n"))
                    .flatMap(Stream::of)
                    .filter(line -> line.length() > 0)
                    .collect(Collectors.toList());

            async.submit(() -> {
                for (String line : lines) {
                    service.send(mappedMessage.mapping, author, line);
                }
            });
        }

        return Mono.empty();
    }

    private void printServerStatus(Mapping mapping) {
        ServerStatus serverStatus = statusService.getServerStatus(mapping);

        String message;

        if (serverStatus == null) {
            message = "server offline";
        } else {
            List<String> playerNames = new ArrayList<>();
            if (serverStatus.players.online > 0) {
                for (Player player : serverStatus.players.players) {
                    playerNames.add(player.name);
                }
            }

            message = (serverStatus.players.online > 0
                    ? "server online - "+serverStatus.players.online+" players:\n - " + String.join("\n - ", playerNames)
                    : "server online - 0 players");
        }
        try {
            sendingService.send(mapping, OutgoingDiscordMessage.serverMessage(message));
        } catch (IOException e) {
            log.log(Level.SEVERE, "error while sending message", e);
        }
    }

    private static class MappedMessage {
        private final Mapping mapping;
        private final Message message;

        private MappedMessage(Mapping mapping, Message message) {
            this.mapping = mapping;
            this.message = message;
        }
    }

}

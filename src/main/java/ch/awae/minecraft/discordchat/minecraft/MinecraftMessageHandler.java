package ch.awae.minecraft.discordchat.minecraft;

import ch.awae.minecraft.discordchat.discord.DiscordSendingService;
import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import ch.awae.minecraft.discordchat.persistence.repository.MappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class MinecraftMessageHandler {

    private static final Logger log = Logger.getLogger(MinecraftMessageHandler.class.getName());

    private final DiscordSendingService sendingService;
    private final MappingRepository mappingRepository;

    @Autowired
    public MinecraftMessageHandler(DiscordSendingService sendingService,
                                   MappingRepository mappingRepository) {
        this.sendingService = sendingService;
        this.mappingRepository = mappingRepository;
    }

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void message(@RequestBody ChatMessage request) throws IOException {
        Mapping mapping = Optional.ofNullable(request)
                .map(r -> r.token)
                .flatMap(mappingRepository::findByMinecraftServerToken)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.FORBIDDEN));
        log.info(request.toString());
        sendingService.send(mapping, request.user, request.message);
    }

}

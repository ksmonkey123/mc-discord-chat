package ch.awae.minecraft.discordchat.minecraft;

import ch.awae.minecraft.discordchat.discord.DiscordSendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.logging.Logger;

@RestController
public class MinecraftMessageHandler {

    private static Logger log = Logger.getLogger(MinecraftMessageHandler.class.getName());

    private final DiscordSendingService sendingService;

    @Autowired
    public MinecraftMessageHandler(DiscordSendingService sendingService) {
        this.sendingService = sendingService;
    }

    @PostMapping("/message")
    public void message(@RequestBody MessageRequest request) throws IOException {
        log.info(request.toString());
        sendingService.send(request.user, request.message);
    }

    static class MessageRequest {
        public String user;
        public String message;

        @Override
        public String toString() {
            return user + ": " + message;
        }
    }

}

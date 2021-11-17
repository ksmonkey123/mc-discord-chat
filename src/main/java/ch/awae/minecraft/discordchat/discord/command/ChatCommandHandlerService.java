package ch.awae.minecraft.discordchat.discord.command;

import ch.awae.minecraft.discordchat.discord.DiscordSendingService;
import ch.awae.minecraft.discordchat.discord.OutgoingDiscordMessage;
import ch.awae.minecraft.discordchat.minecraft.ServerStatusService;
import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import mcstatus.model.Player;
import mcstatus.model.ServerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ChatCommandHandlerService {

    private final static Logger log = Logger.getLogger(ChatCommandHandlerService.class.getName());

    private final ServerStatusService statusService;
    private final DiscordSendingService sendingService;

    @Autowired
    public ChatCommandHandlerService(ServerStatusService statusService, DiscordSendingService sendingService) {
        this.statusService = statusService;
        this.sendingService = sendingService;
    }

    public void printServerStatus(Mapping mapping) {
        ServerStatus serverStatus = statusService.getServerStatus(mapping);

        String message = mapping.getDiscordChannelDescription() + "\n";

        if (serverStatus == null) {
            message += "server offline";
        } else {
            List<String> playerNames = new ArrayList<>();
            if (serverStatus.players.online > 0) {
                for (Player player : serverStatus.players.players) {
                    playerNames.add(player.name);
                }
            }

            message += (serverStatus.players.online > 0
                    ? "server online - "+serverStatus.players.online+" players:\n - " + String.join("\n - ", playerNames)
                    : "server online - 0 players");
        }
        try {
            sendingService.send(mapping, OutgoingDiscordMessage.serverMessage(message));
        } catch (IOException e) {
            log.log(Level.SEVERE, "error while sending message", e);
        }
    }

}

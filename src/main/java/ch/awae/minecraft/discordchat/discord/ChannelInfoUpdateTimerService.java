package ch.awae.minecraft.discordchat.discord;

import ch.awae.minecraft.discordchat.minecraft.ServerStatusService;
import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import ch.awae.minecraft.discordchat.persistence.repository.MappingRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.channel.Channel;
import mcstatus.model.Player;
import mcstatus.model.ServerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ChannelInfoUpdateTimerService {

    private final static Logger logger = Logger.getLogger(ChannelInfoUpdateTimerService.class.getName());

    private final MappingRepository repo;
    private final ServerStatusService service;
    private final DiscordChannelUpdateService updater;

    @Autowired
    public ChannelInfoUpdateTimerService(MappingRepository repo,
                                         ServerStatusService service,
                                         DiscordChannelUpdateService updater
    ) {
        this.repo = repo;
        this.service = service;
        this.updater = updater;
    }

    @Scheduled(fixedRate = 300 * 1000)
    public void updateAll() {
        logger.info("updating minecraft server status");
        repo.findAll()
                .stream()
                .filter(m -> m.getMinecraftServerApiUrl() != null)
                .filter(m -> m.getDiscordChannelName() != null)
                .forEach(this::updateChannel);
    }

    public void updateChannel(Mapping mapping) {
        logger.info("updating status of server " + mapping.getId());
        ServerStatus status = service.getServerStatus(mapping);

        updateChannelName(mapping, status);
    }

    private void updateChannelName(Mapping mapping, ServerStatus response) {
        if (response != null) {
            String channelName = mapping.getDiscordChannelName() + "︱" + response.players.online;

            List<String> playerNames = new ArrayList<>();

            if (response.players.online > 0) {
                for (Player player : response.players.players) {
                    playerNames.add(player.name);
                }
            }

            String desc = (response.players.online > 0
                                    ? "online:\n - " + String.join("\n - ", playerNames)
                                    : "no players online");

            updater.updateChannel(mapping, channelName, desc);
        } else {
            updater.updateChannel(mapping, mapping.getDiscordChannelName() + "︱offline", "server offline");
        }
    }
}

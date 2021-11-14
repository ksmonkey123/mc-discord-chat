package ch.awae.minecraft.discordchat.minecraft.processing;

import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import ch.awae.minecraft.discordchat.persistence.repository.MappingRepository;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.Channel;
import discord4j.discordjson.json.ChannelModifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChannelNameProcessor {

    private final RestTemplate http;
    private final MappingRepository repo;
    private final GatewayDiscordClient discord;

    @Autowired
    public ChannelNameProcessor(RestTemplate http,
                                MappingRepository repo,
                                GatewayDiscordClient discord
    ) {
        this.http = http;
        this.repo = repo;
        this.discord = discord;
    }

    @Scheduled(fixedRate = 305 * 1000)
    public void updateAll() {
        repo.findAll()
                .stream()
                .filter(m -> m.getMinecraftServerApiUrl() != null)
                .filter(m -> m.getDiscordChannelName() != null)
                .forEach(this::updateChannel);
    }

    public void updateChannel(Mapping mapping) {
        SrvStatResponse response = http.getForObject("https://api.mcsrvstat.us/2/" + mapping.getMinecraftServerAddress(), SrvStatResponse.class);

        updateChannelName(mapping, response);
    }

    private void updateChannelName(Mapping mapping, SrvStatResponse response) {
        ChannelModifyRequest modification;

        if (response != null && response.online) {
            String channelName = mapping.getDiscordChannelName() + "︱" + response.players.online;
            modification = ChannelModifyRequest.builder()
                    .name(channelName)
                    .topic(mapping.getDiscordChannelDescription() + " | " +
                            (response.players.online > 0
                                ? "online:\n - " + String.join("\n - ", response.players.list)
                                : "no players online"))
                    .build();
        } else {
            modification = ChannelModifyRequest.builder()
                    .name(mapping.getDiscordChannelName() + "︱offline")
                    .topic("server offline")
                    .build();
        }

        discord.getChannelById(Snowflake.of(mapping.getDiscordChannelId()))
                .map(Channel::getRestChannel)
                .flatMap(c -> c.modify(modification, null))
                .block();
    }

    static class SrvStatResponse {
        public boolean online;
        public PlayerData players;

        static class PlayerData {
            public int online;
            public String[] list;
        }
    }

}

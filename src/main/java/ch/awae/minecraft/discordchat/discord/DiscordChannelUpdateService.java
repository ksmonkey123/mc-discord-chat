package ch.awae.minecraft.discordchat.discord;

import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.Channel;
import discord4j.discordjson.json.ChannelModifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class DiscordChannelUpdateService {

    private final static Logger logger = Logger.getLogger(DiscordChannelUpdateService.class.getName());

    private final GatewayDiscordClient discord;
    private static final Map<String, String> titleMap = Collections.synchronizedMap(new HashMap<>());
    private static final Map<String, String> descriptionMap = Collections.synchronizedMap(new HashMap<>());

    @Autowired
    public DiscordChannelUpdateService(GatewayDiscordClient discord) {
        this.discord = discord;
    }

    public synchronized void updateChannel(Mapping mapping, String title, String description) {
        String key = mapping.getDiscordChannelId();

        String oldTitle = titleMap.get(key);
        String oldDesc = descriptionMap.get(key);


        boolean dirty = false;

        if (!Objects.equals(oldTitle, title)) {
            titleMap.put(key, title);
            dirty = true;
        }
        if (!Objects.equals(oldDesc, description)) {
            descriptionMap.put(key, description);
            dirty = true;
        }

        if (dirty) {
            logger.info("sending channel update for mapping " + mapping.getId());
            sendUpdate(mapping, title, description);
        } else {
            logger.info("no channel update needed for mapping " + mapping.getId());
        }
    }

    private void sendUpdate(Mapping mapping, String title, String description) {
        ChannelModifyRequest modification = ChannelModifyRequest.builder()
                .name(title)
                .topic(description)
                .build();

        try {
            discord.getChannelById(Snowflake.of(mapping.getDiscordChannelId()))
                    .map(Channel::getRestChannel)
                    .flatMap(c -> c.modify(modification, null))
                    .block(Duration.ofSeconds(5));
        } catch(RuntimeException e) {
            logger.log(Level.SEVERE, "error while updating channel info", e);
            titleMap.remove(mapping.getDiscordChannelId());
            descriptionMap.remove(mapping.getDiscordChannelId());
        }
    }

}

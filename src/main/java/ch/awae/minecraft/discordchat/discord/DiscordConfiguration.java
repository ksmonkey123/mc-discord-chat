package ch.awae.minecraft.discordchat.discord;

import discord4j.common.util.Snowflake;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Configuration
@ConfigurationProperties(prefix = "discord")
public class DiscordConfiguration {

    private String tokenFile;
    private Long channelId;

    public String getTokenFile() {
        return tokenFile;
    }

    public void setTokenFile(String tokenFile) {
        this.tokenFile = tokenFile;
    }

    public String getToken() throws IOException {
        InputStream stream = this.getClass().getResourceAsStream(tokenFile);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return reader.readLine();
        }
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Snowflake getChannelId() {
        return Snowflake.of(channelId);
    }
}

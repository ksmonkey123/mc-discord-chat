package ch.awae.minecraft.discordchat.persistence.model;

import javax.persistence.*;

@Entity
@Table(name = "mapping")
public class Mapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String discordChannelId;

    private String discordChannelName;

    private String discordWebhookUrl;

    private String minecraftServerToken;

    private String minecraftServerAddress;

    private String minecraftServerApiUrl;

    public Mapping() {

    }

    public Long getId() {
        return id;
    }

    public String getDiscordChannelId() {
        return discordChannelId;
    }

    public void setDiscordChannelId(String discordChannelId) {
        this.discordChannelId = discordChannelId;
    }

    public String getDiscordWebhookUrl() {
        return discordWebhookUrl;
    }

    public void setDiscordWebhookUrl(String discordWebhookUrl) {
        this.discordWebhookUrl = discordWebhookUrl;
    }

    public String getMinecraftServerToken() {
        return minecraftServerToken;
    }

    public void setMinecraftServerToken(String minecraftServerToken) {
        this.minecraftServerToken = minecraftServerToken;
    }

    public String getMinecraftServerAddress() {
        return minecraftServerAddress;
    }

    public void setMinecraftServerAddress(String minecraftServerAddress) {
        this.minecraftServerAddress = minecraftServerAddress;
    }

    public String getDiscordChannelName() {
        return discordChannelName;
    }

    public void setDiscordChannelName(String discordChannelName) {
        this.discordChannelName = discordChannelName;
    }

    public String getMinecraftServerApiUrl() {
        return minecraftServerApiUrl;
    }

    public void setMinecraftServerApiUrl(String minecraftServerUrl) {
        this.minecraftServerApiUrl = minecraftServerUrl;
    }
}

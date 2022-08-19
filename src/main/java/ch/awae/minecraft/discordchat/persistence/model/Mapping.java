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
    
    private String discordChannelDescription;

    private String minecraftStatusHost;

    private String minecraftStatusPort;

    private boolean active;

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

    public String getDiscordChannelDescription() {
        return discordChannelDescription;
    }

    public void setDiscordChannelDescription(String discordChannelDescription) {
        this.discordChannelDescription = discordChannelDescription;
    }

    public String getMinecraftStatusHost() {
        return minecraftStatusHost;
    }

    public void setMinecraftStatusHost(String minecraftStatusHost) {
        this.minecraftStatusHost = minecraftStatusHost;
    }

    public String getMinecraftStatusPort() {
        return minecraftStatusPort;
    }

    public void setMinecraftStatusPort(String minecraftStatusPort) {
        this.minecraftStatusPort = minecraftStatusPort;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

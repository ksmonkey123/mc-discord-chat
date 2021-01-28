package ch.awae.minecraft.discordchat.minecraft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MinecraftSendingService {

    private final MinecraftConfig config;
    private final RestTemplate http;

    @Autowired
    public MinecraftSendingService(MinecraftConfig config, RestTemplate http) {
        this.config = config;
        this.http = http;
    }

    public void send(String user, String message) {
        http.postForObject(config.getUrl(), new ChatMessage(user, message), Object.class);
    }

}

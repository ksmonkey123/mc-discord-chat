package ch.awae.minecraft.discordchat.minecraft;

import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MinecraftSendingService {

    private final RestTemplate http;

    @Autowired
    public MinecraftSendingService(RestTemplate http) {
        this.http = http;
    }

    public void send(Mapping mapping, String user, String message) {
        http.postForObject(mapping.getMinecraftServerApiUrl(), new ChatMessage(user, message), Object.class);
    }

}

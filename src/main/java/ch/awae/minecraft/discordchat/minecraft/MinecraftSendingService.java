package ch.awae.minecraft.discordchat.minecraft;

import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
public class MinecraftSendingService {

    private final static Logger log = Logger.getLogger(MinecraftSendingService.class.getName());

    private final RestTemplate http;

    @Autowired
    public MinecraftSendingService(RestTemplate http) {
        this.http = http;
    }

    public void send(Mapping mapping, String user, String message) {
        if (mapping.getMinecraftServerApiUrl() != null) {
            http.postForObject(mapping.getMinecraftServerApiUrl(), new ChatMessage(mapping.getMinecraftServerToken(), user, message), Object.class);
        } else {
            log.warning("no minecraft_server_api_url set for mapping " + mapping.getId());
        }
    }

}

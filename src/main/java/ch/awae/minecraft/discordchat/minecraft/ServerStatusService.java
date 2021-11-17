package ch.awae.minecraft.discordchat.minecraft;

import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import mcstatus.model.ServerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServerStatusService {

    private final RestTemplate http;
    private final String url;

    @Autowired
    public ServerStatusService(RestTemplate http,
                               @Value("${minecraft.statusServer}") String mcstatUrl) {
        this.http = http;
        this.url = mcstatUrl;
    }

    public ServerStatus getServerStatus(String host, String port) {
        try {
            return http.getForObject(url + "/server?host=" + host + "&port=" + port, ServerStatus.class);
        } catch (Exception e) {

            return null;
        }
    }

    public ServerStatus getServerStatus(Mapping mapping) {
        return getServerStatus(mapping.getMinecraftStatusHost(), mapping.getMinecraftStatusPort());
    }
}

package ch.awae.minecraft.discordchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@SpringBootApplication
public class McDiscordChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(McDiscordChatApplication.class, args);
    }

}

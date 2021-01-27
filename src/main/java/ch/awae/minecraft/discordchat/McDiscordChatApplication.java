package ch.awae.minecraft.discordchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableConfigurationProperties
@SpringBootApplication
public class McDiscordChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(McDiscordChatApplication.class, args);
    }

    @Bean
    public RestTemplate createRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ExecutorService createExecutorService() {
        return Executors.newCachedThreadPool();
    }

}

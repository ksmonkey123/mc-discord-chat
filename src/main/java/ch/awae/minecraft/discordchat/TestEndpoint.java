package ch.awae.minecraft.discordchat;

import ch.awae.minecraft.discordchat.minecraft.ChatMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEndpoint {

    @PostMapping("/mc/message")
    public void getMessage(@RequestBody ChatMessage message) {
        System.out.println(message.user + " : " + message.message);
    }

}

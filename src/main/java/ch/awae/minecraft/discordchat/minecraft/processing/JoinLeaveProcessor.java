package ch.awae.minecraft.discordchat.minecraft.processing;

import ch.awae.minecraft.discordchat.discord.DiscordSendingService;
import ch.awae.minecraft.discordchat.minecraft.LogType;
import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;

@Service
public class JoinLeaveProcessor extends RegexMatchingProcessor {

    private final DiscordSendingService sendingService;

    @Autowired
    public JoinLeaveProcessor(DiscordSendingService sendingService) {
        super("^(?:\\[.+\\] ){2}\\[[^\\ ]+\\]\\: (\\w+) (joined|left) the game$");
        this.sendingService = sendingService;
    }

    @Override
    public void process(Mapping mapping, String line, LogType type) {
        if (type == LogType.OUTPUT) {
            super.process(mapping, line, type);
        }
    }

    @Override
    protected void processMatch(Mapping mapping, Matcher matcher) {
        String user = matcher.group(1);
        String joinLeave = matcher.group(2);
        try {
            sendingService.send(mapping, user, "*" + joinLeave + " the game*");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

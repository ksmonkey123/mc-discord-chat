package ch.awae.minecraft.discordchat.minecraft.processing;

import ch.awae.minecraft.discordchat.discord.ChannelInfoUpdateTimerService;
import ch.awae.minecraft.discordchat.discord.DiscordSendingService;
import ch.awae.minecraft.discordchat.discord.OutgoingDiscordMessage;
import ch.awae.minecraft.discordchat.minecraft.LogType;
import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Matcher;

@Service
public class JoinLeaveProcessor extends RegexMatchingProcessor {

    private final DiscordSendingService sendingService;
    private final ChannelInfoUpdateTimerService statusTimer;

    @Autowired
    public JoinLeaveProcessor(DiscordSendingService sendingService, ChannelInfoUpdateTimerService statusTimer) {
        super("^(?:\\[.+\\] ){2}\\[[^\\ ]+\\]\\: (\\w+) (joined|left) the game$");
        this.sendingService = sendingService;
        this.statusTimer = statusTimer;
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
            sendingService.send(mapping, OutgoingDiscordMessage.userMessage(user, "*" + joinLeave + " the game*"));
            statusTimer.updateChannel(mapping);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

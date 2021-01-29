package ch.awae.minecraft.discordchat.minecraft.processing;

import ch.awae.minecraft.discordchat.minecraft.LogStatementProcessor;
import ch.awae.minecraft.discordchat.minecraft.LogType;
import ch.awae.minecraft.discordchat.persistence.model.Mapping;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexMatchingProcessor implements LogStatementProcessor {

    private final Pattern pattern;

    protected RegexMatchingProcessor(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public void process(Mapping mapping, String line, LogType type) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.matches()) {
            processMatch(mapping, matcher);
        }
    }

    protected abstract void processMatch(Mapping mapping, Matcher matcher);

}

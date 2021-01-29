package ch.awae.minecraft.discordchat.minecraft;

import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import ch.awae.minecraft.discordchat.persistence.repository.MappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@RestController
public class MinecraftLogStatementController {

    private final MappingRepository mappingRepository;
    private final List<LogStatementProcessor> processors;

    @Autowired
    public MinecraftLogStatementController(MappingRepository mappingRepository,
                                           List<LogStatementProcessor> processors) {
        this.mappingRepository = mappingRepository;
        this.processors = processors;
    }

    @PostMapping("/log")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void log(@RequestBody IncomingLogStatement request) {
        Mapping mapping = Optional.ofNullable(request)
                .map(r -> r.token)
                .flatMap(mappingRepository::findByMinecraftServerToken)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.FORBIDDEN));

        for (LogStatementProcessor processor : processors) {
            processor.process(mapping, request.message, LogType.fromErrorFlag(request.error));
        }
    }

}

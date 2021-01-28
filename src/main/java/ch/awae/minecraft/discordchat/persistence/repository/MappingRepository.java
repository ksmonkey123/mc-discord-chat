package ch.awae.minecraft.discordchat.persistence.repository;

import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MappingRepository extends JpaRepository<Mapping, Long> {

    Optional<Mapping> findByDiscordChannelId(String channelId);

    Optional<Mapping> findByMinecraftServerToken(String token);

}

package ch.awae.minecraft.discordchat.persistence.repository;

import ch.awae.minecraft.discordchat.persistence.model.Mapping;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MappingRepository extends JpaRepository<Mapping, Long> {

    @Cacheable(cacheNames = "mappingsByChannel")
    Optional<Mapping> findByDiscordChannelId(String channelId);

    @Cacheable(cacheNames = "mappingsByToken")
    Optional<Mapping> findByMinecraftServerToken(String token);

}

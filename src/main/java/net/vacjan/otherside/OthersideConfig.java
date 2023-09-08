package net.vacjan.otherside;

import com.mojang.logging.LogUtils;
import net.vacjan.otherside.libs.SimpleConfig;
import org.slf4j.Logger;

public class OthersideConfig {
    public SimpleConfig CONFIG;
    private static final Logger LOGGER = LogUtils.getLogger();

    private int despawnCooldown;

    public void loadConfig() {
        CONFIG = SimpleConfig.of( "otherside" ).provider( this::defaultConfiguration ).request();
        despawnCooldown = CONFIG.getOrDefault( "despawn_cooldown", 5 );
        LOGGER.info("[Otherside] Config (re)loaded");
        LOGGER.info("[Otherside] Despawn cooldown is "+getDespawnCooldown()+" seconds");
    }

    private String defaultConfiguration( String filename ) {
        return """
                #Otherside configuration

                #Despawn cooldown in seconds
                despawn_cooldown=60
                """;
    }

    public int getDespawnCooldown() {
        return despawnCooldown;
    }

}

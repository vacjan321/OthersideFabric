package net.vacjan.otherside;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import static net.minecraft.server.command.CommandManager.literal;

public class Otherside implements ModInitializer {
    public static OthersideConfig config = new OthersideConfig();

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        System.out.println("INIT OTHERSIDE");

        config.loadConfig();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("otherside")
                .executes(context -> {
                    context.getSource().sendMessage(Text.literal("Otherside is enabled!"));

                    return 1;
                })
                .then(literal("reload")
                        .requires(source -> source.hasPermissionLevel(4))
                        .executes(context -> {
                            config.loadConfig();
                            context.getSource().sendMessage(Text.literal("Otherside reloaded!"));
                            context.getSource().sendMessage(Text.literal("Current despawn cooldown is " + config.getDespawnCooldown() + " seconds"));

                            return 1;
                        })
                )
        ));


    }
}

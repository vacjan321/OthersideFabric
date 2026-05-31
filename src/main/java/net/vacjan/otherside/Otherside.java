package net.vacjan.otherside;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import static net.minecraft.commands.Commands.literal;

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
                    context.getSource().sendSuccess(() -> Component.literal("Otherside is enabled!"), false);

                    return 1;
                })
                .then(literal("reload")
                        .requires(Commands.hasPermission(Commands.LEVEL_OWNERS))
                        .executes(context -> {
                            config.loadConfig();
                            context.getSource().sendSuccess(() -> Component.literal("Otherside reloaded!"), false);
                            context.getSource().sendSuccess(() -> Component.literal("Current despawn cooldown is " + config.getDespawnCooldown() + " seconds"), false);

                            return 1;
                        })
                )
        ));


    }
}

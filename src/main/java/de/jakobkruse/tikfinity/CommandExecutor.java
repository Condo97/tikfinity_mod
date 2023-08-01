package de.jakobkruse.tikfinity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;

public class CommandExecutor{
    public void execute(String command) {
        if(command.trim().isEmpty()) {
            return;
        }
        try {
            MinecraftServer server = MinecraftClient.getInstance().getServer();

            if (server != null) {
                String[] playerNames = server.getPlayerManager().getPlayerNames();

                if (playerNames.length > 0) {
                    String firstPlayer = playerNames[0];

                    ServerPlayerEntity player = server.getPlayerManager().getPlayer(firstPlayer);

                    if(player == null) {
                        return;
                    }


                    CommandManager cm = server.getCommandManager();
                    if(cm == null) {
                        return;
                    }

                    String[] commands = command.split("\n");
                    for (String cmd : commands) {
                        cm.executeWithPrefix(player.getCommandSource(), cmd.trim());
                    }
                }
            }
        } catch (NullPointerException ignored) {

        }

    }
}

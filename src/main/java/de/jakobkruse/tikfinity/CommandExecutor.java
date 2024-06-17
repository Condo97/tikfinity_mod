package de.jakobkruse.tikfinity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Arrays;

public class CommandExecutor {

    public void execute(String command) {
        if (command == null || command.trim().isEmpty()) {
            return;
        }

        try {
            MinecraftServer server = MinecraftClient.getInstance().getServer();

            if (server == null) {
                System.err.println("Server is null. Unable to execute commands.");
                return;
            }

            String[] playerNames = server.getPlayerManager().getPlayerNames();
            if (playerNames.length == 0) {
                System.err.println("No players online. Unable to execute commands.");
                return;
            }

            String firstPlayer = playerNames[0];
            ServerPlayerEntity player = server.getPlayerManager().getPlayer(firstPlayer);

            if (player == null) {
                System.err.println("Player " + firstPlayer + " is null. Unable to execute commands.");
                return;
            }

            CommandManager cm = server.getCommandManager();
            if (cm == null) {
                System.err.println("CommandManager is null. Unable to execute commands.");
                return;
            }

            String[] commands = Arrays.stream(command.split("\n"))
                    .map(String::trim)
                    .map(cmd -> cmd.startsWith("/") ? cmd : "/" + cmd)
                    .toArray(String[]::new);

            // Execute commands
            for (String cmd : commands) {
                server.execute(() -> {
                    try {
                        cm.executeWithPrefix(player.getCommandSource(), cmd);
                    } catch (Exception e) {
                        player.sendMessage(Text.of("Failed to execute command: " + cmd + " due to an error: " + e.getMessage()));
                        e.printStackTrace();
                    }
                });
            }

        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
        }
    }

}

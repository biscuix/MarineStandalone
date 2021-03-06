package com.marine.game.command;

import com.marine.player.Player;
import com.marine.server.Marine;
import com.marine.world.entity.Entity;

import java.util.List;

/**
 * Created 2014-12-01 for MarineStandalone
 *
 * @author Citymonstret
 */
public abstract class Command {

    private final String command;
    private final String[] aliases;
    private final String description;

    public Command(String command, String[] aliases, String description) {
        this.command = command.toLowerCase();
        this.aliases = aliases;
        this.description = description;
    }

    @Override
    public String toString() {
        return command;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute(CommandSender sender, String[] arguments);

    public String[] replaceAll(String[] args, CommandSender sender) {
        String[] returner = new String[args.length];
        for(int x = 0; x < args.length; x++) {
            returner[x] = args[x].replace("@p", getClosestPlayer(sender).getName());
            returner[x] = args[x].replace("@a", getAllPlayers());
            returner[x] = args[x].replace("@e", getClosestEntity(sender).toString());
        }
        return returner;
    }

    public Entity getClosestEntity(CommandSender sender) {
        if(sender instanceof Player) return (Entity) sender;
        return null;
    }

    public String getAllPlayers() {
        List<Player> players = Marine.getPlayers();
        StringBuilder s = new StringBuilder();
        Player lastPlayer;
        if(players.size() > 1)
            lastPlayer = players.get(players.size() - 2);
        else
            lastPlayer = null;
        for(Player player : players) {
            if(lastPlayer != null && player == lastPlayer)
                s.append(player.getName()).append(" and ");
            else
                s.append(player.getName()).append(", ");
        }
        return s.toString().substring(0, s.toString().length() - 2);
    }

    public Player getClosestPlayer(CommandSender sender) {
        if(sender instanceof Player) return (Player) sender;
        else return null;
    }
}

package com.marine.game.commands;

import com.marine.game.command.Command;
import com.marine.game.command.CommandSender;
import com.marine.server.Marine;
import com.sun.deploy.util.StringUtils;

import java.util.Arrays;

/**
 * Created 2014-12-06 for MarineStandalone
 *
 * @author Citymonstret
 */
public class Say extends Command {

    public Say() {
        super("say", new String[] {}, "Say something");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        arguments = replaceAll(arguments, sender);
        Marine.broadcastMessage(StringUtils.join(Arrays.asList(arguments), " "));
    }
}

package me.joeleoli.portal.bukkit.command.commands;

import com.google.gson.JsonObject;
import me.joeleoli.portal.bukkit.Portal;
import me.joeleoli.portal.bukkit.command.BaseCommand;
import me.joeleoli.portal.shared.jedis.JedisAction;
import me.joeleoli.portal.shared.jedis.JedisChannel;
import me.joeleoli.portal.shared.queue.Queue;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class QueueToggleCommand extends BaseCommand {

    public QueueToggleCommand() {
        super("queuetoggle");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!commandSender.hasPermission("portal.toggle") && !commandSender.isOp()) {
            commandSender.sendMessage(NO_PERMISSION);
            return true;
        }

        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /queuetoggle <server>");
            return true;
        }

        Queue queue = Queue.getByName(args[0]);

        if (queue == null) {
            commandSender.sendMessage(ChatColor.RED + "That queue does not exist.");
            return true;
        }

        queue.setEnabled(!queue.isEnabled());

        JsonObject json = new JsonObject();
        json.addProperty("queue", queue.getName());

        Portal.getInstance().getPublisher().write(JedisChannel.INDEPENDENT, JedisAction.TOGGLE, json);

        commandSender.sendMessage(ChatColor.GREEN + "Changed status of `" + queue.getName() + "` to " + queue.isEnabled());

        return true;
    }
}

package me.aov.sellgui.commands;

import me.aov.sellgui.SellGUI;
import me.aov.sellgui.SellGUIMain;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SellCommand implements CommandExecutor {
    private final SellGUIMain main;

    private static ArrayList<SellGUI> sellGUIS;

    public SellCommand(SellGUIMain main) {
        this.main = main;
        sellGUIS = new ArrayList<>();
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player) && strings.length == 0) {
            commandSender.sendMessage("Sorry, Player-only Command");
            return true;
        }
        if (!(commandSender instanceof Player) && strings.length == 1 && ifPlayer(strings[0])) {
            sellGUIS.add(new SellGUI(this.main, this.main.getServer().getPlayer(strings[0])));
            return true;
        }
        Player player = (Player) commandSender;
        if (strings.length == 0 && player.hasPermission("sellgui.use")) {
            sellGUIS.add(new SellGUI(this.main, player));
            return true;
        }
        if (strings.length == 1 && player.hasPermission("sellgui.use")) {
            if (strings[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission("sellgui.reload")) {
                    this.main.reload();
                    player.sendMessage(color("&7Configs reloaded."));
                } else {
                    player.sendMessage(color("&8No Permission"));
                }
                return true;
            }
            if (ifPlayer(strings[0])) {
                if (player.hasPermission("sellgui.others")) {
                    sellGUIS.add(new SellGUI(this.main, this.main.getServer().getPlayer(strings[0])));
                    player.sendMessage(color("Opened for player: " + player.getName()));
                } else {
                    player.sendMessage(color("&8No Permission"));
                }
                return true;
            }
            player.sendMessage(this.main.getLangConfig().getString("not-a-command"));
        } else if (!player.hasPermission("sellgui.use")) {
            player.sendMessage(color("&8No Permission"));
        }
        return true;
    }

    public ArrayList<SellGUI> getSellGUIS() {
        return sellGUIS;
    }

    public boolean ifPlayer(String s) {
        for (Player p : this.main.getServer().getOnlinePlayers()) {
            if (p.getName().equalsIgnoreCase(s))
                return true;
        }
        return false;
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static ArrayList<SellGUI> getSellGUIs() {
        return sellGUIS;
    }

    public static boolean isSellGUI(Inventory inventory) {
        for (SellGUI sellGUI : sellGUIS) {
            if (inventory.equals(sellGUI.getMenu()))
                return true;
        }
        return false;
    }

    public static SellGUI getSellGUI(Inventory inventory) {
        for (SellGUI sellGUI : sellGUIS) {
            if (inventory.equals(sellGUI.getMenu()))
                return sellGUI;
        }
        return null;
    }

    public static SellGUI getSellGUI(Player player) {
        for (SellGUI sellGUI : sellGUIS) {
            if (sellGUI.getPlayer().equals(player))
                return sellGUI;
        }
        return null;
    }

    public static boolean openSellGUI(Player player) {
        for (SellGUI sellGUI : sellGUIS) {
            if (sellGUI.getPlayer().equals(player))
                return true;
        }
        return false;
    }
}

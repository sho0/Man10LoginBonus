package red.man10.man10loginbonus;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import red.man10.Man10PlayerDataArchive.Man10PlayerDataArchiveAPI;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public final class Man10LoginBonus extends JavaPlugin implements Listener {

    List<String> commands = new ArrayList<>();
    Man10PlayerDataArchiveAPI pda = null;

    String prefix = "&e&l[&d&lMan10LoginBonus&e&l]&f".replaceAll("&","§");

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        loadCommandsToMemory();
        Bukkit.getPluginManager().registerEvents(this,this);
        pda = new Man10PlayerDataArchiveAPI();
    }

    void loadCommandsToMemory(){
        commands = (List<String>) getConfig().getList("commands");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onJoin(PlayerJoinEvent e){
        if(pda.getLastBonusTime(e.getPlayer().getUniqueId()) != 0) {
            long lastBonus = pda.getLastBonusTime(e.getPlayer().getUniqueId()) / 86400;
            long current = System.currentTimeMillis()/1000/86400;
            if (lastBonus != current) {
                for (int i = 0; i < commands.size(); i++) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.get(i).replaceAll("&", "§").replaceAll("%PLAYER%", e.getPlayer().getName()).replaceAll("%PREFIX%", prefix));
                }
                pda.updateLastBonusTime(e.getPlayer().getUniqueId(), System.currentTimeMillis() / 1000);
            }
            return;
        }
        for (int i = 0; i < commands.size(); i++) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commands.get(i).replaceAll("&", "§").replaceAll("%PLAYER%", e.getPlayer().getName()).replaceAll("%PREFIX%", prefix));
        }
        pda.updateLastBonusTime(e.getPlayer().getUniqueId(), System.currentTimeMillis() / 1000);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("man10loginbonus") || command.getName().equalsIgnoreCase("mlb")){
            if(args.length != 1){
                help(sender);
                return false;
            }
            if(args[0].equalsIgnoreCase("reload")){
                if(!sender.hasPermission("man10.loginbonus.reload")){
                    sender.sendMessage(prefix +"あなたには権限がありません");
                    return false;
                }
                this.reloadConfig();
                loadCommandsToMemory();
                sender.sendMessage(prefix + "コンフィグをリロードしました");
                return false;
            }
            if(args[0].equalsIgnoreCase("help")){
                help(sender);
                return false;
            }
        }
        return false;
    }
    public void help(CommandSender p){
        p.sendMessage("§d§l====" + prefix + "§d§l====");
        p.sendMessage("§6/mlb reload コンフィグのリロード");
        p.sendMessage("§6/blb help ヘルプの表示");
        p.sendMessage("§d============================");
        p.sendMessage("§c§lCreated By Sho0");
        return;
    }
}

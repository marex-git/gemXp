package PearlXP; // By Nebual of nebtown.info March 2012

import java.util.logging.Logger;
import org.bukkit.Server;
import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

public class PearlXP extends org.bukkit.plugin.java.JavaPlugin {
	final String NAME = "PearlXP";
	//final String VER = "1.0"; Edit this in plugin.xml
	
	public PearlXPListener listener;
	Logger logger;
	public static Server server;
	
	public void onEnable() {
		logger = Logger.getLogger("Minecraft");
		server = this.getServer();
		
		if(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml")).getInt("configversion",0) < 1) {saveResource("config.yml",true); reloadConfig();}
		else{getConfig().options().copyDefaults(true);}
	
		listener = new PearlXPListener(this);
		server.getPluginManager().registerEvents(listener, this);

		logger.info(NAME + ": Plugin loading complete. Plugin enabled.");
	}

	//public void onDisable() {}
}
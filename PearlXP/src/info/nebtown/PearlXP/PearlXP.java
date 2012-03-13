package info.nebtown.PearlXP; // By Nebual of nebtown.info March 2012

import java.util.logging.Logger;
import org.bukkit.Server;
import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

public class PearlXP extends org.bukkit.plugin.java.JavaPlugin {
	
	public static final String NAME = "PearlXP";
	
	public PearlXPListener listener;
	Logger logger;
	public static Server server;
	
	// Config
	private static int maxLevel;
	private static int itemId;
	
	

	public void onEnable() {
		logger = Logger.getLogger("Minecraft");
		server = this.getServer();
		
		// Initializing config options
		maxLevel = this.getConfig().getInt("maxlevel");
		itemId = this.getConfig().getInt("itemid");
		
		
		if(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml")).getInt("configversion",0) < 1) {saveResource("config.yml",true); reloadConfig();}
		else{getConfig().options().copyDefaults(true);}
	
		listener = new PearlXPListener(this);
		server.getPluginManager().registerEvents(listener, this);

		logger.info(NAME + ": Plugin loading complete. Plugin enabled.");
	}
	
	//public void onDisable() {}
	
	/**
	 * @return the maxLevel
	 */
	public static int getMaxLevel() {
		return maxLevel;
	}

	/**
	 * @return the itemId
	 */
	public static int getItemId() {
		return itemId;
	}
	
}
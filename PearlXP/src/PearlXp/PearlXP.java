package PearlXp; // By Nebual of nebtown.info March 2012

import java.util.logging.Logger;
import org.bukkit.Server;

public class PearlXP extends org.bukkit.plugin.java.JavaPlugin {
	final String NAME = "PearlXP";
	//final String VER = "1.0"; Edit in plugin.xml
	
	public PearlXPListener listener;
	Logger logger;
	public static Server server;
	
	public void onEnable() {
		logger = Logger.getLogger("Minecraft");

		server = this.getServer();
		getConfig().options().copyDefaults(true);
        saveConfig();
	
		listener = new PearlXPListener(this);
		server.getPluginManager().registerEvents(listener, this);

		logger.info(NAME + ": Plugin loading complete. Plugin enabled.");
	}

	//public void onDisable() {}
}
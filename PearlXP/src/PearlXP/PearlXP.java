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
		//getConfig().options().copyDefaults(true);
        //saveConfig();
		
		/*try {
			if (!(new java.io.File(this.getDataFolder(), "config.yml").exists())) {
				java.io.FileOutputStream writer = new java.io.FileOutputStream(new java.io.File(getDataFolder(), "config.yml"));
				java.io.InputStream out = PearlXP.class.getResourceAsStream("config.yml");
				byte[] linebuffer = new byte[4096];
				int lineLength = 0;
				while((lineLength = out.read(linebuffer)) > 0) {
					writer.write(linebuffer, 0, lineLength);
				}
				writer.close();
			}
		} catch (java.io.IOException e) {}*/
		//if (!(new java.io.File(this.getDataFolder(),"config.yml").exists())) {saveDefaultConfig();}
		if(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml")).getInt("configversion",0) < 1) {saveResource("config.yml",true); reloadConfig();}
		else{getConfig().options().copyDefaults(true);}
	
		listener = new PearlXPListener(this);
		server.getPluginManager().registerEvents(listener, this);

		logger.info(NAME + ": Plugin loading complete. Plugin enabled.");
	}

	//public void onDisable() {}
}
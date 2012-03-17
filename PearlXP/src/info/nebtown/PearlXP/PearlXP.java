/**
 * Rewrites of the original PearlXP created by Nebual of nebtown.info in March 2012.
 * 
 * Small plugin to enable the storage of experience points in an item à la soul gem.
 * 
 * Contributors: Marex, Zonta.
 * 
 * Copyrights belongs to their respective owners.
 */

package info.nebtown.PearlXP;

import java.util.logging.Logger;
import java.io.File;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class PearlXP extends org.bukkit.plugin.java.JavaPlugin {


	/**
	 * Name of the plugin
	 */
	public static final String NAME = "PearlXP";

	/**
	 * Maximum storage capacity of a item.
	 */
	public static final int MAX_STORAGE = 32767; // max of a short

	/****** Configuration options ******/

	/**
	 * Configuration value of the maximum storage capacity
	 */
	private static int maxLevel;

	/**
	 * Configuration value of the item id used
	 */
	private static int itemId;
	
	/**
	 * Configuration value of the item name to display
	 */
	private static String itemName;
	
	/**
	 * Configuration value of the imbue item appearance
	 */
	private static int imbuedItem;

	private static Logger logger;

	@Override
	public void onEnable() {

		logger = Logger.getLogger("Minecraft");
		
		// Check if a config file is missing and create it
		if (YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"))
				.getInt("configversion", 0) < 3) {

			saveResource("config.yml",true);
			reloadConfig();

		} else {
			getConfig().options().copyDefaults(true);
		}

		// Initializing config options
		itemId = this.getConfig().getInt("itemid", Material.ENDER_PEARL.getId());
		setMaxLevel(this.getConfig().getInt("maxlevel", 225));
		itemName = this.getConfig().getString("itemname", "soul gem");
		imbuedItem = (this.getConfig().getInt("imbued_appearance", Material.EYE_OF_ENDER.getId()));

		this.getServer().getPluginManager().registerEvents(new PearlXPListener(), this);

		logger.info(NAME + ": Plugin loading complete. Plugin enabled.");
	}

	@Override
	public void onDisable() {
		logger.info(NAME + ": Plugin disabled.");
	}

	/**
	 * @return the maxLevel
	 */
	public static int getMaxLevel() {
		return maxLevel;
	}

	/**
	 * @return the logger
	 */
	public static Logger getPluginLogger() {
		return logger;
	}

	/**
	 * @return the itemId
	 */
	public static int getItemId() {
		return itemId;
	}

	/**
	 * @return the itemName
	 */
	public static String getItemName() {
		return itemName;
	}

	/**
	 * @param maxLevel the maxLevel to set
	 */
	public static void setMaxLevel(int maxLevel) {

		// check if maxLevel fits in a short (2^15 - 1)
		if (maxLevel > MAX_STORAGE) {
			PearlXP.maxLevel = MAX_STORAGE;
			logger.info(NAME+ ": WARNING: maxLevel exceeds possible limits! Please modify your config file.");
			logger.info(NAME+ ": Setting maxLevel to " + maxLevel);
		} else { 
			PearlXP.maxLevel = maxLevel;
		}
	}

	/**
	 * @return the imbuedItem
	 */
	public static int getImbuedItem() {
		return imbuedItem;
	}


}
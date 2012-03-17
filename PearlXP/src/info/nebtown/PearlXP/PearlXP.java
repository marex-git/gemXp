/**
 * Rewrites of the original PearlXP created by Nebual of nebtown.info in March 2012.
 * 
 * Small plugin to enable the storage of experience points in an item � la soul gem.
 * 
 * Contributors: Marex, Zonta.
 * 
 * Copyrights belongs to their respective owners.
 */

package info.nebtown.PearlXP;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;

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

	private int maxLevel;
	private int itemId;
	private String itemName;
	private int imbuedItem;

	// the plugin logger
	private Logger logger;

	@Override
	public void onEnable() {

		logger = Logger.getLogger("Minecraft");
		loadConfig();
		new PearlXPListener(this);

		logInfo("Plugin loading complete. Plugin enabled.");
	}

	@Override
	public void onDisable() {
		logInfo("Plugin disabled.");
	}

	/**
	 * Load the default configuration files and set the variables accordingly
	 */
	public void loadConfig() {
		Configuration config = this.getConfig();
		String itemName;

		if(config.getInt("configversion", 0) < 3) {
			saveResource("config.yml", true);
		}

		setMaxLevel(config.getInt("max_level"));
		setItemId(config.getInt("item_id"));
		
		// take the default item name if no config exists
		itemName = Material.getMaterial(this.getItemId()).toString();
		setItemName(config.getString("item_name", itemName.toLowerCase()));
		
		// no change of appearance if this config doesn't exists
		setImbuedItem(config.getInt("imbued_appearance", this.getItemId()));

	}

	/**
	 * Log information to the console with the "Plugin name: " prefix
	 * @param s
	 */
	protected void logInfo(String s) {
		getPluginLogger().info("[" + NAME + "] " + s);
	}

	/**
	 * @return the logger
	 */
	private Logger getPluginLogger() {
		return logger;
	}

	/**
	 * @return the maxLevel
	 */
	public int getMaxLevel() {
		return maxLevel;
	}

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}


	/**
	 * @return the imbuedItem
	 */
	public int getImbuedItem() {
		return imbuedItem;
	}

	/**
	 * @param maxLevel the maxLevel to set
	 */
	private void setMaxLevel(int maxLevel) {
		// check if maxLevel fits in a short (2^15 - 1)
		if (maxLevel > MAX_STORAGE) {
			this.maxLevel = MAX_STORAGE;
			logInfo("WARNING: maxLevel exceeds possible limits! Please modify your config file.");
			logInfo("Setting maxLevel to " + maxLevel);
		} else { 
			this.maxLevel = maxLevel;
		}
	}

	private void setItemId(int i) {
		this.itemId = i;

	}

	/**
	 * @param itemName the itemName to set
	 */
	private void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * @param imbuedItem the imbuedItem to set
	 */
	private void setImbuedItem(int imbuedItem) {
		this.imbuedItem = imbuedItem;
	}


}
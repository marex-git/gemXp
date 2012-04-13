/**
 * Small plugin to enable the storage of experience points in an item.
 * 
 * Rewrite of the original PearlXP created by Nebual of nebtown.info in March 2012.
 * 
 * rewrite by: Marex, Zonta.
 * 
 * contact us at : plugins@makeitonthe.net
 * 
 * Copyright (C) 2012 belongs to their respective owners
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.makeitonthe.GemXp;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class GemXp extends org.bukkit.plugin.java.JavaPlugin {

	private static final Logger LOGGER = Logger.getLogger("Minecraft");
	private static final String LOGGER_PREFIX = "[GemXP]";

	/****** Configuration options ******/

	private List<String> messages;

	public enum MsgKeys { 

		INFO_XP("info_xp_content"),
		INFO_XP_EMPTY("info_xp_empty"),
		IMBUE_XP("filled_xp"),
		RESTORE_XP("restore_xp");

		private String key;

		MsgKeys(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	@Override
	public void onEnable() {
		loadConfig();
		new GemXpListener(this);
		new GemPickupListener(this);
		new InventoryListener(this);

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
		ConfigurationSection msgSection;
		String itemName;

		if (getConfig().getInt("configversion", 0) < 3) {
			saveResource("config.yml", true);
			logInfo("New config file created, you should check if your " +
					"configurations are correct!");
			reloadConfig();
		}

		// set the max experience level and check if it exceeded it...
		if (!XpContainer.setMaxExp(getConfig().getInt("max_level"))) {
			log(Level.WARNING, "maxLevel exceeds possible limits! Please modify your config file.");
			logInfo("Setting maxLevel to " + XpContainer.MAX_STORAGE);
		}

		XpContainer.setItemId(getConfig().getInt("item_id"));

		// take the default item name if no config exists
		itemName = Material.getMaterial(XpContainer.getItemId()).toString();
		XpContainer.setItemName(getConfig().getString("item_name", itemName.toLowerCase()));

		// no change of appearance if this config doesn't exists
		XpContainer.setImbuedItemId(getConfig().getInt("filled_appearance", XpContainer.getItemId()));

		XpContainer.setMaxStackSize(getConfig().getInt("max_gem_stack_size"));

		// set the default value if the tax doesn't make sense
		if (getConfig().getDouble("xp_tax") > XpContainer.MAX_TAX) {
			XpContainer.setXpTax((getConfig().getDefaults().getDouble("xp_tax")/100.0));
			log(Level.WARNING, "xp_tax exceeds possible limits! Please modify your config file.");
			logInfo("Setting xp_tax to " + getConfig().getDefaults().getDouble("xp_tax") + "%");
		} else {
			XpContainer.setXpTax((getConfig().getDouble("xp_tax")/100.0));
		}

		// Loading custom texts
		msgSection = getConfig().getConfigurationSection("Messages");
		messages = new ArrayList<String>();

		if (msgSection != null) {
			for (MsgKeys key : MsgKeys.values()) {
				messages.add(msgSection.getString(key.getKey(), null));
			}
		}

	}

	/**
	 * Log information to the console with the plugin prefix
	 * @param msg text to log
	 */
	public void logInfo(String msg) {
		log(Level.INFO, msg);
	}

	/**
	 * Log information with given level with the plugin prefix
	 * @param level
	 * @param msg text to log
	 */
	public void log(Level level, String msg) {
		LOGGER.log(level, String.format("%s %s", LOGGER_PREFIX, msg));
	}

	/**
	 * @return the config message text
	 */
	public String getMessage(MsgKeys key) {
		String msg = null;
		int i = 0;

		for (MsgKeys k : MsgKeys.values()) {
			if (key == k) {
				msg = messages.get(i);
			}
			i += 1;
		}

		return msg;
	}
}

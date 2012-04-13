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

import net.makeitonthe.GemXp.GemXp.MsgKeys;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class GemInteractListener implements Listener {

	private static final ChatColor TEXT_COLOR = ChatColor.BLUE;
	private static final ChatColor INFO_COLOR = ChatColor.AQUA;

	// messages
	private String infoXpMsg;
	private String infoXpEmptyMsg;
	private String imbueXpMsg;
	private String restoreXpMsg;
	private GemXp plugin;

	public GemInteractListener(GemXp plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);

		this.infoXpMsg = plugin.getMessage(MsgKeys.INFO_XP);
		this.infoXpEmptyMsg = plugin.getMessage(MsgKeys.INFO_XP_EMPTY);
		this.imbueXpMsg = plugin.getMessage(MsgKeys.IMBUE_XP);
		this.restoreXpMsg = plugin.getMessage(MsgKeys.RESTORE_XP);
		this.plugin = plugin;
	}


	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		XpContainer gem;
		Player player = event.getPlayer();
		Action action = event.getAction();
		int xp = 0;
		double xpTaxed = 0;


		if (event.hasItem() && XpContainer.isAnXpContainer(event.getItem())) {

			gem = new XpContainer(event.getItem());

			if (gem.canStoreXp() && gem.getStoredXp() == 0) { // The item possess no XP

				if (action == Action.RIGHT_CLICK_BLOCK) {
					// the item is empty and the player clicked "on is feet"
					// Show the amount of XP stored

					event.setUseItemInHand(Result.DENY); //Don't throw the item!
					sendInfo(infoXpEmptyMsg, INFO_COLOR, player, gem);

				} else if (player.getTotalExperience() > 0
						&& (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
					// Store some XP in the item

					if (player.getTotalExperience() > XpContainer.getmaxExp() + XpContainer.getmaxExp() * XpContainer.getXpTax()) {

						xp = XpContainer.getmaxExp();
						xpTaxed = xp * XpContainer.getXpTax();
					} else {

						xp = player.getTotalExperience();
						xpTaxed = xp * XpContainer.getXpTax();
						xp = xp - (int)(xpTaxed);
					}

					gem = storeXp(xp, gem, player);
					removePlayerXp((int) (xp + xpTaxed), player);

					// Friendly message !
					sendInfo(imbueXpMsg, player, gem);

					// Visual and sound effects
					player.getWorld().playEffect(player.getEyeLocation(), Effect.ENDER_SIGNAL, 0);
					player.playEffect(player.getEyeLocation(), Effect.EXTINGUISH, 0);
				}

			} else if (gem.canContainXp()) {

				if (gem.getStoredXp() > 0 && action == Action.RIGHT_CLICK_AIR 
						|| action == Action.RIGHT_CLICK_BLOCK) {
					// Show the stored XP...

					event.setUseItemInHand(Result.DENY); //Don't throw the item!

					if (gem.getStoredXp() == 0) {
						sendInfo(infoXpEmptyMsg, INFO_COLOR, player, gem);
					} else {
						sendInfo(infoXpMsg, INFO_COLOR, player, gem);
					}


				} else if (gem.getStoredXp() > 0 
						&& (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
					// Restore XP to the player

					xp = gem.getStoredXp();

					// Remove all Stored XP and give it
					storeXp(0, gem, player);
					player.giveExp(xp);
					sendInfo(restoreXpMsg, player, gem);

					// Special effects!
					player.playEffect(player.getEyeLocation(), Effect.GHAST_SHOOT, 0);
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2, 1));
					player.getWorld().playEffect(player.getEyeLocation(), Effect.SMOKE, BlockFace.SELF);
				}
			}

		}

	} //onPlayerInteract


	/**
	 * Format the message to add variables values
	 * @param msg message
	 * @param xp item xp
	 * @param playerXp player total xp
	 * @return the modified string
	 */
	private String formatMsg(String msg, int xp, int playerXp) {
		String[] values = { XpContainer.getItemName(),
				String.valueOf(xp),
				String.valueOf(playerXp) };

		return formatMsg(msg, values);
	}


	/**
	 * Format the message to add variables values
	 * @param msg message
	 * @param values Array of the values to display
	 * @return the modified string
	 */
	private String formatMsg(String msg, String[] values) {
		String[] keys = { "item_name", "xp", "player_xp" };

		if (msg != null) {
			for (int i = 0; i < keys.length && i < values.length; i++) {
				msg = msg.replaceAll("\\$\\{" + keys[i] + "\\}", values[i]);
			}
		}

		return msg;
	}


	/**
	 * Send the player an information message with the default text color.
	 * @param s message
	 * @param p player to inform
	 */
	private void sendInfo(String msg, Player p, XpContainer i) {
		if (msg != null) {
			sendInfo(msg, TEXT_COLOR, p, i);
		}
	}


	/**
	 * Send the player an information message with the specified text color.
	 * @param s message
	 * @param p player to inform
	 */
	private void sendInfo(String msg, ChatColor c, Player p, XpContainer i) {
		if (msg != null) {
			p.sendMessage(c + formatMsg(msg, i.getStoredXp(), p.getTotalExperience()));
		}
	}


	/**
	 * Remove a number of XP from a given player
	 * @param xp the XP to remove
	 * @param p player
	 */
	private void removePlayerXp(int xp, Player p) {
		int currentXp = p.getTotalExperience();

		// Reset level to fix update bug
		p.setTotalExperience(0);
		p.setExp(0);
		p.setLevel(0);

		p.giveExp(currentXp - xp);

	}


	/**
	 * Store the given amount of XP in the item. If other uncompleted stack
	 * exists with the correct XP the method stack them together.
	 * 
	 * @param item ItemStack to store XP
	 * @param xp experience points
	 * @param inv inventory of the player
	 */
	private XpContainer storeXp(int xp, XpContainer item,  Player player) {
		XpContainer similarStack;
		XpContainer newGem;
		PlayerInventory inv = player.getInventory();
		int slot = inv.firstEmpty();
		Item droppedItem;
		Vector lookingVector;

		newGem = new XpContainer(item.clone());
		newGem.setStoredXp(xp);
		similarStack = newGem.findSimilarStack(inv);

		if (item.getAmount() == 1 && similarStack == null) {

			inv.setItemInHand(newGem);

		} else { // We can unstack stuff!

			if (similarStack != null) {
				// Stack on top of

				similarStack.setAmount(similarStack.getAmount() + 1);

			} else { // no similar stack

				// Only create one item...
				newGem.setAmount(1);

				if (slot >= 0) {

					inv.setItem(slot, newGem);

				} else {
					// The item is in a stack and cannot be unstack
					droppedItem = player.getWorld().dropItem(player.getEyeLocation().subtract(new Vector(0, 0.2, 0)), newGem);
					plugin.getServer().getPluginManager().callEvent(new PlayerDropItemEvent(player, droppedItem));

					// We drop the item where the player is looking
					lookingVector = getLookingVector(player);
					lookingVector.multiply(0.4);
					droppedItem.setVelocity(lookingVector);
				}
			}

			// Remove the item used
			if (item.getAmount() == 1) {
				inv.setItemInHand(null);
			} else {
				item.setAmount(item.getAmount() - 1);

			}
		}

		return newGem;

	}


	/**
	 * Return a normalized vector representing where a player is looking
	 * @param player
	 * @return looking normalized vector
	 */
	private Vector getLookingVector(Player player) {
		Block lookingBlock = player.getTargetBlock(null, 25);
		Vector v1 = new Vector(lookingBlock.getX() - player.getEyeLocation().getX(),
				lookingBlock.getY() - player.getEyeLocation().getY(),
				lookingBlock.getZ() - player.getEyeLocation().getZ());

		return v1.normalize();
	}

}
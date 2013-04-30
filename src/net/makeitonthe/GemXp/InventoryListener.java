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

import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {

	private static final int QUICKBAR_SLOT_NB = 9;
	private GemXp plugin;

	public InventoryListener(GemXp plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}


	@EventHandler
	public void onInventoryClicked(InventoryClickEvent event) {
		XpContainer clickedGem;
		XpContainer cursorGem;
		Inventory inv;
		int transfertQty;
		int startSlot;
		int endSlot;

		// we ignore throwing items and non soul gems items...
		if(plugin.getGemFactory().isAFilledGem(event.getCurrentItem())
				&& event.getSlotType() != InventoryType.SlotType.OUTSIDE) {

			inv = event.getInventory();
			clickedGem = plugin.getGemFactory().make(event.getCurrentItem());

			if (event.isShiftClick()) {
				// Transfert stacks to the other "section" of an inventory
				event.setCancelled(true);

				if (inv.getType() == InventoryType.CHEST
						|| inv.getType() == InventoryType.DISPENSER) {
					// In chest and dispenser we switch between player and entity inventory
					// TODO Reverse iterate to match minecraft implementation better

					if (event.getRawSlot() <= inv.getSize()) {
						// The player clicked outside is own inventory
						// We transfer in it...
						inv = event.getWhoClicked().getInventory();
					}

					startSlot = 0;
					endSlot = inv.getSize();

					GemInventory.stackGems(clickedGem, event, inv, startSlot, endSlot);

				} else if (event.getSlotType() == InventoryType.SlotType.RESULT) {
					// The player crafted gems
					plugin.log(Level.WARNING, "Crafting gems is not supported");

				} else {
					// Use the player inventory
					inv = event.getWhoClicked().getInventory();

					// We alternate between the Quickbar and the main inventory block
					if (event.getSlotType() == InventoryType.SlotType.QUICKBAR) {
						startSlot = QUICKBAR_SLOT_NB;
						endSlot = inv.getSize();
					} else {
						startSlot = 0;
						endSlot = QUICKBAR_SLOT_NB;
					}

					// Stack all gems and transfer the rest in empty slots...
					GemInventory.stackGems(clickedGem, event, inv, startSlot, endSlot);
				}



			} else if (plugin.getGemFactory().isAFilledGem(event.getCursor())) {
				// the gem is click with another gem
				event.setCancelled(true);
				cursorGem = plugin.getGemFactory().make(event.getCursor());

				//check if stacking is possible and stack, leftover are on cursor, if not switch the item
				if (event.isLeftClick()) {
					transfertQty = cursorGem.getAmount();
				} else { // right clicked
					transfertQty = 1;
				}

				if (cursorGem.equals(clickedGem)) {
					if (GemInventory.transferGems(cursorGem, clickedGem, transfertQty)) {
						event.setCursor(null);
					}

				} else {
					// Switch items
					event.setCursor(clickedGem);
					event.setCurrentItem(cursorGem);
				}
			}
		}
	}
}
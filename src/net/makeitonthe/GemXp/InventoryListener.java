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
import org.bukkit.inventory.ItemStack;

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
		if(XpContainer.isAFilledXpContainer(event.getCurrentItem())
				&& event.getSlotType() != InventoryType.SlotType.OUTSIDE) {

			inv = event.getInventory();
			clickedGem = new XpContainer(event.getCurrentItem());

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

					stackGems(clickedGem, event, inv, startSlot, endSlot);

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
					stackGems(clickedGem, event, inv, startSlot, endSlot);
				}



			} else if (XpContainer.isAFilledXpContainer(event.getCursor())) {
				// the gem is click with another gem
				event.setCancelled(true);
				cursorGem = new XpContainer(event.getCursor());

				//check if stacking is possible and stack, leftover are on cursor, if not switch the item
				if (event.isLeftClick()) {
					transfertQty = cursorGem.getAmount();
				} else { // right clicked
					transfertQty = 1;
				}

				if (cursorGem.equals(clickedGem)) {
					transfertGems(cursorGem, clickedGem, inv, event, transfertQty, true);

				} else {
					// Switch items
					event.setCursor(clickedGem);
					event.setCurrentItem(cursorGem);
				}
			}

		}

	}


	/**
	 * Transfert all possible gems in the gemToTransfert stack into another stack of gems.
	 * @param gemToTransfert
	 * @param gemStack
	 * @param inv inventory
	 * @param event {@link InventoryClickEvent} that triggered the transfert
	 * @param onCursor if the item to transfert is on the cursor
	 * @return true if all the items where put into the gemStack
	 */
	private boolean transfertGems(XpContainer gemToTransfert, XpContainer gemStack, Inventory inv, InventoryClickEvent event, boolean onCursor) {
		return transfertGems(gemToTransfert, gemStack, inv, event, gemToTransfert.getAmount(), onCursor);
	}


	/**
	 * Transfert all possible gems in the gemToTransfert stack into another stack of gems.
	 * @param gemToTransfert
	 * @param gemStack
	 * @param inv inventory
	 * @param event {@link InventoryClickEvent} that triggered the transfert
	 * @param quantity quantity to transfert
	 * @param onCursor if the item to transfert is on the cursor
	 * @return true if all the items where put into the gemStack
	 */
	private boolean transfertGems(XpContainer gemToTransfert, XpContainer gemStack, Inventory inv, InventoryClickEvent event, int quantity ,boolean onCursor) {
		int transfertQty = 0;
		boolean removedAll = false;


		// Check the maximum possible to transfert
		if (gemStack.getAmount() + quantity <= gemStack.getMaxStackSize()) {

			transfertQty = quantity;
			if (quantity == gemToTransfert.getAmount()) {
				// We remove the stack completly
				removedAll = true;

				if (onCursor) {
					event.setCursor(null);
				} else {
					event.setCurrentItem(null);
				}
			}

		} else {
			transfertQty = gemStack.getMaxStackSize() - gemStack.getAmount();
		}

		if (!removedAll) {
			gemToTransfert.setAmount(gemToTransfert.getAmount() - transfertQty);
		}

		gemStack.setAmount(gemStack.getAmount() + transfertQty);

		return removedAll;
	}


	/**
	 * Find the first empty slot between the start and end index as start <= x < end
	 * @param inv inventory
	 * @param start index to start iterate
	 * @param end index to end the iteration
	 * @return the fist empty slot of the inventory
	 */
	private int firstEmptySlot(Inventory inv, int start, int end) {
		ItemStack[] items = inv.getContents();
		int slot = -1;
		int i = start;

		while(slot == -1 && i < end) {
			if (items[i] == null) {
				slot = i;
			}

			i++;
		}

		return slot;
	}


	/**
	 * Stack gemToStack gems into all possible stack between the start and end
	 * index (start <= x < end); if no stack is available the stack is placed in
	 * first empty space found.
	 *
	 * @param gemToStack
	 * @param event {@link InventoryClickEvent} that triggered this action
	 * @param inv inventory
	 * @param start start index
	 * @param end end index
	 */
	private void stackGems(XpContainer gemToStack, InventoryClickEvent event, Inventory inv, int start, int end) {
		boolean finish = false;
		XpContainer similarGem;
		int emptySlot = firstEmptySlot(inv, start, end);

		while (!finish) {
			similarGem = gemToStack.findSimilarStack(inv, start, end);

			if (similarGem != null) {
				finish = transfertGems(gemToStack, similarGem, inv, event, false);

			} else {
				if (emptySlot >= 0) {
					inv.setItem(emptySlot, gemToStack);
					event.setCurrentItem(null); // remove the transfered gem
				}

				finish = true;
			}
		}
	}
}

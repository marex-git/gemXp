package net.makeitonthe.GemXp;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class GemInventory {
	/**
	 * Transfer all possible gems in the gemToTransfert stack into another stack of gems.
	 * @param gemToTransfert
	 * @param similarItem
	 * @return true if all the items where put into the gemStack
	 */
	static public boolean transferGems(XpContainer gemToTransfert, ItemStack similarItem) {
		return transferGems(gemToTransfert, similarItem, gemToTransfert.getAmount());
	}


	/**
	 * Transfer all possible gems in the gemToTransfert stack into another stack of gems.
	 * @param gemToTransfert
	 * @param similarItem
	 * @param quantity quantity to transfer
	 * @return true if all the items where put into the gemStack
	 */
	static public boolean transferGems(XpContainer gemToTransfert, ItemStack similarItem, int quantity) {
		int transfertQty = 0;
		boolean removedAll = false;


		// Check the maximum possible to transfer
		if (similarItem.getAmount() + quantity <= similarItem.getMaxStackSize()) {

			transfertQty = quantity;
			if (quantity == gemToTransfert.getAmount()) {
				// We remove the stack completely
				removedAll = true;
			}

		} else {
			transfertQty = similarItem.getMaxStackSize() - similarItem.getAmount();
		}

		if (!removedAll) {
			gemToTransfert.setAmount(gemToTransfert.getAmount() - transfertQty);
		}

		similarItem.setAmount(similarItem.getAmount() + transfertQty);

		return removedAll;
	}


	/**
	 * Find the first empty slot between the start and end index as start <= x < end
	 * @param inv inventory
	 * @param start index to start iterate
	 * @param end index to end the iteration
	 * @return the fist empty slot of the inventory
	 */
	static public int firstEmptySlot(Inventory inv, int start, int end) {
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
	static public void stackGems(XpContainer gemToStack, InventoryClickEvent event, Inventory inv, int start, int end) {
		boolean finish = false;
		ItemStack similarGem;
		int emptySlot = firstEmptySlot(inv, start, end);

		while (!finish) {
			similarGem = findSimilarStack(inv, gemToStack, start, end);

			if (similarGem != null) {
				finish = transferGems(gemToStack, similarGem);

			} else {
				if (emptySlot >= 0) {
					inv.setItem(emptySlot, gemToStack);
					event.setCurrentItem(null); // remove the transfered gem
				}

				finish = true;
			}
		}
	}

	/**
	 * Find first not full stack with the same property. Return null if nothing
	 * found.
	 *
	 * @param stack {@link XpContainer} with the property looking for
	 * @param inv inventory
	 * @return ItemStack found
	 */
	static public ItemStack findSimilarStack(Inventory inv, ItemStack item) {
		return findSimilarStack(inv, item,0, inv.getSize());
	}


	/**
	 * Find the first not full stack of XpContainer with the same property starting at start
	 * and ending at the index stop.
	 *
	 * @param stack {@link XpContainer} with the property looking for
	 * @param inv inventory
	 * @param start the index to start the search
	 * @param stop
	 * @return the XpContainer found or null if nothing found
	 */
	static public ItemStack findSimilarStack(Inventory inv, ItemStack item, int start, int stop) {
		ItemStack[] items = inv.getContents();
		ItemStack stack = null;
		boolean found = false;

		if (stop > items.length) stop = items.length;

		for (int i = start; i < stop && !found; i++) {
			if (items[i] != null) {
				stack = new XpContainer(items[i]);

				if (stack.getAmount() < stack.getMaxStackSize() && stack.equals(item)) {
					found = true;
				}
			}
		}

		return found ? stack : null;
	}
}

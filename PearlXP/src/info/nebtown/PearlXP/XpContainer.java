/**
 * Rewrite of the original PearlXP created by Nebual of nebtown.info in March 2012.
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
package info.nebtown.PearlXP;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class XpContainer extends ItemStack {

	/**
	 * Maximum storage capacity of a item
	 */
	public static int MAX_STORAGE = 32767; // Max of a short

	/**
	 * Enchantement used to store experience points
	 */
	public static Enchantment enchantment = Enchantment.OXYGEN;

	private static int itemId;
	private static int imbuedItemId;
	private static int maxExp;
	private static String itemName;

	private ItemStack itemStack;

	public XpContainer(ItemStack stack) {
		super(stack);
		this.itemStack = stack;

	}




	/**
	 * Return true if the ItemStack has the capability of storing experience points
	 * @return true if it can store XP, false otherwise
	 */
	public static boolean isAnXpContainer(ItemStack stack) {
		int itemId = stack.getTypeId();

		return itemId == getImbuedItemId() || itemId == getItemId();
	}

	/**
	 * Return true if the ItemStack has the capability of containing experience points
	 * @return true if it can contain XP, false otherwise
	 */
	public boolean canContainXp() {
		return getTypeId() == getImbuedItemId();
	}

	/**
	 * Return true if the ItemStack has the capability of storing experience points
	 * @return true if it can store XP, false otherwise
	 */
	public boolean canStoreXp() {
		return getTypeId() == getItemId();
	}

	/**
	 * Return the amount of experience points stored in the item
	 * @param item
	 * @return xp experience points
	 */
	public int getStoredXp() {
		int xp = 0;

		if (containsEnchantment(enchantment))
			xp = getEnchantmentLevel(enchantment);

		return xp;
	}

	/**
	 * Set the amount of stored experience points in the itemstack to xp (mutable)
	 * @param xp new stored experience points
	 * @param item ItemStack getting modified
	 */
	public void setStoredXp(int xp) {
		// check for overflow
		if (xp > getmaxExp()) {
			xp = getmaxExp();
		}

		if (xp == 0) {
			removeEnchantment(enchantment);
			setTypeId(getItemId()); // Change appearance
		} else {
			addUnsafeEnchantment(enchantment, xp);
			setTypeId(getImbuedItemId()); // Change appearance
		}
	}


	/**
	 * @return the itemId used as the empty containers
	 */
	public static int getItemId() {
		return itemId;
	}


	/**
	 * @return the ItemId used as the containers
	 */
	public static int getImbuedItemId() {
		return imbuedItemId;
	}


	/**
	 * @return the maximum level cap of the containers
	 */
	public static int getmaxExp() {
		return maxExp;
	}


	/**
	 * @return the itemName of the containers
	 */
	public static String getItemName() {
		return itemName;
	}


	/**
	 * @return the stack
	 */
	public ItemStack getItemStack() {
		return itemStack;
	}


	/**
	 * @param imbuedItemId the imbuedItemId to set
	 */
	protected static void setImbuedItemId(int imbuedItemId) {
		XpContainer.imbuedItemId = imbuedItemId;
	}


	/**
	 * @param itemId the itemId to set
	 */
	protected static void setItemId(int itemId) {
		XpContainer.itemId = itemId;
	}


	/**
	 * @param maxExp the maximum experience points cap  to set
	 */
	protected static boolean setMaxExp(int maxExp) {
		boolean result = false;
		// check if maxLevel fits in a short (2^15 - 1)
		if (maxExp > MAX_STORAGE) {
			XpContainer.maxExp = MAX_STORAGE;
		} else { 
			XpContainer.maxExp = maxExp;
			result = true;
		}
		return result;
	}


	/**
	 * @param itemName the itemName to set
	 */
	protected static void setItemName(String itemName) {
		XpContainer.itemName = itemName;
	}


}

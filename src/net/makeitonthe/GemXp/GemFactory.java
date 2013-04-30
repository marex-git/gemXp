/**
 * Small plugin to enable the storage of experience points in an item.
 *
 * Fork of the original PearlXP created by Nebual of nebtown.info in March 2012.
 *
 * Fork by: Marex, Zonta.
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

import org.bukkit.inventory.ItemStack;


public class GemFactory {

	private int itemId;
	private int imbuedItemId;
	private int maxExp;

	private String itemName;
	private String itemHint;

	private double xpTax;
	private int stackSize;

	private XpContainer gem;

	public GemFactory(int itemId, int imbuedItemId, String itemName, String itemHint, double xpTax, int maxExp, int stackSize) {
		this.itemId = itemId;
		this.imbuedItemId = imbuedItemId;
		this.maxExp = maxExp;

		this.itemName = itemName;
		this.itemHint = itemHint;

		this.xpTax = xpTax;
		this.stackSize = stackSize;

		this.gem = new XpContainer(itemId, imbuedItemId, itemName, itemHint, xpTax, maxExp, stackSize);
	}


	/**
	 * Create a new XpContainer object with the right attributes
	 * @param stack to be gemified
	 * @return XpContainer gem
	 */
	public XpContainer make(ItemStack stack) {
		return new XpContainer(stack, itemId, imbuedItemId, itemName, itemHint, xpTax, maxExp, stackSize);
	}

	/**
	 * Create a new XpContainer object with the right attributes and with amount quantity
	 * @param stack to be gemified
	 * @param amount in the XpContainer stack
	 * @return XpContainer gem
	 */
	public XpContainer make(ItemStack stack, int amount) {
		return new XpContainer(stack, itemId, imbuedItemId, itemName, itemHint, xpTax, maxExp, stackSize, amount);
	}

	/**
	 * Test if the stack is a gem
	 * @param stack
	 * @return true if the stack can be a gem
	 */
	public boolean isAGem(ItemStack stack) {
		return gem.isAnXpContainer(stack);
	}

	/**
	 * Test if the stack is a filled gem
	 * @param stack
	 * @return true if the stack can be a filled gem
	 */
	public boolean isAFilledGem(ItemStack stack) {
		return gem.isAFilledXpContainer(stack);
	}

	public String getItemName() {
		return itemName;
	}

}

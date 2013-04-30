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


	public XpContainer make(ItemStack stack) {
		return new XpContainer(stack, itemId, imbuedItemId, itemName, itemHint, xpTax, maxExp, stackSize);
	}

	public boolean isAGem(ItemStack stack) {
		return gem.isAnXpContainer(stack);
	}

	public boolean isAFilledGem(ItemStack stack) {
		return gem.isAFilledXpContainer(stack);
	}

	public String getItemName() {
		return itemName;
	}

}

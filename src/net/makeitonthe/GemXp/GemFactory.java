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

package info.nebtown.PearlXP;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.event.block.Action;

public class PearlXPListener implements Listener {
	
	
	
	private static String itemName = "pearl";
	private static ChatColor textColor = ChatColor.BLUE;
	private static Enchantment enchantment = Enchantment.OXYGEN;
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack item;
		Action action = event.getAction();
		
		int xpToStore = 0;
		String storeMsg = "-Imbued this " + itemName + " with ";
		
		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();
		
		if (event.hasItem() && event.getItem().getTypeId() == PearlXP.getItemId()) {
			
			item = event.getItem();
			
				if (getStoredXp(item) == 0) {
					
					if (action == Action.RIGHT_CLICK_BLOCK) {
						// Show the amount of XP stored
						
						event.setUseItemInHand(Result.DENY); //Don't throw the item!
						
						// the item is empty and the player clicked "on is feet"
						sendInfo("This " + itemName + " is empty.", player);
						
					} else if (player.getTotalExperience() > 0 
							&& (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
						// Store some XP in the item
						
						// Unstack the item
						if (item.getAmount() > 1) item = unStack(item, inventory);
						
						if (player.getTotalExperience() > PearlXP.getMaxLevel()) {
							
							xpToStore = PearlXP.getMaxLevel();
							storeMsg +=  xpToStore + " XP! " + player.getTotalExperience() + "XP left!";
						} else {
							
							xpToStore = player.getTotalExperience();
							storeMsg += xpToStore + " XP!";
						}
						
						setStoredXp(xpToStore, item);
						removePlayerXp(xpToStore, player);
						
						// Friendly message !
						sendInfo(storeMsg, player);
						
						// Visual and sound effects
						player.getWorld().playEffect(player.getEyeLocation(), Effect.ENDER_SIGNAL, 0);
						player.playEffect(player.getEyeLocation(), Effect.EXTINGUISH, 0);
					}
						
				} else { // Contains XP
					
					if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
						
						// Unstack the item
						if (item.getAmount() > 1) item = unStack(item, inventory);
						
						player.giveExp(getStoredXp(item));
						
						sendInfo("+Restoring " + getStoredXp(item) + " XP! You now have " 
									+ player.getTotalExperience() + " XP!", player);

						// Remove all Stored xp
						setStoredXp(0, item);
							
					} else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
						// Show the imbued xp...
						
						event.setUseItemInHand(Result.DENY); //Don't throw the item!
						
						sendInfo("This " + itemName + " is imbued with "
								+ getStoredXp(item) + " XP!", player);
					}
				}
		
		}
		
	} //onPlayerInteract
	
	/**
	 * Send the player a information text with the default text color.
	 * @param s message
	 * @param p player to inform
	 */
	private void sendInfo(String s, Player p) {
		p.sendMessage(textColor + s);
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
	 * Unstack one item in the inv inventory.
	 * @param stack ItemStack to remove one item
	 * @param inv inventory
	 */
	private ItemStack unStack(ItemStack stack, PlayerInventory inv) {
		int slot = inv.firstEmpty();
		ItemStack newItem = stack.clone();
		
		// Only create one item...
		newItem.setAmount(1);
		
		stack.setAmount(stack.getAmount() - 1);
		inv.setItem(slot, newItem);
		
		return inv.getItem(slot);
	}
	
	/**
	 * Return the amount of experience points stored in the item
	 * @param item
	 * @return xp experience points
	 */
	private int getStoredXp(ItemStack item) {
		int xp = 0;
		
		if (item.containsEnchantment(enchantment))
			xp = item.getEnchantmentLevel(enchantment);

		return xp;
	}
	
	/**
	 * Set the amount of stored experience points in the item i to xp
	 * @param xp new stored experience points
	 * @param item
	 */
	private void setStoredXp(int xp, ItemStack item) {
		// check for overflow
		if (xp > PearlXP.getMaxLevel()) {
			xp = PearlXP.getMaxLevel();
		}
		
		if (xp == 0) {
			item.removeEnchantment(enchantment);
		} else {
			item.addUnsafeEnchantment(enchantment, xp);
		}
	}
	
} //class
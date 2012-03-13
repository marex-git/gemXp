package info.nebtown.PearlXP;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.block.Action;

public class PearlXPListener implements Listener {
	
	
	
	private static String itemName = "pearl";
	private static ChatColor textColor = ChatColor.BLUE;

	public PearlXPListener() {

	}
	
	
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		
		int maxLevel = PearlXP.getMaxLevel();
		int playerXp;
		
		Player player = event.getPlayer();
		
		if (item != null && item.getTypeId() == PearlXP.getItemId()) {
			
			
			if (item.getAmount() == 1 && (event.getAction() == Action.RIGHT_CLICK_AIR
					|| event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
				
				
				
				if (item.getDurability() != 0) {
					// the item have stored XP
					
					event.setCancelled(true); // keep the item !
					
					sendInfo("This " + itemName + " is imbued with "
							+ item.getDurability() + " XP!", player);
					
				} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					
					event.setCancelled(true); // keep the item !
					
					// the item is empty and the player clicked "on is feet"
					sendInfo("This " + itemName + " is empty.", player);
				}
				
				
			} else if (event.getAction() == Action.LEFT_CLICK_AIR
						|| event.getAction() == Action.LEFT_CLICK_BLOCK) {
				
				if (item.getAmount() > 1) {
					sendInfo("To store experience in the " + itemName
							+ ", please unstack them!", player);
					return;
				}
				

				if (item.getDurability() > 0) {
					
					player.giveExp(item.getDurability());
					
					sendInfo("+Restoring " + item.getDurability() + " XP! You now have " 
								+ player.getTotalExperience() + " XP!", player);
					
					item.setDurability((short) 0);
					
				} else { // the item is empty
					
					player.getWorld().playEffect(player.getEyeLocation(), Effect.ENDER_SIGNAL, 0);
					player.playEffect(player.getEyeLocation(), Effect.EXTINGUISH, 0);
					
					if (player.getTotalExperience() > maxLevel) {
						
						// remove only the maximum xp
						playerXp = player.getTotalExperience() - maxLevel;
						item.setDurability((short) maxLevel);
						player.setTotalExperience(0);
						player.setLevel(0);
						player.giveExp(playerXp);
						
						sendInfo("-Imbued this " + itemName + " with "
								+ item.getDurability() + " XP! You have " + player.getTotalExperience() + "XP left!", player);
					} else {
						
						//Get Player XP and Set it to the Item Durability
						item.setDurability((short) player.getTotalExperience()); 

						player.setTotalExperience(0);
						player.setLevel(0);
						
						sendInfo("Imbued this " + itemName + " with "
								+ item.getDurability() + " XP!", player);
					}
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
	
} //class
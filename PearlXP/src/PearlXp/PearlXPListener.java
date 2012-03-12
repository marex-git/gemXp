package PearlXp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PearlXPListener implements org.bukkit.event.Listener {
	
	public PearlXP instance;
	public PearlXPListener(PearlXP owner) {instance = owner;}
	
    @EventHandler
	public void onPlayerInteract(org.bukkit.event.player.PlayerInteractEvent event){
		ItemStack item = event.getItem();
		if (item != null && item.getType() == Material.ENDER_PEARL) {
			Player ply = event.getPlayer();
			if (item.containsEnchantment(org.bukkit.enchantments.Enchantment.WATER_WORKER) && (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK || event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR)) {
				int level = item.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.WATER_WORKER);
				XPEditor xpeditor = new XPEditor(ply);
				ply.sendMessage("This pearl is imbued with "+xpeditor.xpLookup[level]+"xp (level 0->"+level+")!");
				event.setCancelled(true); return;
			}
			else if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {ply.sendMessage("Throwing a pearl at your feet would hurt!"); event.setCancelled(true); return;}
			else if (event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_AIR || event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK) {
				if (item.getAmount() > 1) {
					ply.sendMessage("To store experience in the pearl, please unstack them!");
					return;
				}
				XPEditor xpeditor = new XPEditor(ply);
				xpeditor.recalcTotalExp();
				int levels = xpeditor.getLevel();
				
				if (item.containsEnchantment(org.bukkit.enchantments.Enchantment.WATER_WORKER)) {
					int level = item.getEnchantmentLevel(org.bukkit.enchantments.Enchantment.WATER_WORKER);
					item.removeEnchantment(org.bukkit.enchantments.Enchantment.WATER_WORKER);
					xpeditor.giveExp(xpeditor.xpLookup[level]);
					ply.sendMessage("Restoring "+xpeditor.xpLookup[level]+"xp (level 0->"+level+")!");
				}
				else if(levels > 0) {
					if (levels > instance.getConfig().getInt("maxlevel")) {levels = instance.getConfig().getInt("maxlevel");}
					xpeditor.takeExp(xpeditor.xpLookup[levels]);
					item.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.WATER_WORKER,levels);
					ply.sendMessage("Imbued this pearl with "+xpeditor.xpLookup[levels]+"xp (level 0->"+levels+")!");
				}
			}
		}
	}
}
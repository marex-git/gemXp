package PearlXP;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class PearlXPListener implements org.bukkit.event.Listener {

	public PearlXP instance;

	public PearlXPListener(PearlXP owner) {
		instance = owner;
	}

	@EventHandler
	public void onPlayerInteract(
			org.bukkit.event.player.PlayerInteractEvent event) {
		ItemStack item = event.getItem();
		if (item != null
				&& item.getTypeId() == instance.getConfig().getInt("itemid")) {
			
			Player ply = event.getPlayer();
			String itemname = "pearl";
			
			if (item.getTypeId() != 368) {
				itemname = "item";
			}

			if (item.getDurability() >= 1
					&& (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK || event
							.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR)) {
				
				int level = item
						.getDurability();

				ply.sendMessage("This " + itemname + " is imbued with "
						+ XPEditor.xpLookup[level] + "xp (level 0->" + level
						+ ")!");
				event.setCancelled(true);
				
			} else if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK
					&& item.getTypeId() == 368) {
				ply.sendMessage("Throwing a pearl at your feet would hurt!");
				event.setCancelled(true);
				
			} else if (event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_AIR
					|| event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK) {
				
				if (item.getAmount() > 1) {
					ply.sendMessage("To store experience in the " + itemname
							+ ", please unstack them!");
					return;
				}
				
				XPEditor xpeditor = new XPEditor(ply);
				xpeditor.recalcTotalExp();
				int levels = xpeditor.getLevel();

				if (item.getDurability() >= 1) {
					int level = item
							.getDurability();
					item.setDurability((short) 0);
					xpeditor.giveExp(XPEditor.xpLookup[level]);
					ply.sendMessage("Restoring " + XPEditor.xpLookup[level]
							+ "xp (level 0->" + level + ")!");
					
				} else if (levels == 0) {
					
					if (levels > instance.getConfig().getInt("maxlevel")) {
						levels = instance.getConfig().getInt("maxlevel");
					}
					
					xpeditor.takeExp(XPEditor.xpLookup[levels]);
					item.setDurability((short) levels);
					ply.sendMessage("Imbued this " + itemname + " with "
							+ XPEditor.xpLookup[levels] + "xp (level 0->"
							+ levels + ")!");
				}
			}
		}
	}
}
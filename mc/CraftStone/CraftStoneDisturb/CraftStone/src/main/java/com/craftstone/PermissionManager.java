package com.craftstone;

import java.util.HashMap;
import java.util.List;

import com.craftstone.stone.entity.Player;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PermissionManager {
	private HashMap<String, List<String>> permissionMap;
	
	public PermissionManager() {
		permissionMap = Maps.newHashMap();
	}
	
	public boolean hasPermission(Player player, String permission) {
		//if (this.hasPermission(player, "*")) { return true; } 
		
		if (permissionMap.get(player.getName()) != null) {
			List<String> permissions = permissionMap.get(player.getName());
			if (permissions.indexOf(permission) > -1) {
				return true;
			} else return false;
		} else return false;
	}
	
	public void givePermission(Player player, String permission) {
		if (permissionMap.get(player.getName()) == null) {
			List<String> tempLs = Lists.newArrayList();
			permissionMap.put(player.getName(), tempLs);
		}
		
		if (permissionMap.get(player.getName()).indexOf(permission) > -1) {
			return;
		}
		
		permissionMap.get(player.getName()).add(permission);
	}
	
	public void removePermission(Player player, String permission) {
		if (permissionMap.get(player.getName()) != null) {
			return;
		}
		
		if (permissionMap.get(player).indexOf(permission) == -1) {
			return;
		}
		
		permissionMap.get(player).remove(permission);
	}
}

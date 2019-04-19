package com.craftstone.plugin;

public class RawCommand {
	private String name;
	private String[] aliases;
	private String desc;
	private String usage;
	private String permission;
	private String permissionMessage;
	
	public RawCommand(String name, String[] aliases, String desc, String usage,
			String permission, String permissionMessage) {
		this.name = name;
		this.aliases = aliases;
		this.desc = desc;
		this.usage = usage;
		this.permission = permission;
		this.permissionMessage = permissionMessage;
	}
	
	public String getName() {
		return name;
	}

	public String[] getAliases() {
		return aliases;
	}

	public String getDesc() {
		return desc;
	}

	public String getUsage() {
		return usage;
	}

	public String getPermission() {
		return permission;
	}

	public String getPermissionMessage() {
		return permissionMessage;
	}
}

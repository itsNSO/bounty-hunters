package net.Indyuce.bountyhunters.version.wrapper;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.Indyuce.bountyhunters.api.CustomItem;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_13_R2.PacketPlayOutTitle.EnumTitleAction;

public class VersionWrapper_1_13_R2 implements VersionWrapper {
	@Override
	public ItemStack addTag(ItemStack item, ItemTag... tags) {
		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();

		for (ItemTag tag : tags)
			if (tag.getValue() instanceof String)
				compound.setString(tag.getPath(), (String) tag.getValue());
			else if (tag.getValue() instanceof Boolean)
				compound.setBoolean(tag.getPath(), (boolean) tag.getValue());
			else if (tag.getValue() instanceof Double)
				compound.setDouble(tag.getPath(), (double) tag.getValue());
			else if (tag.getValue() instanceof Integer)
				compound.setInt(tag.getPath(), (Integer) tag.getValue());

		nmsi.setTag(compound);
		return CraftItemStack.asBukkitCopy(nmsi);
	}

	@Override
	public String getStringTag(ItemStack item, String path) {
		if (item == null || item.getType() == Material.AIR)
			return "";

		net.minecraft.server.v1_13_R2.ItemStack nmsi = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = nmsi.hasTag() ? nmsi.getTag() : new NBTTagCompound();
		return compound.getString(path);
	}

	@Override
	public void sendJson(Player player, String message) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(message)));
	}

	@Override
	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int ticks, int fadeOut) {
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\": \"" + title + "\"}")));
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\": \"" + subtitle + "\"}")));
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutTitle(EnumTitleAction.TIMES, null, fadeIn, ticks, fadeOut));
	}

	@Override
	public ItemStack getHead(OfflinePlayer player) {

		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta) CustomItem.PLAYER_HEAD.toItemStack().getItemMeta();
		meta.setDisplayName(meta.getDisplayName().replace("{name}", player.getName()));
		meta.setOwningPlayer(player);
		item.setItemMeta(meta);

		return item;
	}

	@Override
	public void spawnParticle(Particle particle, Location loc, Player player, Color color) {
		player.spawnParticle(particle, loc, 0, new Particle.DustOptions(color, 1));
	}

	@Override
	public void setOwner(SkullMeta meta, OfflinePlayer player) {
		meta.setOwningPlayer(player);
	}
}
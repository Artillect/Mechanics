package com.pfaeff_and_cdkrot.net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.pfaeff_and_cdkrot.api.benchmark.BenchmarkRegistry;
import com.pfaeff_and_cdkrot.gui.GuiBenchmark;
import com.pfaeff_and_cdkrot.tileentity.TileEntityBenchmark;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

//Sided network code
public class SidedNetworkStuff
{

	@SideOnly(Side.CLIENT)
	public static void openBenchmarkGUI(GamePosition position, String text)
	{
		Minecraft.getMinecraft().displayGuiScreen(new GuiBenchmark(position, text));
	}

	@SideOnly(Side.CLIENT)
	public static void setBenchmarkText(GamePosition pos, String text, EntityPlayer player)
	{
		TileEntityBenchmark tile = (TileEntityBenchmark) MinecraftServer.getServer().worldServers[pos.worldid].getTileEntity(pos.x, pos.y, pos.z);
		if (BenchmarkRegistry.instance.onTextChanged(tile, text, player))
			tile.s=text;
	}
}

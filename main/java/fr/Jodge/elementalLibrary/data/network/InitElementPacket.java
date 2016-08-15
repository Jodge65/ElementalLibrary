package fr.Jodge.elementalLibrary.data.network;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;

import fr.Jodge.elementalLibrary.Main;
import fr.Jodge.elementalLibrary.data.DataHelper;
import fr.Jodge.elementalLibrary.data.PlayerHelper;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.entity.PlayerStats;
import fr.Jodge.elementalLibrary.data.interfaces.IElementalWritable;
import fr.Jodge.elementalLibrary.data.matrix.AttackMatrix;
import fr.Jodge.elementalLibrary.data.matrix.DefenceMatrix;
import fr.Jodge.elementalLibrary.data.matrix.ElementalMatrix;
import fr.Jodge.elementalLibrary.function.JLog;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class InitElementPacket implements IMessage
{
	public InitElementPacket(){}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			Element element = new Element();
			element.fromByte(buf);
			JLog.info("New Element : " + element);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(Element.numberOfElement());
		for(Element element : Element.getAllElement())
		{
			element.toByte(buf);
		}
	}
	
	public static class Handler implements IMessageHandler<InitElementPacket, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(InitElementPacket message, MessageContext ctx) 
		{
			JLog.info("Elements supposed to be received.");
			return null;
		}
		
	}

}

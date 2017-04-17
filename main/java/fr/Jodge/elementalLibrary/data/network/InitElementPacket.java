package fr.Jodge.elementalLibrary.data.network;

import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.log.JLog;
import io.netty.buffer.ByteBuf;
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

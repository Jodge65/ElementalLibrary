package fr.Jodge.elementalLibrary.data.network;

import java.util.Map.Entry;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import fr.Jodge.elementalLibrary.data.element.Element;
import fr.Jodge.elementalLibrary.data.register.Register;
import fr.Jodge.elementalLibrary.data.register.Variable;
import fr.Jodge.elementalLibrary.log.JLog;


public class InitDamageSourcePacket implements IMessage
{
	public InitDamageSourcePacket(){}
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		int size = buf.readInt();
		for(int i = 0; i < size; i++)
		{
			String name = BufUtils.readUTF8String(buf);
			Element element = Element.findById(buf.readInt());
			Register.addNewElementOnDamageSources(name, element);
			boolean needToApply = buf.readBoolean();
			Register.setDamageSourceUseEffect(name, needToApply);
		}

	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(Variable.DEFAULT_ELEMENT_DAMAGE_SOURCES.size());
		for(Entry<String, Element> entry : Variable.DEFAULT_ELEMENT_DAMAGE_SOURCES.entrySet())
		{
			BufUtils.writeUTF8String(buf, entry.getKey());
			buf.writeInt(entry.getValue().getId());
			boolean needToApply = Variable.DEFAULT_EFFECT_DAMAGE_SOURCES.getOrDefault(entry.getKey(), true);
			buf.writeBoolean(needToApply);
		}

	}
	
	public static class Handler implements IMessageHandler<InitDamageSourcePacket, IMessage>
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(InitDamageSourcePacket message, MessageContext ctx) 
		{
			JLog.info("Damage sources is supposed to be register.");
			return null;
		}
		
	}

}
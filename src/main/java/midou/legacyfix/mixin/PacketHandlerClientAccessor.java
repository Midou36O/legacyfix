package midou.legacyfix.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.net.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.net.handler.PacketHandlerClient;

@Mixin(value = PacketHandlerClient.class, remap = false)
public interface PacketHandlerClientAccessor {

	@Accessor("mc")
	Minecraft getMinecraft();

	@Accessor("netManager")
	NetworkManager getNetworkManager();
}

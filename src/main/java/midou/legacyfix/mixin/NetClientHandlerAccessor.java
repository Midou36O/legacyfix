package midou.legacyfix.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.net.NetworkManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import net.minecraft.client.net.handler.NetClientHandler;

@Mixin(value = NetClientHandler.class, remap = false)
public interface NetClientHandlerAccessor {

	@Accessor("mc")
	Minecraft getMinecraft();

	@Accessor("netManager")
	NetworkManager getNetworkManager();
}

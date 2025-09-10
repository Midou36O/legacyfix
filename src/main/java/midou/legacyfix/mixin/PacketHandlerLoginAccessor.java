package midou.legacyfix.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.net.handler.PacketHandlerLogin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = PacketHandlerLogin.class, remap = false)
public interface PacketHandlerLoginAccessor {
	@Accessor("mcServer")
	MinecraftServer getMcServer();
}

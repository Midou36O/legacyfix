package midou.legacyfix.mixin;

import net.minecraft.server.net.thread.ThreadLoginVerifier;
import net.minecraft.core.net.packet.Packet1Login;
import net.minecraft.server.net.handler.NetLoginHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ThreadLoginVerifier.class, remap = false)
public interface ThreadLoginVerifierAccessor {

	@Accessor("loginPacket")
	Packet1Login getLoginPacket();

	@Accessor("loginHandler")
	NetLoginHandler getLoginHandler();
}

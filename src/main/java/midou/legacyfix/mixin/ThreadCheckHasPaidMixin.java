package midou.legacyfix.mixin;

import net.minecraft.client.net.thread.ThreadCheckHasPaid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ThreadCheckHasPaid.class)
public class ThreadCheckHasPaidMixin {

	@ModifyConstant(method = "run", constant = @Constant(stringValue = "http://session.minecraft.net/game/joinserver.jsp?user="), remap = false)
	private String replaceSessionUrl(String original) {
		// Replace with your custom domain
		return "https://mcauth.linuxcrack.zip/game/joinserver.jsp?user=";
	}
}

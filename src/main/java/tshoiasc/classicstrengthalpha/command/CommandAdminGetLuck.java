package tshoiasc.classicstrengthalpha.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import tshoiasc.classicstrengthalpha.utils.TextColor;

import java.util.Optional;

public class CommandAdminGetLuck implements CommandExecutor {
    //private final Text usage = Text.of("/qh admin get <class> [percent] /n<class>:luck、protect [percent]:can be use if class equals luck");

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("控制台禁用此指令！"));
            return CommandResult.success();
        }
        Player p = (Player) src;
        Optional<Integer> percent = args.getOne("percent");
        if (!percent.isPresent()) {
            p.sendMessage(Text.of("§6/qh admin get luck <percent> |percent:成功幸运率"));
            return CommandResult.success();
        }
        int per = percent.get();
        ItemStack luck = ItemStack.builder().itemType(ItemTypes.END_CRYSTAL).add(Keys.DISPLAY_NAME, Text.of(TextColor.getColorCode(per)+per + "％强化幸运石")).quantity(64).build();
        p.getInventory().offer(luck);
        p.sendMessage(Text.of("§a64个" + TextColor.getColorCode(per) +per+ "％强化幸运石" + "§a已发送到你的背包."));
        return CommandResult.success();
    }
}

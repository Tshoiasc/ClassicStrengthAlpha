package tshoiasc.classicstrengthalpha.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import tshoiasc.classicstrengthalpha.ClassicStrengthAlpha;
import tshoiasc.classicstrengthalpha.Inventory.StrengthInventory;

public class CommandNormal implements CommandExecutor {
    static ClassicStrengthAlpha cs;

    public CommandNormal(ClassicStrengthAlpha cs1) {
        cs = cs1;

    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("控制台使用此命令无效"));
        }
        assert src instanceof Player;
        new StrengthInventory(cs).open((Player) src);
        return CommandResult.success();
    }
}

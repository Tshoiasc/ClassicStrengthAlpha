package tshoiasc.classicstrengthalpha.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class CommandQH implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("控制台禁用此指令"));
        }
        if (src.hasPermission("qh.use")){
            src.sendMessage(Text.of("§4你没有权限这样做"));
        }
        src.sendMessage(Text.of("§6§l--------------[ClassicStrengthAlpha]--------------"));
        if (src.hasPermission("qh.sharpen"))src.sendMessage(Text.of("§6§l/qh sharpen 为手中武器开刃"));
        src.sendMessage(Text.of("§6§l/qh normal  打开强化炉"));
        if (src.hasPermission("qh.admin"))src.sendMessage(Text.of("§6§l/qh admin   管理员指令"));
        src.sendMessage(Text.of("§6§l-------------------------------------------------"));

        return CommandResult.success();
    }
}



package tshoiasc.classicstrengthalpha.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import tshoiasc.classicstrengthalpha.Inventory.StrengthInventory;

import java.util.Optional;

public class CommandAdminSet implements CommandExecutor {
    private final Text usage = Text.of("/qh set <level>");

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("控制台禁用此指令！"));
            return CommandResult.success();
        }

        Player p = (Player) src;
        Optional<Integer> level = args.getOne("level");
        if (!level.isPresent()) {
            p.sendMessage(usage);
            return CommandResult.success();
        }
        int w_level = level.get() > 15 ? 15 : level.get();
        Optional<ItemStack> hand = p.getItemInHand(HandTypes.MAIN_HAND);
        if (!hand.isPresent()) {
            p.sendMessage(Text.of("§4请把要开刃的物品放在手上！"));
            return CommandResult.success();
        }
        ItemStack ItemInHand = hand.get();
        p.setItemInHand(HandTypes.MAIN_HAND, StrengthInventory.setLevel(ItemInHand, w_level));


        return CommandResult.success();
    }
}



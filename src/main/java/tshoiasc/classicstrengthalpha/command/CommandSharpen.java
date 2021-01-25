package tshoiasc.classicstrengthalpha.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import tshoiasc.classicstrengthalpha.Inventory.StrengthInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandSharpen implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            src.sendMessage(Text.of("控制台使用此命令无效"));
            return CommandResult.success();
        }
        Player p = (Player) src;
        Optional<ItemStack> hand = p.getItemInHand(HandTypes.MAIN_HAND);
        if (!hand.isPresent()) {
            p.sendMessage(Text.of("请将要开刃的物品放在手中"));
            return CommandResult.success();
        }
        Optional<String> name = args.getOne("name");
        if (!name.isPresent()){
            p.sendMessage(Text.of("/qh sharpen <名字>"));
            return CommandResult.success();
        }
        String na = name.get();
        ItemStack handItem = hand.get();

        if (StrengthInventory.canStrength(handItem)){
            p.sendMessage(Text.of("§4当前物品已开过刃了！"));
            return CommandResult.success();
        }
        Optional<List<Text>> lores = handItem.get(Keys.ITEM_LORE);
        List<Text> lore = lores.orElseGet(ArrayList::new);

        lore.add(Text.of("§6可强化"));
        p.setItemInHand(HandTypes.MAIN_HAND, ItemStack.builder().fromItemStack(handItem).add(Keys.DISPLAY_NAME,Text.of(na)).keyValue(Keys.ITEM_LORE, lore).build());

        return CommandResult.success();
    }
}

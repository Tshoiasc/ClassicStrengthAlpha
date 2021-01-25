package tshoiasc.classicstrengthalpha.Inventory.listener;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandType;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import tshoiasc.classicstrengthalpha.Inventory.StrengthInventory;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttackEventListener implements EventListener<DamageEntityEvent> {

    @Override
    public void handle(DamageEntityEvent event) throws Exception {


        Optional<Player> firstPlayer = event.getCause().first(Player.class);
        if (!firstPlayer.isPresent()) return;
        Player p = firstPlayer.get();

        Optional<ItemStack> im = p.getItemInHand(HandTypes.MAIN_HAND);
        if (!im.isPresent()) return;
        ItemStack sword = im.get();
        int level = StrengthInventory.getLevel(sword);
        event.setBaseDamage(event.getOriginalFinalDamage() + level * 2);


    }


}
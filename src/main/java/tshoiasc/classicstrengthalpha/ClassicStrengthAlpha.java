package tshoiasc.classicstrengthalpha;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.EventListener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import tshoiasc.classicstrengthalpha.Inventory.listener.AttackEventListener;
import tshoiasc.classicstrengthalpha.command.*;

@Plugin(
        id = "classicstrength-alpha",
        name = "ClassicStrength Alpha",
        description = "classicStrength",
        url = "http://www.zzpz.net/",
        authors = {
                "Tshoiasc"
        }
)
public class ClassicStrengthAlpha {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStarting(GameStartedServerEvent event) {
        CommandSpec qh_get_luck = CommandSpec.builder().description(Text.of("ClassicStrength"))
                .arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("percent"))))
                .executor(new CommandAdminGetLuck())
                .build();
        CommandSpec qh_get_protect = CommandSpec.builder().description(Text.of("ClassicStrength"))
                .executor(new CommandAdminGetProtect())
                .build();
        CommandSpec qh_get = CommandSpec.builder()
                .description(Text.of("ClassicStrength"))
                .child(qh_get_luck, "luck", "l")
                .child(qh_get_protect, "protect", "p")
                .build();
        CommandSpec qh_set = CommandSpec.builder()
                .description(Text.of("ClassicStrength"))
                .permission("qh.admin")
                .arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("level"))))
                .executor(new CommandAdminSet())
                .build();
        CommandSpec qh_admin = CommandSpec.builder()
                .description(Text.of("ClassicStrength"))
                .permission("qh.admin")
                .child(qh_set, "set")
                .child(qh_get, "get")
                .build();
        CommandSpec qh_normal = CommandSpec.builder()
                .description(Text.of("ClassicStrength"))
                .permission("qh.normal")
                .executor(new CommandNormal(this))
                .build();
        CommandSpec qh_sharpen = CommandSpec.builder()
                .description(Text.of("ClassicStrength"))
                .permission("qh.sharpen")
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("name"))))
                .executor(new CommandSharpen())
                .build();
        CommandSpec qh = CommandSpec.builder()
                .description(Text.of("ClassicStrength"))
                .permission("qh.use")
                .child(qh_sharpen, "sharpen")
                .child(qh_admin, "admin")
                .child(qh_normal, "normal")
                .executor(new CommandQH())
                .build();
        EventListener<DamageEntityEvent> listener = new AttackEventListener();
        Sponge.getEventManager().registerListener(this, DamageEntityEvent.class, listener);
        Sponge.getCommandManager().register(this, qh, "qh", "QH");
    }

}

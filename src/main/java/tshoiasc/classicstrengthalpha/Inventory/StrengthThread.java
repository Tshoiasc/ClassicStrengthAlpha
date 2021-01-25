package tshoiasc.classicstrengthalpha.Inventory;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperation;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StrengthThread implements Runnable {
    Player p;
    Inventory inv;

    public StrengthThread(Player p, Inventory inv) {
        this.p = p;
        this.inv = inv;
    }

    @Override
    public void run() {

        StrengthInventory.canClick.put(p.getUniqueId(), false);
        boolean isProtect = false;
        double luck = 0;
        boolean isBreakable = false;
        Optional<ItemStack> s_sword = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(19))).peek();
        if (!s_sword.isPresent()) return;
        ItemStack sword = s_sword.get();
        int level = StrengthInventory.getLevel(sword);
        if (level == 15) {
            p.sendMessage(Text.of("§5§l武器已满级"));
            StrengthInventory.canClick.put(p.getUniqueId(), true);
            return;
        }
        //?????
        boolean lucks = false;
        boolean isForProtect = false;
        Optional<ItemStack> protect = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(8))).peek();
        if (protect.isPresent()) {

            ItemStack pro = protect.get();
            if (!pro.getType().equals(ItemTypes.AIR)) {
                Optional<Text> D_name = pro.get(Keys.DISPLAY_NAME);
                if (!pro.getType().equals(ItemTypes.END_STONE) || !D_name.isPresent() || D_name.get().isEmpty() || D_name.get().getColor().getName().equals("NONE") || !D_name.get().toPlain().equals("强化保护石")) {
                } else {
                    isForProtect = true;
                }
            }
        }



        Optional<ItemStack> lucky = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(26))).peek(1);
        if (!lucky.isPresent()) luck = 0;
        else {
            ItemStack luck_stone = lucky.get();
            Optional<Text> r_name = luck_stone.get(Keys.DISPLAY_NAME);if (r_name.isPresent()) {
                Text name = r_name.get();luck = (double) Integer.parseInt(name.toPlain().replaceAll("％强化幸运石", "").trim()) / 100;
                    if (luck >= 1) lucks = true;
                }
            }

        //?????
        if (isForProtect) {
            if (lucks) {
                p.sendMessage(Text.of("§4§l武器强化成功率已达100％，请卸掉保护石"));
                StrengthInventory.canClick.put(p.getUniqueId(), true);
                return;
            }
            if (level < 4) {
                p.sendMessage(Text.of("§4§l当前等级小于4，强化失败不会降级，请卸掉保护石"));
                StrengthInventory.canClick.put(p.getUniqueId(), true);
                return;
            }
            if (level >= 12) {
                p.sendMessage(Text.of("§4§l当前等级大于12，保护石无法为被强化物品提供保护，请卸掉保护石"));
                StrengthInventory.canClick.put(p.getUniqueId(), true);
                return;
            }
            inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(8))).poll(1);
            isProtect = true;
        }
        ItemStack b_sword = StrengthInventory.setLevel(sword.copy(), level - 1);
        ItemStack c_sword = StrengthInventory.setLevel(sword.copy(), 7);
        int consume_sponge = 0;
        if (level < 4) consume_sponge = 3;
        else if (level < 8) consume_sponge = 5;
        else if (level < 12) consume_sponge = 8;
        else if (level < 15) consume_sponge = 10;

        inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(13)), QueryOperationTypes
                .INVENTORY_PROPERTY.of(SlotIndex.of(14)),
                QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(15)),
                QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(22)),
                QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(23)),
                QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(24)))
                .poll(consume_sponge);
        inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(26)))
                .poll(1);
        Integer[] a = {0, 1, 2, 9, 10, 11, 18, 20, 27, 29, 36, 37, 38};

        for (int i : a) {
            inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))})
                    .set(ItemStack.builder()
                            .itemType(ItemTypes.STAINED_GLASS_PANE)
                            .add(Keys.ITEM_DURABILITY, -14)
                            .add(Keys.DISPLAY_NAME,
                                    Text.of(TextColors.RED, "强化中"))
                            .build());
        }


        if (level < 4) {
            isProtect = true;
            luck += 0.6;
        } else if (level < 8) {
            luck += 0.4;
        } else if (level < 12) {
            luck += 0.2;
        } else {
            luck += 0.1;
            isProtect = false;
            isBreakable = true;
        }
        double rand = Math.random();
        boolean result = rand <= luck;
        if (isProtect) {
            p.sendMessage(Text.of("§b§l保护石已生效，本次强化收到保护"));
        } else {
            if (isBreakable) {
                p.sendMessage(Text.of("§b§l本次强化不收保护，且失败有概率损坏"));
            } else {
                p.sendMessage(Text.of("§b§l本次强化不收到保护"));
            }

        }
        p.sendMessage(Text.of("§e§l本次强化成功率为：§4§l" + Math.floor((luck * 100) > 100 ? 100 : (luck * 100)) + "%"));


        ItemStack before = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.ITEM_DURABILITY, -15).add(Keys.DISPLAY_NAME, Text.of(TextColors.BLUE, "冷却层")).build();
        ItemStack after = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.ITEM_DURABILITY, 0).add(Keys.DISPLAY_NAME, Text.of(TextColors.RED, "灼热的冷却层")).build();
        for (int i = 45; i <= 53; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))).set(after);
        }

        if (result) {
            inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(19))).set(StrengthInventory.setLevel(sword, level + 1));
            p.sendMessage(Text.of("§6§l强化成功。"));
            if (level >= 7) {
                Sponge.getServer().getBroadcastChannel().send(Text.of("§6§l[公告] : §5§l玩家\"" + p.getName() + "\"§5§l的§4§l\"§6§l" + sword.get(Keys.DISPLAY_NAME).get().toPlain().replaceAll("[+][\\d]{1,2}", "").trim() + "§4§l\"§5§l成功强化到" + (level + 1) + "级"));
            }
        } else {

            if (level < 4) {
                p.sendMessage(Text.of("§4§l强化失败,由于等级小于4，本次不降级"));
            } else if (level <= 7) {
                if (isProtect) {
                    //inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(19))).set(sword);
                    p.sendMessage(Text.of("§4§l强化失败,受到保护石保护，本次不降级"));
                } else {
                    inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(19))).set(b_sword);
                    p.sendMessage(Text.of("§4§l强化失败,等级下降一级"));
                }
            } else if (level <= 12) {
                double rand2 = Math.random();
                if (isProtect) {
                    //inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(19))).set(sword);
                    p.sendMessage(Text.of("§4§l强化失败,受到保护石保护，本次不降级"));
                } else {
                    if (rand2 < 0.2) {
                        inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(19))).set(c_sword);
                        p.sendMessage(Text.of("§4§l糟糕！炉温过高，没有保护石加护，装备变成7级。"));
                    } else{inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(19))).set(b_sword);
                    p.sendMessage(Text.of("§4§l强化失败,本次下降一级"));}
                }
            } else {
                double rand2 = Math.random();
                if (rand2 < 0.1) {
                    p.sendMessage(Text.of("§4§l糟糕！天神降怒,装备损坏"));
                    inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(19))).poll();
                } else if (rand2 > 0.7) {
                    inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(19))).set(c_sword);
                    p.sendMessage(Text.of("§4§l糟糕！炉温过高，没有保护石加护，装备变成7级。"));
                } else {
                    inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(19))).set(b_sword);
                    p.sendMessage(Text.of("§4§l强化失败,本次下降一级"));
                }
            }

        }
        for (int i = 45; i <= 53; i++) {
            inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))).set(before);
        }
        for (int i : a) {
            inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))}).set(ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.ITEM_DURABILITY, -11).add(Keys.DISPLAY_NAME, Text.of(TextColors.GRAY, "等待强化")).build());
        }

        StrengthInventory.canClick.put(p.getUniqueId(), true);


    }
}

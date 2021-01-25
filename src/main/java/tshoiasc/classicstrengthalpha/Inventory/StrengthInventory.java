package tshoiasc.classicstrengthalpha.Inventory;


import com.google.common.collect.ImmutableList;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperation;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import tshoiasc.classicstrengthalpha.ClassicStrengthAlpha;

import java.util.*;

import static org.spongepowered.api.item.inventory.InventoryArchetypes.CHEST;

public class StrengthInventory {
    static Map<UUID, Boolean> canClick = new HashMap<>();
    static Map<UUID, Inventory> inv = new HashMap<>();
    ClassicStrengthAlpha plugin;

    public StrengthInventory(ClassicStrengthAlpha plugin) {
        this.plugin = plugin;


    }

    public void open(Player p) {

        if (!inv.containsKey(p.getUniqueId())) {
            inv.put(p.getUniqueId(), create(p));
        }
        p.openInventory(inv.get(p.getUniqueId()));


    }

    public Inventory create(Player p) {
        Inventory chestInventory =
                Inventory

                        .builder()
                        .of(CHEST)
                        .withCarrier(p)
                        .property("inventorydimension", new InventoryDimension(9, 6))
                        .property("inventorytitle", new InventoryTitle(Text.of("§b§l强化炉")))
                        .listener(ClickInventoryEvent.class, this::fireClickEvent)
                        .build(plugin);
        setItem(chestInventory);

        UUID uid = p.getUniqueId();
        inv.put(uid, chestInventory);
        return chestInventory;

    }

    public static ItemStack setLevel(ItemStack origin, int level) {
        Optional<Text> d_name = origin.get(Keys.DISPLAY_NAME);
        if (!d_name.isPresent()) return origin;
        Text name = d_name.get();
        Text newName = null;
        if (name.toPlain().matches(".*[+][\\d]{1,2}")) {
            boolean flag = false;
            ImmutableList<Text> children = name.getChildren();
            for (Text child : children) {
                // Sponge.getServer().getConsole().sendMessage(child);
                if (child.getColor() == TextColors.GOLD && child.toPlain().matches(".*[+][\\d]{1,2}")) {
                    newName = name.toBuilder().remove(child).append(Text.builder().color(TextColors.GOLD).append(Text.of("§l+" + level)).build()).build();
                    flag = true;
                }
            }
            if (!flag) return origin;
        } else
            newName = Text.builder().append(name).append(Text.of(" ")).append(Text.builder().color(TextColors.GOLD).append(Text.of("§l+" + level)).build()).build();
        origin.offer(Keys.DISPLAY_NAME, newName);
        return origin;
    }

    private void fireClickEvent(ClickInventoryEvent e) {
        //????8????? 13 14 15 22 23 24 ????? 19??????? 26?????
        Object[] pl = e.getTargetInventory().getViewers().toArray();
        Player p = (Player) pl[0];
        if (canClick.containsKey(p.getUniqueId()) && !canClick.get(p.getUniqueId())) {
            e.setCancelled(true);
            return;
        }
        Integer[] var = {8, 13, 14, 15, 22, 23, 24, 19, 26};
        int val = 0;
        List<Integer> varl = Arrays.asList(var);
        for (SlotTransaction slotTransaction : e.getTransactions()) {
            Slot slot = slotTransaction.getSlot();
            SlotIndex pos = getSlotOrdinal(slot);
            val = pos.getValue();
            if (!varl.contains(val) && val < 54) e.setCancelled(true);
            else if (val == 19) { //???
                Optional<ItemStack> im = slot.peek();
                if (!im.isPresent()) {
                    continue;
                }
                ItemStack sword = im.get();
                if (!canStrength(sword)) {
                    p.sendMessage(Text.of("§4当前装备不可以强化"));
                    e.setCancelled(true);
                    return;
                }
            } else if (val == 8) {//???
                Optional<ItemStack> im = slot.peek();
                if (!im.isPresent()) {
                    continue;
                }
                ItemStack protect = im.get();
                Optional<Text> na = protect.get(Keys.DISPLAY_NAME);
                if (protect.getType() != ItemTypes.END_STONE||!na.isPresent()||na.get().getColor().getName().equals("NONE")||!na.get().toPlain().equals("强化保护石") ){
                    p.sendMessage(Text.of("保护石放置错误"));
                    e.setCancelled(true);
                    return;
                }
            } else if (val == 26) {
                Optional<ItemStack> im = slot.peek();
                if (!im.isPresent()) {
                    continue;
                }

                ItemStack luck = im.get();
                if (luck.getType() == ItemTypes.END_CRYSTAL) {
                    Optional<Text> name = luck.get(Keys.DISPLAY_NAME);

                    if (name.isPresent()) {
                        if (name.get().toPlain().matches("[\\d]{1,3}％强化幸运石")) {
                            if (name.get().getColor().getName().equals("NONE")) {
                                p.sendMessage(Text.of("§4幸运石作弊！系统已记录。"));
                                Sponge.getServer().getConsole().sendMessage(Text.of(p.getName() + "幸运石作弊"));
                                e.setCancelled(true);
                                return;
                            }
                        } else {
                            p.sendMessage(Text.of("幸运石放置错误"));
                            e.setCancelled(true);
                            return;
                        }
                    } else {
                        p.sendMessage(Text.of("幸运石放置错误"));
                        e.setCancelled(true);
                        return;
                    }

                } else {
                    p.sendMessage(Text.of("幸运石放置错误"));
                    e.setCancelled(true);
                    return;
                }
            } else if (val == 13 || val == 14 || val == 15 || val == 22 || val == 23 || val == 24) {
                Optional<ItemStack> im = slot.peek();
                if (!im.isPresent()) {
                    continue;
                }
                ItemStack qh = im.get();
                if (qh.getType() != ItemTypes.SPONGE) {
                    p.sendMessage(Text.of("强化石放置错误"));
                    e.setCancelled(true);
                    return;
                }
            }
        }
        Inventory in = e.getTargetInventory();
        if (val == 44) {
            if (checkInventory(in)) {
                StrengthThread d = new StrengthThread(p, in);
                new Thread(d).start();
            } else {
                p.sendMessage(Text.of("强化石数量错误"));
            }


        }

    }

    public static boolean canStrength(ItemStack im) {
        Optional<List<Text>> lo = im.get(Keys.ITEM_LORE);
        if (!lo.isPresent()) return false;
        List<Text> lore = lo.get();
        for (Text line : lore) {
            if (line.getColor() == TextColors.GOLD && line.toPlain().equals("可强化")) return true;
        }
        return false;
    }

    public static int getLevel(ItemStack sword) {
        Optional<Text> r_dame = sword.get(Keys.DISPLAY_NAME);
        if (!r_dame.isPresent()) return 0;
        Text name = r_dame.get();
        ImmutableList<Text> child = name.getChildren();
        for (Text c : child) {
            String sp = c.toPlain();
            if (sp.matches("[+]\\d{1,2}")) {
                String spl = sp.substring(1);
                int level = Integer.parseInt(spl);
                return Math.min(level, 15);
            }
        }
        return 0;
    }

    private boolean checkInventory(Inventory inv) {
        Integer[] g = {13, 14, 15, 22, 23, 24};
        Optional<ItemStack> sword = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(19))).peek();
        if (!sword.isPresent()) return false;
        //Sponge.getServer().getConsole().sendMessage(Text.of(1));
        ItemStack real_sword = sword.get();
        int level = getLevel(real_sword);
        boolean result1 = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(13))).contains(ItemTypes.SPONGE)
                || inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(14))).contains(ItemTypes.SPONGE)
                || inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(15))).contains(ItemTypes.SPONGE)
                || inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(22))).contains(ItemTypes.SPONGE)
                || inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(23))).contains(ItemTypes.SPONGE)
                || inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(24))).contains(ItemTypes.SPONGE);
        if (!result1) return false;
        //Sponge.getServer().getConsole().sendMessage(Text.of(2));
        int count = 0;
        for (int i = 0; i <= 5; i++) {
            Optional<ItemStack> im = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(g[i]))).peek();
            if (!im.isPresent()) continue;
            ItemStack sponge = im.get();
            count += sponge.getQuantity();
        }
        int consume_sponge = 0;
        if (level < 4) consume_sponge = 3;
        else if (level < 8) consume_sponge = 5;
        else if (level < 12) consume_sponge = 8;
        else if (level < 15) consume_sponge = 10;

        return count > consume_sponge;
        //Sponge.getServer().getConsole().sendMessage(Text.of(3));

    }

    public static SlotIndex getSlotOrdinal(Slot slot) {
        Collection<SlotIndex> properties = slot.parent().getProperties(slot, SlotIndex.class);
        if (properties.isEmpty()) {
            throw new UnsupportedOperationException("Not recognized");
        }
        return properties.iterator().next();
    }

    private void setItem(Inventory inv) {
        Integer[] a = {0, 1, 2, 9, 10, 11, 18, 20, 27, 29, 36, 37, 38};//§1冷却层
        Integer[] b = {12, 16, 21, 25, 30, 31, 32, 33, 34, 39, 43};//§7熔炉壁
        Integer[] c = {3, 4, 6, 7};//§7隔热层
        Integer[] d = {40, 41, 42};//§4熔炉火焰
        Integer[] e = {45, 46, 47, 48, 49, 50, 51, 52, 53};
        for (int i = 0; i <= 53; i++) {

            List<Integer> Glass = Arrays.asList(c);
            if (Glass.contains(i)) {
                inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))}).set(ItemStack.builder().itemType(ItemTypes.GLASS_PANE).add(Keys.DISPLAY_NAME, Text.of(TextColors.GRAY, "隔热层")).build());
            }
            List<Integer> Glass_blue = Arrays.asList(a);
            if (Glass_blue.contains(i)) {
                inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))}).set(ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.ITEM_DURABILITY, -11).add(Keys.DISPLAY_NAME, Text.of(TextColors.DARK_BLUE, "冷却层")).build());
            }
            List<Integer> Iron_zha = Arrays.asList(b);
            if (Iron_zha.contains(i)) {
                inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))}).set(ItemStack.builder().itemType(ItemTypes.IRON_BARS).add(Keys.DISPLAY_NAME, Text.of(TextColors.GRAY, "熔炉壁")).build());
            }
            if (i == 28)
                inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))}).set(ItemStack.builder().itemType(ItemTypes.ENCHANTING_TABLE).add(Keys.DISPLAY_NAME, Text.of(TextColors.AQUA, "强化台")).build());
            List<Integer> Blaze = Arrays.asList(d);
            if (Blaze.contains(i)) {
                inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))}).set(ItemStack.builder().itemType(ItemTypes.BLAZE_POWDER).add(Keys.DISPLAY_NAME, Text.of(TextColors.RED, "熔炉火焰")).build());
            }
            if (i == 17)
                inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))}).set(ItemStack.builder().itemType(ItemTypes.ANVIL).add(Keys.DISPLAY_NAME, Text.of(TextColors.DARK_PURPLE, "保护石放置处")).build());
            if (i == 35)
                inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))}).set(ItemStack.builder().itemType(ItemTypes.END_PORTAL_FRAME).add(Keys.DISPLAY_NAME, Text.of(TextColors.RED, "幸运石放置处")).build());
            if (i == 44)
                inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))}).set(ItemStack.builder().itemType(ItemTypes.CRAFTING_TABLE).add(Keys.DISPLAY_NAME, Text.of(TextColors.RED, "§l开始强化")).build());
            if (i == 5)//§c熔炼炉
                inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))}).set(ItemStack.builder().itemType(ItemTypes.FURNACE).add(Keys.DISPLAY_NAME, Text.of(TextColors.RED, "熔炼炉")).build());
            List<Integer> Black_Glass = Arrays.asList(e);
            if (Black_Glass.contains(i)) {
                inv.query(new QueryOperation[]{QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(i))}).set(ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).add(Keys.ITEM_DURABILITY, -15).add(Keys.DISPLAY_NAME, Text.of(TextColors.GRAY, "等待强化")).build());
            }
        }


    }
}



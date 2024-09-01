package io.wispforest.affinity.client.hud;

import com.google.common.base.Suppliers;
import io.wispforest.affinity.Affinity;
import io.wispforest.affinity.aethumflux.net.AethumLink;
import io.wispforest.affinity.blockentity.template.AethumNetworkMemberBlockEntity;
import io.wispforest.affinity.item.IridescenceWandItem;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Positioning;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.hud.Hud;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class AethumNetworkLinkingHud {

    public static final Identifier COMPONENT_ID = Affinity.id("aethum_linking");

    public static void initialize() {
        var component = Suppliers.<Component>memoize(() -> {
            return Containers.verticalFlow(Sizing.content(), Sizing.content())
                    .positioning(Positioning.relative(50, 50))
                    .margins(Insets.right(32));
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world == null) return;

            //noinspection DataFlowIssue
            var stack = client.player.getMainHandStack();
            if (stack.getItem() instanceof IridescenceWandItem wand) {
                if (!Hud.hasComponent(COMPONENT_ID)) Hud.add(COMPONENT_ID, component);

                var potentialComponent = Hud.getComponent(COMPONENT_ID);
                if (!(potentialComponent instanceof FlowLayout container)) return;

                var storedPos = wand.getStoredPos(stack);
                var blockEntity = storedPos != null
                        ? client.world.getBlockEntity(storedPos)
                        : null;

                container.<FlowLayout>configure(layout -> {
                    layout.clearChildren();

                    if (blockEntity != null) {
                        layout.child(Components.block(blockEntity.getCachedState().getBlock().getDefaultState(), blockEntity)
                                .sizing(Sizing.fixed(16)));

                        var linkActionLabel = Text.empty();
                        switch (stack.get(IridescenceWandItem.MODE_KEY)) {
                            case BIND -> {
                                linkActionLabel = Text.literal("+").styled(style -> style.withColor(0x28FFBF));

                                var linkData = stack.get(IridescenceWandItem.LINK_DATA_KEY);
                                if (!linkData.has(AethumNetworkMemberBlockEntity.LINK_TYPE_KEY)) break;
                                if (linkData.get(AethumNetworkMemberBlockEntity.LINK_TYPE_KEY) == AethumLink.Type.PUSH) {
                                    linkActionLabel = Text.literal("→").styled(style -> style.withColor(0x3955E5));
                                } else if (linkData.get(AethumNetworkMemberBlockEntity.LINK_TYPE_KEY) == AethumLink.Type.PRIORITIZED) {
                                    linkActionLabel = Text.literal("⌘").styled(style -> style.withFormatting(Formatting.YELLOW));
                                }
                            }
                            case RELEASE ->
                                    linkActionLabel = Text.literal("-").styled(style -> style.withColor(0xEB1D36));
                        }

                        layout.child(Components.label(linkActionLabel)
                                .shadow(true)
                                .positioning(Positioning.relative(100, 100))
                                .zIndex(750));
                    }
                });
            } else {
                Hud.remove(COMPONENT_ID);
            }
        });
    }

}

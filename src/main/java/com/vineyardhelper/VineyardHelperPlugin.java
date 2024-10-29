package com.vineyardhelper;

import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.GraphicsObject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.api.events.GraphicsObjectCreated;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.events.GameTick;


import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;


@PluginDescriptor(
        name = "Vineyard Helper",
        description = "Highlights pickable grapevines and indicates when Grape barrel is full",
        tags = {"highlight", "grape", "vineyard", "grapevine", "wine", "aldarin"}
)
public class VineyardHelperPlugin extends Plugin {
    @Inject
    private Client client;

    @Inject
    private VineyardHelperConfig config;

    @Inject
    private VineyardHelperOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    private final Set<GraphicsObject> highlightedObjects = new HashSet<>();

    public boolean full = false;

    @Provides
    VineyardHelperConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(VineyardHelperConfig.class);
    }

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
        highlightedObjects.clear();
    }

    @Subscribe
    public void onGraphicsObjectCreated(GraphicsObjectCreated event) {
        GraphicsObject graphicsObject = event.getGraphicsObject();
        int objectId = graphicsObject.getId();

        if (config.objectIds() == objectId) {
            highlightedObjects.add(graphicsObject);
        }
    }

    public Set<GraphicsObject> getHighlightedObjects() {
        return highlightedObjects;
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        String message = event.getMessage();

        // Check for if the barrel is full
        if (message.contains("Your grape barrel is now full")) {
            full = true;
            }

        if (message.contains("The grape barrel is empty")) {
            full = false;
        }

        if (message.contains("The grape barrel is partially full")) {
            full = false;
        }
    }
    @Subscribe
    public void onGameTick(GameTick event) {
        // Check for NPC dialogue widget using group and child IDs
        Widget npcDialogueWidget = client.getWidget(ComponentID.DIALOG_NPC_TEXT);
        if (npcDialogueWidget != null) {
            String currentDialogue = npcDialogueWidget.getText();

            // Only process if dialogue has changed
            if (!currentDialogue.equals(lastDialogue)) {
                processDialogue(currentDialogue);
                lastDialogue = currentDialogue;
            }
        }
    }
    private String lastDialogue = "";

    private void processDialogue(String dialogue) {
        // Check for dialogue corresponding to handing in barrel
        if (dialogue.contains("Great work!")) {
            full = false;
        }
    }

}

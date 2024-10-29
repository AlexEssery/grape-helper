package com.vineyardhelper;

import net.runelite.api.Client;
import net.runelite.api.GraphicsObject;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import javax.inject.Inject;
import java.awt.*;
import java.awt.Polygon;
import java.util.Iterator;
import java.util.Set;

public class VineyardHelperOverlay extends Overlay {
    private final VineyardHelperPlugin plugin;
    private final Client client;

    @Inject
    public VineyardHelperOverlay(VineyardHelperPlugin plugin, Client client) {
        this.plugin = plugin;
        this.client = client;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(OverlayPriority.LOW);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        Set<GraphicsObject> highlightedGraphics = plugin.getHighlightedObjects();
        Iterator<GraphicsObject> iterator = highlightedGraphics.iterator();
        while (iterator.hasNext()) {
            GraphicsObject graphicsObject = iterator.next();

            if (graphicsObject.finished()) {
                // If the graphic object has finished, remove it from the highlighted set
                iterator.remove();
            } else {
                // Render the highlight for active graphic objects
                if (plugin.full) {
                    renderGraphicHighlight(graphics, graphicsObject, Color.RED);
                } else {
                    renderGraphicHighlight(graphics, graphicsObject, Color.GREEN);
                }
            }
        }
        return null;
    }

    private void renderGraphicHighlight(Graphics2D graphics, GraphicsObject graphicObject, Color color) {
        LocalPoint localPoint = graphicObject.getLocation(); // Get the local location of the graphic object
        if (localPoint != null) {
            // Get the polygon that represents the tile area for the graphic object
            Polygon polygon = Perspective.getCanvasTileAreaPoly(client, localPoint, 1); // Size of 1 for a single tile
            if (polygon != null) {
                // Draw the outline of the polygon
                graphics.setColor(color);
                graphics.drawPolygon(polygon);
            }
        }
    }
}

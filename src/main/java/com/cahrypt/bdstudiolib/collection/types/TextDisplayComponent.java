package com.cahrypt.bdstudiolib.collection.types;

import com.cahrypt.bdstudiolib.collection.CollectionComponent;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.joml.Matrix4f;

import java.util.List;

public abstract class TextDisplayComponent extends CollectionComponent<TextDisplay> {

    public TextDisplayComponent(Matrix4f localTransformation) {
        super(localTransformation);
    }

    @Override
    public List<TextDisplay> getDisplays(Location location) {
        TextDisplay textDisplay = location.getWorld().spawn(location, TextDisplay.class);
        textDisplay.text(getText());
        textDisplay.setAlignment(getAlignment());
        textDisplay.setLineWidth(getWidth());

        // maybe have a section that tells you what the hell the undeprecated version of this is
        textDisplay.setBackgroundColor(getBackgroundColor());
        textDisplay.setTextOpacity(getTextOpacity());

        // no background opacity function??
        textDisplay.setSeeThrough(isSeeThrough());

        return List.of(textDisplay);
    }

    public abstract Component getText();

    public abstract TextDisplay.TextAlignment getAlignment();

    public abstract int getWidth();

    public abstract Color getBackgroundColor();

    public abstract byte getTextOpacity();

    public abstract boolean isSeeThrough();
}

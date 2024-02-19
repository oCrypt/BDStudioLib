package com.cahrypt.bdstudiolib.collection.types;

import com.cahrypt.bdstudiolib.collection.SingletonBDComponent;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.TextDisplay;
import org.joml.Matrix4f;

// TODO this should not be abstract lmao
public abstract class TextDisplayBDComponent extends SingletonBDComponent<TextDisplay> {

    public TextDisplayBDComponent(String name, Matrix4f localTransformation) {
        super(name, localTransformation);
    }

    @Override
    public TextDisplay getDisplay(Location location) {
        TextDisplay textDisplay = location.getWorld().spawn(location, TextDisplay.class);
        textDisplay.text(getText());
        textDisplay.setAlignment(getAlignment());
        textDisplay.setLineWidth(getWidth());

        // maybe have a section that tells you what the hell the undeprecated version of this is
        textDisplay.setBackgroundColor(getBackgroundColor());
        textDisplay.setTextOpacity(getTextOpacity());

        // no background opacity function??
        textDisplay.setSeeThrough(isSeeThrough());

        return textDisplay;
    }

    public abstract net.kyori.adventure.text.Component getText();

    public abstract TextDisplay.TextAlignment getAlignment();

    public abstract int getWidth();

    public abstract Color getBackgroundColor();

    public abstract byte getTextOpacity();

    public abstract boolean isSeeThrough();
}

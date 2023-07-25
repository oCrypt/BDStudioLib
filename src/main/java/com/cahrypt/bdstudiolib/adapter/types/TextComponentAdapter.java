package com.cahrypt.bdstudiolib.adapter.types;

import com.cahrypt.bdstudiolib.adapter.ComponentAdapter;
import com.cahrypt.bdstudiolib.collection.types.TextDisplayComponent;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;

import java.lang.reflect.Type;

public class TextComponentAdapter implements ComponentAdapter<TextDisplayComponent> {

    @Override
    public TextDisplayComponent deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String text = deserializeName(object);

        ComponentBuilder<TextComponent, TextComponent.Builder> builder = Component.text();
        builder.append(Component.text(text));
        builder.color(TextColor.fromHexString(object.get("color").getAsString()));

        Style.Builder styleBuilder = Style.style();

        boolean bold = object.get("bold").getAsBoolean();
        boolean italic = object.get("italic").getAsBoolean();
        boolean underlined = object.get("underlined").getAsBoolean();
        boolean strikethrough = object.get("strikethrough").getAsBoolean();

        if (bold) {
            styleBuilder.decorate(TextDecoration.BOLD);
        }

        if (italic) {
            styleBuilder.decorate(TextDecoration.ITALIC);
        }

        if (underlined) {
            styleBuilder.decorate(TextDecoration.UNDERLINED);
        }

        if (strikethrough) {
            styleBuilder.decorate(TextDecoration.STRIKETHROUGH);
        }

        builder.style(styleBuilder.build());

        return new TextDisplayComponent(deserializeTransformation(object)) {
            @Override
            public Component getText() {
                return builder.build();
            }

            @Override
            public TextDisplay.TextAlignment getAlignment() {
                return TextDisplay.TextAlignment.valueOf(object.get("alignment").getAsString().toUpperCase());
            }

            @Override
            public int getWidth() {
                return object.get("length").getAsInt();
            }

            @Override
            public Color getBackgroundColor() {
                TextColor color = TextColor.fromHexString(object.get("backgroundColor").getAsString());

                if (color == null) {
                    return Color.WHITE;
                }

                return Color.fromRGB(color.red(), color.green(), color.blue());
            }

            @Override
            public byte getTextOpacity() {
                return (byte) (127 * object.get("alpha").getAsFloat());
            }

            @Override
            public boolean isSeeThrough() {
                return object.get("backgroundAlpha").getAsFloat() < 0.5F;
            }
        };
    }
}

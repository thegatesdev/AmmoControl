package io.github.thegatesdev.crossyourbows.interaction;

import net.kyori.adventure.audience.*;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.*;

public final class Messages {

    public static void warn(Audience audience, String text) {
        audience.sendMessage(Component.text(text, NamedTextColor.RED));
    }

    public static void tell(Audience audience, String text) {
        audience.sendMessage(Component.text(text));
    }

    public static void success(Audience audience, String text) {
        audience.sendMessage(Component.text(text, NamedTextColor.GREEN));
    }
}

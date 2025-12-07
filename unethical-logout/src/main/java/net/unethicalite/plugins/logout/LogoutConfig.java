package net.unethicalite.plugins.logout;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("unethicallogout")
public interface LogoutConfig extends Config
{
    @ConfigItem(
            keyName = "enabled",
            name = "Activado",
            description = "Activar o desactivar el plugin",
            position = 0
    )
    default boolean enabled()
    {
        return true;
    }

    @ConfigItem(
            keyName = "whitelist",
            name = "Lista Blanca",
            description = "Nombres a ignorar separados por coma",
            position = 1
    )
    default String whitelist()
    {
        return "";
    }

    @ConfigItem(
            keyName = "combatRangeMin",
            name = "Nivel Combat Min",
            description = "Ignorar enemigos menores a este nivel",
            position = 2
    )
    default int combatRangeMin()
    {
        return 3;
    }

    @ConfigItem(
            keyName = "combatRangeMax",
            name = "Nivel Combat Max",
            description = "Ignorar enemigos mayores a este nivel",
            position = 3
    )
    default int combatRangeMax()
    {
        return 126;
    }
}
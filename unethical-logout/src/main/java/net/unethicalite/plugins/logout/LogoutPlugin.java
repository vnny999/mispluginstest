package net.unethicalite.plugins.logout;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "Unethical Logout",
        description = "Logout automatico con filtro de nivel",
        enabledByDefault = false,
        tags = {"combat", "logout", "pvp"}
)
public class LogoutPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private LogoutConfig config;

    @Provides
    LogoutConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(LogoutConfig.class);
    }

    @Subscribe
    public void onGameTick(GameTick event)
    {
        // 1. Validaciones: Si no estamos logueados, no hacemos nada
        if (client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer() == null)
        {
            return;
        }

        // Si el plugin está desactivado en la config, salimos
        if (!config.enabled())
        {
            return;
        }

        // 2. Procesar Whitelist (Hecho manual para evitar errores de importación)
        List<String> whitelist = new ArrayList<>();
        String whitelistRaw = config.whitelist();
        if (whitelistRaw != null && !whitelistRaw.isEmpty())
        {
            String[] names = whitelistRaw.split(",");
            for (String name : names)
            {
                whitelist.add(name.trim().toLowerCase());
            }
        }

        // 3. Escanear jugadores
        for (Player player : client.getPlayers())
        {
            if (player == null || player.getName() == null) continue;

            // Ignorarnos a nosotros mismos
            if (player == client.getLocalPlayer())
            {
                continue;
            }

            // Ignorar amigos (Comparamos nombres en minúscula)
            if (whitelist.contains(player.getName().toLowerCase()))
            {
                continue;
            }

            // --- FILTRO DE NIVEL DE COMBATE ---
            int enemyLevel = player.getCombatLevel();

            // Si el nivel del enemigo está FUERA de tu rango, lo ignoramos
            if (enemyLevel < config.combatRangeMin() || enemyLevel > config.combatRangeMax())
            {
                continue;
            }
            // ----------------------------------

            // Si llegamos aquí, ¡es un enemigo válido! Logout.
            logout();
            break;
        }
    }

    private void logout()
    {
        // Forzar desconexión
        if (client.getGameState() != GameState.LOGIN_SCREEN)
        {
            client.setGameState(GameState.LOGIN_SCREEN);
        }
    }
}
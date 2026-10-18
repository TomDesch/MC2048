package io.stealingdapenta.mc2048;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.stealingdapenta.mc2048.persistence.PlayerDataService;
import io.stealingdapenta.mc2048.utils.HighScoreManager;
import io.stealingdapenta.mc2048.utils.InventoryUtil;
import io.stealingdapenta.mc2048.utils.MessageSender;
import io.stealingdapenta.mc2048.utils.data.ActiveGame;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameManagerTest {

    private InventoryUtil inventoryUtil;
    private PlayerDataService playerDataService;
    private GameManager gameManager;
    private Player player;
    private ActiveGame activeGame;

    @BeforeEach
    void setUp() throws ReflectiveOperationException {
        MC2048 plugin = mock(MC2048.class);
        when(plugin.getLogger()).thenReturn(Logger.getLogger(GameManagerTest.class.getName()));
        inventoryUtil = mock(InventoryUtil.class);
        playerDataService = mock(PlayerDataService.class);
        gameManager = new GameManager(
            plugin,
            inventoryUtil,
            mock(HighScoreManager.class),
            playerDataService,
            mock(MessageSender.class)
        );
        player = mock(Player.class);
        activeGame = mock(ActiveGame.class);
        UUID playerId = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(playerId);
        when(activeGame.getPlayer()).thenReturn(player);
        addActiveGame(playerId, activeGame);
    }

    @Test
    void saveOnDepartureWithTerminalSavePendingDoesNotQueuePauseSave() {
        when(activeGame.isTerminalSavePending()).thenReturn(true);

        gameManager.saveOnDeparture(player);

        verify(inventoryUtil, never()).createSavedGame(activeGame);
        verifyNoInteractions(playerDataService);
    }

    @Test
    void saveOnDepartureWithLockedMoveDefersSaveUntilMoveFinishes() {
        when(activeGame.isLocked()).thenReturn(true);

        gameManager.saveOnDeparture(player);

        verify(activeGame).requestClose();
        verify(inventoryUtil, never()).createSavedGame(activeGame);
        verifyNoInteractions(playerDataService);
    }

    private void addActiveGame(UUID playerId, ActiveGame game) throws ReflectiveOperationException {
        Field field = GameManager.class.getDeclaredField("activeGames");
        field.setAccessible(true);
        Object activeGames = field.get(gameManager);
        Map.class.getMethod("put", Object.class, Object.class)
                           .invoke(activeGames, playerId, game);
    }
}

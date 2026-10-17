package io.stealingdapenta.mc2048.persistence;

import io.stealingdapenta.mc2048.utils.data.SavedGame;
import java.util.Optional;
import java.util.UUID;

public record PlayerData(
    UUID playerId,
    String playerName,
    int highScore,
    int attempts,
    long playtime,
    double averageScore,
    int animationSpeed,
    Optional<SavedGame> savedGame
) {

    public PlayerData {
        savedGame = savedGame == null ? Optional.empty() : savedGame;
    }

    public PlayerData withSavedGame(SavedGame game) {
        return new PlayerData(
            playerId, playerName, highScore, attempts, playtime, averageScore, animationSpeed, Optional.of(game));
    }

    public PlayerData withoutSavedGame() {
        return new PlayerData(
            playerId, playerName, highScore, attempts, playtime, averageScore, animationSpeed, Optional.empty());
    }

    public PlayerData withAnimationSpeed(int speed) {
        return new PlayerData(
            playerId, playerName, highScore, attempts, playtime, averageScore, speed, savedGame);
    }

    public PlayerData completed(int score, long elapsedPlaytime, double newAverageScore) {
        return new PlayerData(
            playerId,
            playerName,
            Math.max(highScore, score),
            attempts + 1,
            playtime + elapsedPlaytime,
            newAverageScore,
            animationSpeed,
            Optional.empty()
        );
    }
}

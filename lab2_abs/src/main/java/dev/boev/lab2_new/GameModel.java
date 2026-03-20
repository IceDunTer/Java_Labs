package dev.boev.lab2_new;

import javafx.beans.property.*;
import javafx.geometry.Point2D;

public class GameModel {
    private final IntegerProperty score = new SimpleIntegerProperty(0);
    private final BooleanProperty gameActive = new SimpleBooleanProperty(false);
    private final DoubleProperty ballSpeed = new SimpleDoubleProperty(500.0); // Теперь double
    private final ObjectProperty<Point2D> ballPosition = new SimpleObjectProperty<>(new Point2D(0, 0));

    private static final int SPEED_INCREASE_INTERVAL = 5; // каждые 5 попаданий
    private static final double SPEED_INCREASE_PERCENT = 0.10; // ровно 10%
    private static final double MIN_SPEED = 100.0; // минимальный интервал

    public GameModel() {
        score.addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() > 0 && newVal.intValue() % SPEED_INCREASE_INTERVAL == 0) {
                increaseSpeed();
            }
        });
    }

    public int getScore() { return score.get(); }
    public IntegerProperty scoreProperty() { return score; }
    public void setScore(int score) { this.score.set(score); }

    public boolean isGameActive() { return gameActive.get(); }
    public BooleanProperty gameActiveProperty() { return gameActive; }
    public void setGameActive(boolean gameActive) { this.gameActive.set(gameActive); }

    // Методы для double скорости
    public double getBallSpeed() { return ballSpeed.get(); }
    public DoubleProperty ballSpeedProperty() { return ballSpeed; }
    public void setBallSpeed(double ballSpeed) {
        this.ballSpeed.set(Math.max(ballSpeed, MIN_SPEED));
    }

    public Point2D getBallPosition() { return ballPosition.get(); }
    public ObjectProperty<Point2D> ballPositionProperty() { return ballPosition; }
    public void setBallPosition(Point2D ballPosition) { this.ballPosition.set(ballPosition); }

    // Точное увеличение на 10%
    public void increaseSpeed() {
        double currentSpeed = getBallSpeed();
        double newSpeed = currentSpeed * (1.0 - SPEED_INCREASE_PERCENT); // Умножаем на 0.9
        setBallSpeed(newSpeed);
    }

    public void reset() {
        setScore(0);
        setBallSpeed(500.0);
        setGameActive(true);
    }
}
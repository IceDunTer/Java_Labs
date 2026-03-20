package dev.boev.lab2_new;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import java.util.Random;

public class GameController {
    @FXML private Pane pane;
    @FXML private Circle ball;
    @FXML private Label scoreLabel;
    @FXML private Label speedLabel;

    private GameModel gameModel;
    private AnimationTimer animationTimer;
    private final Random random = new Random();

    private double dx = 2;
    private double dy = 2;

    @FXML
    public void initialize() {
        gameModel = new GameModel();

        scoreLabel.textProperty().bind(
                Bindings.concat(" ", gameModel.scoreProperty())
        );

        speedLabel.textProperty().bind(
                Bindings.createStringBinding(
                        () -> String.format(" %.0f%%", 500.0 / gameModel.getBallSpeed() * 100),
                        gameModel.ballSpeedProperty()
                )
        );

        initAnimationTimer();
        gameModel.setGameActive(true);
        setGame();
    }

    private void initAnimationTimer() {
        animationTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            private long updateInterval = 16;

            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }

                long elapsed = now - lastUpdate;
                if (elapsed > updateInterval) {
                    moveBallSmoothly();
                    lastUpdate = now;
                }
            }
        };
    }

    private void moveBallSmoothly() {
        if (pane == null || ball == null) return;

        double w = pane.getWidth();
        double h = pane.getHeight();
        double r = ball.getRadius();
        double x = ball.getCenterX();
        double y = ball.getCenterY();

        double speedMultiplier = 500.0 / gameModel.getBallSpeed();
        double currentDx = dx * speedMultiplier;
        double currentDy = dy * speedMultiplier;

        double newX = x + currentDx;
        double newY = y + currentDy;

        if (newX - r <= 0 || newX + r >= w) {
            dx = -dx;
            newX = x + dx * speedMultiplier;
        }

        if (newY - r <= 0 || newY + r >= h) {
            dy = -dy;
            newY = y + dy * speedMultiplier;
        }

        newX = Math.max(r, Math.min(newX, w - r));
        newY = Math.max(r, Math.min(newY, h - r));

        ball.setCenterX(newX);
        ball.setCenterY(newY);
        gameModel.setBallPosition(new javafx.geometry.Point2D(newX, newY));
    }

    public void setGame() {
        if (pane.getWidth() > 0 && pane.getHeight() > 0) {
            double centerX = pane.getWidth() / 2;
            double centerY = pane.getHeight() / 2;
            ball.setCenterX(centerX);
            ball.setCenterY(centerY);
            gameModel.setBallPosition(new javafx.geometry.Point2D(centerX, centerY));

            dx = (random.nextBoolean() ? 1 : -1) * 2;
            dy = (random.nextBoolean() ? 1 : -1) * 2;
        }

        animationTimer.start();
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        if (event.getButton() != javafx.scene.input.MouseButton.PRIMARY) {
            return;
        }

        double clickDx = event.getX() - ball.getCenterX();
        double clickDy = event.getY() - ball.getCenterY();
        double distance = Math.sqrt(clickDx * clickDx + clickDy * clickDy);

        if (distance <= ball.getRadius()) {
            gameModel.setScore(gameModel.getScore() + 1);
            teleportBall();
            this.dx = (random.nextBoolean() ? 1 : -1) * 2;
            this.dy = (random.nextBoolean() ? 1 : -1) * 2;
        }
    }

    private void teleportBall() {
        double w = pane.getWidth();
        double h = pane.getHeight();
        double r = ball.getRadius();

        if (w <= 0 || h <= 0) return;

        double minX = r;
        double minY = r;
        double maxX = w - r;
        double maxY = h - r;

        double newX = minX + random.nextDouble() * (maxX - minX);
        double newY = minY + random.nextDouble() * (maxY - minY);

        ball.setCenterX(newX);
        ball.setCenterY(newY);
        gameModel.setBallPosition(new javafx.geometry.Point2D(newX, newY));
    }

    public void restartGame() {
        gameModel.reset();
        dx = (random.nextBoolean() ? 1 : -1) * 2;
        dy = (random.nextBoolean() ? 1 : -1) * 2;
        setGame();
    }
}
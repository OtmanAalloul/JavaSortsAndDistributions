package com.example.projectdatastructure.Presentation;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Calendar;
import java.util.GregorianCalendar;

/** Note that this clock does not keep perfect time, but is close. 
 It's main purpose is to demonstrate various features of JavaFX. */
public class Clock {
    public VBox getClock() throws Exception {
        // construct the analogueClock pieces.
        final Circle face     = new Circle(80, 80, 80);
        face.setId("face");

        final Label brand     = new Label("DataSorter App");
        brand.setId("brand");

        brand.layoutXProperty().bind(face.centerXProperty().subtract(brand.widthProperty().divide(2)));
        brand.layoutYProperty().bind(face.centerYProperty().add(face.radiusProperty().divide(2)));
        final Line hourHand   = new Line(0, 0, 0, -50);

        hourHand.setTranslateX(80);   hourHand.setTranslateY(80);
        hourHand.setId("hourHand");

        final Line minuteHand = new Line(0, 0, 0, -65);
        minuteHand.setTranslateX(80); minuteHand.setTranslateY(80);
        minuteHand.setId("minuteHand");

        final Line secondHand = new Line(0, 15, 0, -75);
        secondHand.setTranslateX(80); secondHand.setTranslateY(80);
        secondHand.setId("secondHand");


        final Circle spindle = new Circle(80, 80, 5);
        spindle.setId("spindle");
        Group ticks = new Group();
        for (int i = 0; i < 12; i++) {
            Line tick = new Line(0, -73, 0, -81);
            tick.setTranslateX(80); tick.setTranslateY(80);
            tick.getStyleClass().add("tick");
            tick.getTransforms().add(new Rotate(i * (360 / 12)));
            ticks.getChildren().add(tick);
        }
        final Group analogueClock = new Group(face, brand, ticks, spindle, hourHand, minuteHand, secondHand);

        // determine the starting time.
        Calendar calendar            = GregorianCalendar.getInstance();
        final double seedSecondDegrees  = calendar.get(Calendar.SECOND) * (360 / 60);
        final double seedMinuteDegrees  = (calendar.get(Calendar.MINUTE) + seedSecondDegrees / 360.0) * (360 / 60);
        final double seedHourDegrees    = (calendar.get(Calendar.HOUR)   + seedMinuteDegrees / 360.0) * (360 / 12) ;

        // define rotations to map the analogueClock to the current time.
        final Rotate hourRotate      = new Rotate(seedHourDegrees);
        final Rotate minuteRotate    = new Rotate(seedMinuteDegrees);
        final Rotate secondRotate    = new Rotate(seedSecondDegrees);
        hourHand.getTransforms().add(hourRotate);
        minuteHand.getTransforms().add(minuteRotate);
        secondHand.getTransforms().add(secondRotate);

        // the hour hand rotates twice a day.
        final Timeline hourTime = new Timeline(
                new KeyFrame(
                        Duration.hours(12),
                        new KeyValue(
                                hourRotate.angleProperty(),
                                360 + seedHourDegrees,
                                Interpolator.LINEAR
                        )
                )
        );

        // the minute hand rotates once an hour.
        final Timeline minuteTime = new Timeline(
                new KeyFrame(
                        Duration.minutes(60),
                        new KeyValue(
                                minuteRotate.angleProperty(),
                                360 + seedMinuteDegrees,
                                Interpolator.LINEAR
                        )
                )
        );

        // move second hand rotates once a minute.
        final Timeline secondTime = new Timeline(
                new KeyFrame(
                        Duration.seconds(60),
                        new KeyValue(
                                secondRotate.angleProperty(),
                                360 + seedSecondDegrees,
                                Interpolator.LINEAR
                        )
                )
        );


        // time never ends.
        hourTime.setCycleCount(Animation.INDEFINITE);
        minuteTime.setCycleCount(Animation.INDEFINITE);
        secondTime.setCycleCount(Animation.INDEFINITE);
        secondTime.play();
        minuteTime.play();
        hourTime.play();

        // add a glow effect whenever the mouse is positioned over the clock.
        final Glow glow = new Glow();
        analogueClock.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                analogueClock.setEffect(glow);
                analogueClock.setCursor(Cursor.CLOSED_HAND);
            }
        });
        analogueClock.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                analogueClock.setEffect(null);
            }
        });


        final VBox layout = new VBox();
        layout.getChildren().addAll(analogueClock);
        layout.setAlignment(Pos.CENTER);
        layout.getStylesheets().add(getClass().getResource("/com/example/projectdatastructure/styles/clock.css").toExternalForm());

        return layout;
    }

    private String pad(int fieldWidth, char padChar, String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < fieldWidth; i++) {
            sb.append(padChar);
        }
        sb.append(s);

        return sb.toString();
    }
}
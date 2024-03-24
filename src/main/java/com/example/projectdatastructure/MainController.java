package com.example.projectdatastructure;

import com.example.projectdatastructure.Presentation.Clock;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class MainController implements Initializable {

    public JFXButton trash;
    @FXML
    private StackPane rootPane;

    @FXML
    private Label date;

    @FXML
    private Label title;

    @FXML
    private VBox calendar;


    Service<AnchorPane> loadDistributionTask = new Service() {
        @Override
        protected Task<AnchorPane> createTask() {
            return new Task() {
                @Override
                protected AnchorPane call() throws Exception {
                    Platform.runLater(() -> {
                        JFXSpinner spinner = new JFXSpinner(JFXSpinner.INDETERMINATE_PROGRESS);
                        spinner.setMaxHeight(100);
                        spinner.setMaxWidth(100);
                        rootPane.getChildren().add(spinner);
                    });

                    return new FXMLLoader(getClass().getResource("/com/example/projectdatastructure/fxmls/logisticDistribution.fxml")).load();
                }
            };
        }
    };

    Service<AnchorPane> loadMeansVariancesTask = new Service() {
        @Override
        protected Task<AnchorPane> createTask() {
            return new Task() {
                @Override
                protected AnchorPane call() throws Exception {
                    Platform.runLater(() -> {
                        JFXSpinner spinner = new JFXSpinner(JFXSpinner.INDETERMINATE_PROGRESS);
                        spinner.setMaxHeight(100);
                        spinner.setMaxWidth(100);
                        rootPane.getChildren().add(spinner);
                    });

                    return new FXMLLoader(getClass().getResource("/com/example/projectdatastructure/fxmls/meansVariances.fxml")).load();
                }
            };
        }
    };
    Service<AnchorPane> loadMergeSortTask = new Service() {
        @Override
        protected Task<AnchorPane> createTask() {
            return new Task() {
                @Override
                protected AnchorPane call() throws Exception {
                    Platform.runLater(() -> {
                        JFXSpinner spinner = new JFXSpinner(JFXSpinner.INDETERMINATE_PROGRESS);
                        spinner.setMaxHeight(100);
                        spinner.setMaxWidth(100);
                        rootPane.getChildren().add(spinner);
                    });

                    return new FXMLLoader(getClass().getResource("/com/example/projectdatastructure/fxmls/mergeSort.fxml")).load();
                }
            };
        }
    };
    Service<AnchorPane> loadAboutTask = new Service() {
        @Override
        protected Task<AnchorPane> createTask() {
            return new Task() {
                @Override
                protected AnchorPane call() throws Exception {
                    Platform.runLater(() -> {
                        JFXSpinner spinner = new JFXSpinner(JFXSpinner.INDETERMINATE_PROGRESS);
                        spinner.setMaxHeight(100);
                        spinner.setMaxWidth(100);
                        rootPane.getChildren().add(spinner);
                    });

                    return new FXMLLoader(getClass().getResource("/com/example/projectdatastructure/fxmls/about.fxml")).load();
                }
            };
        }
    };
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        //Show Date:
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = format.format( new Date());
        date.setText(dateString);

        //Show calendar:
        try {
            calendar.getChildren().add(new Clock().getClock());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            loadIntroduction();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void loadIntroduction() throws IOException {
        title.setText("Applying Merge Sort on logistic distribution data");
        AnchorPane loader = new FXMLLoader(getClass().getResource("/com/example/projectdatastructure/fxmls/introduction.fxml")).load();
        rootPane.getChildren().clear();
        rootPane.getChildren().add(loader);
    }

    private void loadDistribution() throws IOException {

        loadDistributionTask.setOnSucceeded(workerStateEvent -> {
            title.setText("Logistic Distribution");
            rootPane.getChildren().clear();
            rootPane.getChildren().add(loadDistributionTask.getValue());
        });

        loadDistributionTask.setOnCancelled(workerStateEvent -> {
            System.out.println("Cancelled");
        });

        loadDistributionTask.restart();
    }
    private void loadMergeSort() throws IOException {

        loadMergeSortTask.setOnSucceeded(workerStateEvent -> {
            title.setText("Merge Sort");
            rootPane.getChildren().clear();
            rootPane.getChildren().add(loadMergeSortTask.getValue());
        });

        loadMergeSortTask.setOnCancelled(workerStateEvent -> {
            System.out.println("Cancelled");
        });

        loadMergeSortTask.restart();
    }
    private void loadMeansVariances() throws IOException {

        loadMeansVariancesTask.setOnSucceeded(workerStateEvent -> {
            title.setText("Means & Variances");
            rootPane.getChildren().clear();
            rootPane.getChildren().add(loadMeansVariancesTask.getValue());
        });

        loadMeansVariancesTask.setOnCancelled(workerStateEvent -> {
            System.out.println("Cancelled");
        });

        loadMeansVariancesTask.restart();
    }
    private void loadAbout() throws IOException {

        loadAboutTask.setOnSucceeded(workerStateEvent -> {
            title.setText("About");
            rootPane.getChildren().clear();
            rootPane.getChildren().add(loadAboutTask.getValue());
        });

        loadAboutTask.setOnCancelled(workerStateEvent -> {
            System.out.println("Cancelled");
        });

        loadAboutTask.restart();
    }
    @FXML
    private void loadIntroductionAction(ActionEvent e) throws IOException {
        loadDistributionTask.cancel();
        loadIntroduction();
    }

    @FXML
    private void loadDistributionAction(ActionEvent e) throws IOException {
        loadDistribution();
    }

    @FXML
    private void loadMeansVariancesAction(ActionEvent e) throws IOException {
        loadMeansVariances();
    }
    @FXML
    private void loadMergeSortAction(ActionEvent e) throws IOException {
        loadMergeSort();
    }
    @FXML
    private void loadAboutAction(ActionEvent e) throws IOException {
        loadAbout();
    }

}
package org.example.ui;

import javafx.application.Application;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.model.EmployeePair;
import org.example.model.EmployeePairStats;
import org.example.model.ui.UiRow;
import org.example.service.parser.CsvParserService;
import org.example.service.parser.DateParserService;
import org.example.service.ProjectAnalysisService;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class MainApp extends Application {

    private final ProjectAnalysisService service =
            new ProjectAnalysisService(new CsvParserService(new DateParserService()));

    @Override
    public void start(Stage stage) {
        stage.setTitle("Project Records Analyzer");

        // Top panel with labels and a select file button
        final Label fileLabel = new Label("No file selected");
        final Label pairLabel = new Label("Employees: -");
        final Label totalDaysLabel = new Label("Total days worked together: -");

        final Button selectButton = new Button("Select CSV file");

        // Table with the longest working pair's common projects
        final TableView<UiRow> table = new TableView<>();

        final TableColumn<UiRow, Number> emp1Col = new TableColumn<>("Employee #1");
        emp1Col.setCellValueFactory(data ->
                new SimpleLongProperty(data.getValue().employee1_ID()));

        final TableColumn<UiRow, Number> emp2Col = new TableColumn<>("Employee #2");
        emp2Col.setCellValueFactory(data ->
                new SimpleLongProperty(data.getValue().employee2_ID()));

        final TableColumn<UiRow, Number> projectCol = new TableColumn<>("Project ID");
        projectCol.setCellValueFactory(data ->
                new SimpleLongProperty(data.getValue().projectId()));

        final TableColumn<UiRow, Number> daysCol = new TableColumn<>("Days worked");
        daysCol.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().daysWorked()));

        table.getColumns().addAll(emp1Col, emp2Col, projectCol, daysCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No data loaded"));

        // On button click
        selectButton.setOnAction(_ -> {
            final FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose CSV file");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files", "*.csv"));

            final File file = chooser.showOpenDialog(stage);

            if (file == null) {
                return;
            }

            try {
                final Path path = file.toPath();
                fileLabel.setText("Selected: " + file.getName());

                final EmployeePairStats pairStats = service.analyzeProjectRecords(path);

                pairLabel.setText("Employees: " + pairStats.getPair().getEmployee1_ID() + " and " + pairStats.getPair().getEmployee2_ID());
                totalDaysLabel.setText("Total days worked together: " + pairStats.getTotalDaysWorked());
                table.setItems(FXCollections.observableArrayList(getUiRows(pairStats)));
            } catch (Exception ex) {
                showError(ex);
            }
        });

        final VBox root = new VBox(12);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(selectButton, fileLabel, pairLabel, totalDaysLabel, table);

        final Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
    }

    private static List<UiRow> getUiRows(EmployeePairStats pairStats) {
        final EmployeePair pair = pairStats.getPair();
        return pairStats.getOverlaps()
                .stream()
                .map(overlap -> new UiRow(pair.getEmployee1_ID(), pair.getEmployee2_ID(),
                        overlap.getProjectId(), overlap.getDaysWorked()))
                .collect(Collectors.toList());
    }

    private void showError(Exception exception) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Failed to process file");
        alert.setContentText(exception.getMessage());
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
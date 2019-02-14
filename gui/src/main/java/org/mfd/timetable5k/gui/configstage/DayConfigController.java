package org.mfd.timetable5k.gui.configstage;


import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.mfd.timetable5k.gui.TimeTableDataModel;

public class DayConfigController {

	private TimeTableDataModel model;
	@FXML
	private Label              numPeriodLabel;
	@FXML
	private Label              numOfDayLabel;
	@FXML
	private TextField          numPeriodADayText;
	@FXML
	private TextField          numDayText;
	@FXML
	private TableView<Integer> noLabTable;
	@FXML
	private TextField          labConstraintText;

	@FXML
	void setPeriodButtonClick() {
		try {
			model.setNumPeriodADay(Integer.valueOf(numPeriodADayText.getText()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}
		numPeriodLabel.setText(String.valueOf(model.getNumPeriodADay()));


	}
	@SuppressWarnings("unchecked")
	void initModel(TimeTableDataModel model) {
		this.model = model;
		noLabTable.setItems(model.getLabConstraintsObservableList());
		TableColumn<Integer, Number> constraintColumn = (TableColumn<Integer, Number>) noLabTable.getColumns().get(0);
		constraintColumn.setCellValueFactory(integerNumberCellDataFeatures -> new ReadOnlyIntegerWrapper(integerNumberCellDataFeatures.getValue()));

	}


	@FXML
	void setDayButtonClick() {
		try {
			model.setNumOfDay(Integer.valueOf(numDayText.getText()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return;
		}
		numOfDayLabel.setText(String.valueOf(model.getNumOfDay()));


	}

	@FXML
	void addNoLabButtonClick() {
		try {
			model.getLabConstraintsObservableList().add(Integer.valueOf(labConstraintText.getText()));
		} catch (NumberFormatException e) {
			e.printStackTrace();

		}

	}

	@FXML
	void deleteNoLabButtonClick() {
		int focusedIndex = noLabTable.getSelectionModel().getFocusedIndex();

		if (focusedIndex >= 0) noLabTable.getItems().remove(focusedIndex);

	}


}

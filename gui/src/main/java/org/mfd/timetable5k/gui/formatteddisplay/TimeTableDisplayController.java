package org.mfd.timetable5k.gui.formatteddisplay;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class TimeTableDisplayController {
	@FXML
	private GridPane gridPane;
	@FXML
	private GridPane teacherInfo;
	@FXML
	private Label title;
	@FXML
	private Label classLabel;


	GridPane getGridPane() {
		return gridPane;
	}

	GridPane getTeacherInfo() {
		return teacherInfo;
	}

	Label getTitle() {
		return title;
	}

	Label getClassLabel() {
		return classLabel;
	}
}

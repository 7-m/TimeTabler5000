package org.mfd.timetable5k.gui.formatteddisplay;

import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.mfd.timetable5k.core.Teacher;
import org.mfd.timetable5k.gui.Utils;

import java.io.IOException;

public class TeacherTimeTableDisplay {
	private static final String[] dayNames = {"", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	Teacher                    teacher;
	TimeTableDisplayController controller;
	private int   nDaysAWeek;
	private int   nPeriodsADay;
	private Stage stage;


	public TeacherTimeTableDisplay(Teacher teacher, int nDaysAWeek, int nPeriodsADay) {
		this.teacher = teacher;
		this.nDaysAWeek = nDaysAWeek;
		this.nPeriodsADay = nPeriodsADay;
	}

	public void show() {
		//create stage, load xml, show tt,

		initStageAndScene();
		makeTitle();
		makeClassroomLabel();
		makeTimeTable();

		stage.show();
	}

	private void makeClassroomLabel() {
		controller.getClassLabel().setText("Teacher: " + teacher.getName());

	}

	private void makeTitle() {
		Label title = controller.getTitle();
		title.setId("title-label");
		title.setText("Time Table For " + Utils.camelCaseCopy(teacher.getName()));

	}


	private void makeTimeTable() {
		GridPane grid = controller.getGridPane();

		//add day name and period number
		decoratewithPeriods(grid, nPeriodsADay);
		deocrateWithDays(grid, nDaysAWeek);

		//add the periods in a horizontal way
		for (int i = 0; i < nDaysAWeek; i++) {
			for (int j = 0; j < nPeriodsADay; j++) {

				final Label period = new Label();
				period.getStyleClass().add("period-label");

				final Teacher.AllotedPeriod allotedPeriod = teacher.hasPeriod(i * nPeriodsADay + j);
				if (allotedPeriod != null)
					period.setText(allotedPeriod.getClassroomName() + " : " + allotedPeriod.getSubjectName());

				grid.add(period, j + 1, i + 1);

			}
		}


	}

	private void decoratewithPeriods(GridPane grid, int nPeriodsADay) {
		for (int i = 1; i <= nPeriodsADay; i++) {
			Label label = new Label(String.valueOf(i));
			label.getStyleClass().add("period-num-label");
			grid.addColumn(i, label);
			GridPane.setHalignment(label, HPos.CENTER);

		}
	}

	private void deocrateWithDays(GridPane grid, int nDaysAWeek) {
		for (int i = 1; i <= nDaysAWeek; i++) {
			Label label = new Label(dayNames[i]);
			label.getStyleClass().add("day-label");
			grid.addRow(i, label);
			GridPane.setHalignment(label, HPos.CENTER);

		}
	}

	private void initStageAndScene() {
		stage = new Stage();
		FXMLLoader loader =
				new FXMLLoader(getClass().getResource(
						"/org/mfd/timetable5k/layouts/formatteddisplay/display.fxml"));
		try {
			stage.setScene(new Scene(loader.load()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		controller = loader.getController();


	}
}

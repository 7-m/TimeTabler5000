package org.mfd.timetable5k.gui.formatteddisplay;

import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.mfd.timetable5k.core.Classroom;
import org.mfd.timetable5k.core.Period;
import org.mfd.timetable5k.core.Subject;
import org.mfd.timetable5k.gui.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class TimeTableWindow {

	private              TimeTableDisplayController controller;
	private              Stage                      stage;

	private static final String[] dayNames = {"", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

	private              List<Period>               periods;
	private              Classroom                  classroom;
	private              int                        nDaysAWeek;
	private              int                        nPeriodsADay;


	public TimeTableWindow(List<Period> periods, Classroom classroom, int nDaysAWeek, int nPeriodsADay) {
		this.periods = periods;
		this.classroom = classroom;
		this.nDaysAWeek = nDaysAWeek;
		this.nPeriodsADay = nPeriodsADay;
	}

	public void show() {
		//create stage, load xml, show tt,

		initStageAndScene();
		makeTitle();
		makeClassroomLabel();
		makeTimeTable();
		makeTeacherTable();

		stage.show();
	}

	private void makeClassroomLabel() {
		controller.getClassLabel().setText("Semester: " + classroom.getName());

	}

	private void makeTitle() {
		Label title = controller.getTitle();
		title.setId("title-label");
		title.setText("Time Table For " + Utils.camelCaseCopy(classroom.getName()));

	}

	private void makeTeacherTable() {
		GridPane grid = controller.getTeacherInfo();

		int row = 0; // well have 3 entries in a row
		int col = 0;
		for (Subject s : classroom.getSubjectsToTeach()
		) {
			Label label = new Label(Utils.camelCaseCopy(s.getName()) + " --> " + Utils
					.camelCaseCopy(classroom.getTeacher(s).stream().map(teacher -> teacher.getName()).collect(Collectors.joining(" :: "))));
			label.getStyleClass().add("teacher-label");
			grid.add(label, col, row++);
			if (row == 3) {
				row = 0;
				col++;
			}
			//grid.setHalignment(label, HPos.CENTER);

		}
	}

	private void makeTimeTable() {
		GridPane grid = controller.getGridPane();

		//add day name and period number
		decoratewithPeriods(grid, nPeriodsADay);
		deocrateWithDays(grid, nDaysAWeek);

		//add the periods in a horizontal way
		int row = 1; // row 0 is occupied by period number
		for (Period p : periods) {
			Label l = new Label(p.getSubject().getName());
			l.getStyleClass().add("period-label");

			//style for labs
			if (p.getSubject().getConstraintModifiers().containsKey("lab")) l.getStyleClass().add("lab-period-label");

			//style for electives
			if (p.getSubject().getConstraintModifiers().containsKey("elective"))l.getStyleClass().add("elective-period-label");
			grid.addColumn(row, l);
			row = ++row == nPeriodsADay + 1 ? 1 : row;

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

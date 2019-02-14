package org.mfd.timetable5k.gui.timetablestage;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.mfd.timetable5k.core.*;
import org.mfd.timetable5k.core.tablerimplementations.backtracking.BackTrackConstraintCollection;
import org.mfd.timetable5k.core.tablerimplementations.backtracking.LinearSelectionAllocatorStrategy;
import org.mfd.timetable5k.core.tablerimplementations.backtracking.PeriodAllocatorImpl;
import org.mfd.timetable5k.core.tablerimplementations.backtracking.TimeTablerBacktrackImpl;
import org.mfd.timetable5k.gui.TimeTableDataModel;
import org.mfd.timetable5k.gui.Utils;
import org.mfd.timetable5k.gui.formatteddisplay.TeacherTimeTableDisplay;
import org.mfd.timetable5k.gui.formatteddisplay.TimeTableWindow;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TimeTableAllocationController {
	private TimeTableDataModel model;
	private TimeTabler         timeTabler;
	private TimeTable          tt;
	@FXML
	private HBox               contentArea;
	@FXML
	private AnchorPane         rootContainer;
	@FXML
	private Button             getTimetableButton;

	public void initModel(TimeTableDataModel model) {
		this.model = model;


		//window close callback
		Stage currstage = (Stage) rootContainer.getScene().getWindow();

		//set up the timetabler
		setUpTimetabler();

		currstage.setOnCloseRequest(windowEvent -> timeTabler.stopTimeTabling());

	}

	private void setUpTimetabler() {
		int periodsADay = model.getNumPeriodADay();
		int daysAWeek = model.getNumOfDay();
		Classroom[] classroom = model.getClassroomObservableList().toArray(new Classroom[0]);
		int[] labConstraints =
				Arrays.stream(model.getLabConstraintsObservableList().toArray(new Integer[0])).mapToInt(i -> i)
					  .toArray();

		//IMPORTANT: normalize on the COPY of CLASSROOMS to prevent breaking the teacher allocator
		CheckUtils.normalize(periodsADay, daysAWeek, classroom);
		ConstraintCollection cc = new BackTrackConstraintCollection();


		// wait for a time table for 4 sseconds, if none exists restart with a new one
		while (tt == null) {
			if (timeTabler != null) timeTabler.stopTimeTabling();
			timeTabler = new TimeTablerBacktrackImpl(periodsADay, daysAWeek, Utils.copyOf((classroom)), 3,
													 new LinearSelectionAllocatorStrategy(new PeriodAllocatorImpl()),
													 Utils.genericCopyOf((HashMap)model.getTeacherTT()),
													 cc.getTeacherNoConcurrentPeriodsConstraint(),
													 cc.getSubjectMaxHourConstraint(),
													 cc.getSubjectNoLabBeginConstraint(labConstraints),
													 cc.getTeacherConsecutiveLabConstraint(),
													 cc.getTeacherNoAllotConstraint(),
													 cc.getClassSpacingConstraint(3, 2),
													 cc.getSubjectConcurrentElectiveConstraint(),
													 cc.getSubjectNoConcurrentLabConstraint(),
													 cc.getTeacherMaxFirstHourConstraint(2),
													 cc.getTeacherTheroyLabLimitConstraint(),
													 cc.getTeacherMaxAfterLunchHourConstraint(4),
													 cc.getTeacherTheroyLabLimitConstraint(),
													 cc.getTeacherNoContinuousConstraint(),
													 cc.getClassMaxAfterLunch(2)
													 //cc.getTeacherMondayLunchMeetingConstraint()

			);

			tt = timeTabler.nextTimeTable();
		}


		displayResultAndEnableNextTTButton();
	}


	@FXML
	private void onClickGetTimeTable() {
		resetContentAreaAndGetTTButton();
		tt = timeTabler.nextTimeTable();
		displayResultAndEnableNextTTButton();
	}

	private void displayResultAndEnableNextTTButton() {
		getTimetableButton.setDisable(false);

		for (int i = 0; i < timeTabler.getnClass(); i++) {


			// list view of all periods in the timetable
			ListView<Period> classTimeTable =
					new ListView<>(FXCollections.observableList(Arrays.asList(tt.getTimeTableFor(i).allperiods())));
			classTimeTable.setTooltip(new Tooltip("class " + i));
			// button to get the formatted timetable
			Button formattedTableButton = getButtonFor(Arrays.asList(tt.getTimeTableFor(i).allperiods()), i);


			// display the listview and the button in a column wise fashion
			contentArea.getChildren().add(new VBox(classTimeTable, formattedTableButton));

		}

		// add a button to display the teacher
		TableView<Teacher> teacherTT = makeTimeTableForTeacher();
		contentArea.getChildren().add(teacherTT);
	}

	private TableView<Teacher> makeTimeTableForTeacher() {
		TableColumn<Teacher, String> nameCol = new TableColumn<>();
		TableColumn<Teacher, Button> butCol = new TableColumn<>();

		// create the table with columns
		TableView<Teacher> teacherTimeTable = new TableView<>();
		teacherTimeTable.getColumns().add(nameCol);
		teacherTimeTable.getColumns().add(butCol);

		nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
		butCol.setCellValueFactory(p -> new SimpleObjectProperty<>(getTeacherTimeTableButtonFor(p.getValue())));

		// populate the table with the teachers
		teacherTimeTable.setItems(FXCollections.observableArrayList(tt.getTeacherAllotedPeriodMap().keySet()));

		return teacherTimeTable;

	}

	private Button getTeacherTimeTableButtonFor(Teacher value) {
		final Button getTimetable = new Button("Get timetable");
		getTimetable.setOnAction(e -> new TeacherTimeTableDisplay(value, timeTabler.getnDays(), timeTabler.getnPeriodADay()).show());
		return getTimetable;
	}

	private void resetContentAreaAndGetTTButton() {
		//clear content area for next message or timetable
		contentArea.getChildren().clear();

		//non blocking call, for now wait ofr 2 seconds and disable the button to notify processing
		getTimetableButton.setDisable(true);

	}

	private Button getButtonFor(List<Period> periods,
			int i) {
		Button button = new Button("Get tt for class : " + i);
		button.setOnAction(event ->
								   new TimeTableWindow(
										   periods, model.getClassroomObservableList().get(i), model.getNumOfDay(),
										   model.getNumPeriodADay()).show());
		return button;
	}

	@FXML
	private void onClickShuffle() {
		//basically reset the timetabler and start a fresh
		timeTabler.stopTimeTabling();

		resetContentAreaAndGetTTButton();


		tt = null;
		setUpTimetabler();

		doneDialog();

	}

	private void doneDialog() {
		Dialog d = new Dialog();
		d.setContentText("Done");
		Window window = d.getDialogPane().getScene().getWindow();
		window.setOnCloseRequest(event -> window.hide());
		d.show();
	}

	@FXML
	private void onClickSaveTeacherTimeTable(ActionEvent actionEvent) throws IOException {
		FileChooser chooser = new FileChooser();
		File out = chooser.showSaveDialog(null);

		if (out != null) {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(out));
			oos.writeObject(tt.getTeacherAllotedPeriodMap());
			oos.close();
		}
	}
}

package org.mfd.timetable5k.gui.configstage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mfd.timetable5k.core.Teacher;
import org.mfd.timetable5k.gui.FileBasedDao;
import org.mfd.timetable5k.gui.TimeTableDataModel;
import org.mfd.timetable5k.gui.TimeTableDataModelDao;
import org.mfd.timetable5k.gui.timetablestage.TimeTableAllocationController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

public class BeginStageController {

	private TimeTableDataModel       model;
	@FXML
	private SubjectLabController     subjectLabController;
	@FXML
	private DayConfigController      dayConfigController;
	@FXML
	private TeacherController        teacherController;
	@FXML
	private ClassroomController      classroomController;
	@FXML
	private TeacherChooserController teacherChooserController;

	public BeginStageController() {
	}

	@FXML
	private void onClickBegin() throws IOException {
		//create a new stage and set the teacher layout
		Stage tablerStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(
				"/org/mfd/timetable5k/layouts/timetablestage/timetable_allocation.fxml"));
		tablerStage.setScene(new Scene(loader.load()));
		TimeTableAllocationController ttac = loader.getController();
		ttac.initModel(model);
		tablerStage.show();
	}

	public void initModel(TimeTableDataModel model) {
		this.model = model;
		subjectLabController.initModel(model);
		dayConfigController.initModel(model);
		teacherController.initModel(model);
		classroomController.initModel(model);
		teacherChooserController.initModel(model);
	}


	@FXML
	private void onClickLoad() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose File");
		File chosen = fileChooser.showOpenDialog(null);

		if (chosen != null) {
			TimeTableDataModelDao fileDao = new FileBasedDao(chosen);
			try {
				fileDao.loadDataModelInto(model);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void onClickSave() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save");
		File chosen = fileChooser.showSaveDialog(null);

		if (chosen != null) {
			TimeTableDataModelDao fileDao = new FileBasedDao(chosen);
			try {
				fileDao.writeDataModel(model);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void onClickLoadTeacherTimeTable(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
		File teachertt = new FileChooser().showOpenDialog(null);

		if (teachertt != null) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(teachertt));
			Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> m=((Map<Teacher, Map<Integer, Teacher.AllotedPeriod>>) ois.readObject());

			/* The mapping above is actually Map<Teacher, UnmodifiableMap<...>>  bcoz of Teacher.getAllotedPeriods..() .
			 This causes unsupported modification exceptions. So sanitizee it before setting it in model*/

			Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> clean = new HashMap<>();
			m.forEach((k,v) -> clean.put(k,new HashMap<>(v)));

			model.setTeacherTT(clean);
		}
	}
}

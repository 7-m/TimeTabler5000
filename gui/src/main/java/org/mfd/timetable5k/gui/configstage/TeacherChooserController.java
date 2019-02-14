package org.mfd.timetable5k.gui.configstage;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.mfd.timetable5k.core.Classroom;
import org.mfd.timetable5k.core.Subject;
import org.mfd.timetable5k.core.Teacher;
import org.mfd.timetable5k.gui.TimeTableDataModel;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherChooserController {
	private TimeTableDataModel  model;
	@FXML
	private ListView<Teacher>   availableTeacherList;
	@FXML
	private ListView<Classroom> classList;
	@FXML
	private ListView<Subject>   subjectList;
	@FXML
	private Label               selectedTeacher;


	void initModel(TimeTableDataModel model) {
		this.model = model;

		classList.setItems(model.getClassroomObservableList());

		//set up listeners for subject list

		classList.getFocusModel().focusedItemProperty().addListener((observable, oldValue, newValue)
				-> {
			if (newValue != null) subjectList.setItems(FXCollections.observableList(newValue.getSubjectsToTeach()));
		});




		//set up listener for available teachers

		subjectList.getFocusModel().focusedItemProperty().addListener((observable, oldValue, newValue)
				-> {
			if (newValue != null) {
				availableTeacherList.setItems(FXCollections.observableList(newValue.getTeachers()));
				List<Teacher> teachers = classList.getFocusModel().getFocusedItem().getTeacher(newValue);
				if (!teachers.isEmpty())
				selectedTeacher.setText(teachers.stream().map(Teacher::getName).collect(Collectors.joining(",")));
			}
		});


	}

	@FXML
	private void onClickSelectTeacher() {
		//assgin the selected available teacher to the selected subject

		//get all required info

		Classroom c = classList.getFocusModel().getFocusedItem();
		Subject s = subjectList.getFocusModel().getFocusedItem();
		Teacher t = availableTeacherList.getFocusModel().getFocusedItem();

		if (c != null && s != null && t != null) {
			c.assignTeacher(s, t);


		}


	}

	@FXML
	private void onClickDeleteTeachers() {
		Classroom c = classList.getFocusModel().getFocusedItem();
		Subject s = subjectList.getFocusModel().getFocusedItem();


		if (c != null && s != null ) {
			c.removeSubjectTeacher(s);


		}
	}
}

package org.mfd.timetable5k.gui.configstage;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.mfd.timetable5k.core.Classroom;
import org.mfd.timetable5k.core.Subject;
import org.mfd.timetable5k.gui.TimeTableDataModel;
import org.mfd.timetable5k.gui.Utils;

import java.util.ArrayList;

public class ClassroomController {
	private TimeTableDataModel model;

	@FXML
	private TableView<Classroom> classroomTable;
	@FXML
	private TableView<Subject> wantedSubjectsTable;

	@FXML
	private TextField nameField;
	@FXML
	private TableView<Subject> availableSubjectTable;

	@FXML
	private void clickAddClassroomButton() {
		if (!Utils.isEmptyTextField(nameField))
			model.getClassroomObservableList().add(new Classroom(nameField.getText(), new ArrayList<>()));

	}

	@FXML
	private void clickDeleteClassroomButton() {
		int focusedIndex = classroomTable.getSelectionModel().getFocusedIndex();

		if (focusedIndex >= 0) classroomTable.getItems().remove(focusedIndex);
	}

	@FXML
	private void clickAddSubjectButton() {
		int subjectIndex = availableSubjectTable.getSelectionModel().getFocusedIndex();
		int classroomIndex = classroomTable.getSelectionModel().getFocusedIndex();

		if (subjectIndex >= 0 && classroomIndex >= 0) {
			Subject subject = model.getSubjectObservableList().get(subjectIndex);

			// wantedSubjects wraps the currently selected Classroom subject list
			wantedSubjectsTable.getItems().add(subject);

		}

	}
	@FXML
	private  void onClickAddAll(){
		wantedSubjectsTable.getItems().addAll(model.getSubjectObservableList());
	}


	@FXML
	private void clickDeleteSubjectButton() {
		int subjectIndex = wantedSubjectsTable.getSelectionModel().getFocusedIndex();
		int classroomIndex = classroomTable.getSelectionModel().getFocusedIndex();

		if (subjectIndex >= 0 && classroomIndex >= 0) {

			// wantedSubjects wraps the appropriate Classroom subject list
			wantedSubjectsTable.getItems().remove(subjectIndex);

		}

	}
	@SuppressWarnings("unchecked")
	void initModel(TimeTableDataModel model) {
		this.model = model;

		classroomTable.setItems(model.getClassroomObservableList());

		availableSubjectTable.setItems(model.getSubjectObservableList());

		TableColumn<Classroom, String> classColumn = (TableColumn<Classroom, String>) classroomTable.getColumns().get(0);
		classColumn.setCellValueFactory((cdf) -> new ReadOnlyStringWrapper(cdf.getValue().getName()));

		//set up available Subjects cellvalue factory

		TableColumn<Subject, String> availableSubjectColumn = (TableColumn<Subject, String>) availableSubjectTable.getColumns().get(0);
		availableSubjectColumn.setCellValueFactory(cdf -> new ReadOnlyStringWrapper(cdf.getValue().getName()));

		//set up wantedSubjectsTable
		TableColumn<Subject, String> wantedSubjectColumn = (TableColumn<Subject, String>) wantedSubjectsTable.getColumns().get(0);
		wantedSubjectColumn.setCellValueFactory(cdf -> new ReadOnlyStringWrapper(cdf.getValue().getName()));

		//add a listener to the focus event property for the wanted subjects table

		classroomTable.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) ->
		{
			if (newValue.intValue() >= 0) {
				ObservableList<Subject> displayedList = FXCollections.observableArrayList(
						model.getClassroomObservableList().get(newValue.intValue()).getSubjectsToTeach());
				displayedList.addListener((ListChangeListener<? super Subject>) c -> {
					while(c.next()){
						if(c.wasAdded())
							for (Subject added:c.getAddedSubList() )
								model.getClassroomObservableList().get(newValue.intValue()).addSubjectToTeach(added);

						if (c.wasRemoved())	{
							for (Subject removed:c.getRemoved() )
								model.getClassroomObservableList().get(newValue.intValue()).removeSubjectToTeach(removed);
						}
					}
				}); // use the change listener to call the Subject#addTeacher method to prevent breaking encapsulation
				wantedSubjectsTable.setItems(displayedList);
			}
		});


	}


}

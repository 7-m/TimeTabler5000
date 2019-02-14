package org.mfd.timetable5k.gui.configstage;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.mfd.timetable5k.core.Subject;
import org.mfd.timetable5k.core.Teacher;
import org.mfd.timetable5k.gui.TimeTableDataModel;
import org.mfd.timetable5k.gui.Utils;


public class SubjectLabController {


	private TimeTableDataModel model;
	@FXML
	private TextField          subjectField;

	@FXML
	private TextField          hoursField;
	@FXML
	private TableView<Subject> subjectTable;

	@FXML
	private TableView<Teacher> availableTeacherTable;
	@FXML
	private TableView<Teacher> selectedTeacherTable;

	@FXML
	private       TextField subjectConstraintsKeyField;
	@FXML
	private       TextField subjectConstraintsValueField;
	@FXML private VBox      contraintContentBox;

	public SubjectLabController() {
	}

	@FXML
	void subjectAddButtonClick() {
		//retrieve non empty values from the text fields for subject type and hour to create
		// a subject and add it to the observables

		//silently dont accept value if any fields are empty

		if (Utils.isEmptyTextField(subjectField, hoursField)) return;


		//parse the content, if invalid silently ignore
		String sname = subjectField.getText();
		int hours;
		try {
			hours = Integer.parseInt(hoursField.getText());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return; // dont go ahead
		}

		//create a new subject object
		Subject subject = new Subject(sname, hours);
		model.getSubjectObservableList().add(subject);


	}


	@FXML
	void subjectDeleteButtonClick() {

		int sel = subjectTable.getSelectionModel().getFocusedIndex();

		if (sel >= 0)
			model.getSubjectObservableList().remove(sel);


	}


	@FXML
	private void clickAddTeacherButton() {
		//add teacher for the specific subject
		int subIndex = subjectTable.getSelectionModel().getFocusedIndex();
		int teacherIndex = availableTeacherTable.getSelectionModel().getFocusedIndex();

		if (subIndex >= 0 && teacherIndex >= 0)
			selectedTeacherTable.getItems().add(model.getTeacherObservableList().get(teacherIndex));
	}

	@SuppressWarnings("unchecked")
	void initModel(TimeTableDataModel model) {
		this.model = model;

		//init table views object
		subjectTable.setItems(model.getSubjectObservableList());

		availableTeacherTable.setItems(model.getTeacherObservableList());

		//setup the subject table view to render values
		setupSubjectTable();

		//setup availableTeacherTable
		TableColumn<Teacher, String> nameColumn =
				(TableColumn<Teacher, String>) availableTeacherTable.getColumns().get(0);
		nameColumn.setCellValueFactory((cdf) -> new ReadOnlyStringWrapper(cdf.getValue().getName()));

		//setup selected teachers table
		TableColumn<Teacher, String> selectedTeacherNameColumn =
				(TableColumn<Teacher, String>) selectedTeacherTable.getColumns().get(0);
		selectedTeacherNameColumn.setCellValueFactory((cdf) -> new ReadOnlyStringWrapper(cdf.getValue().getName()));

		subjectTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
			if (newValue != null) { //valid index check

				ObservableList<Teacher> displayedList = FXCollections.observableArrayList(newValue.getTeachers());
				displayedList.addListener((ListChangeListener<? super Teacher>) c -> {
					while (c.next()) {
						if (c.wasAdded())
							for (Teacher added : c.getAddedSubList())
								newValue.addTeacher(added);

						if (c.wasRemoved()) {
							for (Teacher removed : c.getRemoved())
								newValue.removeTeacher(removed);
						}
					}
				}); // use the change listener to call the Subject#addTeacher method to prevent breaking encapsulation
				selectedTeacherTable.setItems(displayedList);

				//display the constraints in the constraintdisplay box
				contraintContentBox.getChildren().clear();
				newValue.getConstraintModifiers().entrySet().stream().forEach(entry -> {
					contraintContentBox.getChildren().add(new Label(entry.getKey() + " : " + entry.getValue()));
				});

			}
		});


	}

	private void setupSubjectTable() {
		TableColumn<Subject, String> subjectColumn = (TableColumn<Subject, String>) subjectTable.getColumns().get(0);
		TableColumn<Subject, String> typeColumn = (TableColumn<Subject, String>) subjectTable.getColumns().get(1);
		TableColumn<Subject, Number> hoursColumn = (TableColumn<Subject, Number>) subjectTable.getColumns().get(2);

		subjectColumn.setCellValueFactory((cdf) -> new ReadOnlyStringWrapper(cdf.getValue().getName()));
		typeColumn.setCellValueFactory(
				(cdf) -> new ReadOnlyStringWrapper(String.join(",", cdf.getValue().getConstraintModifiers().keySet())));
		hoursColumn.setCellValueFactory((cdf) -> new ReadOnlyIntegerWrapper(cdf.getValue().getRequiredHours()));
	}

	@FXML
	private void clickDeleteTeacherButton() {
		int subIndex = subjectTable.getSelectionModel().getFocusedIndex();
		int teacherIndex = selectedTeacherTable.getSelectionModel().getFocusedIndex();

		if (subIndex >= 0 && teacherIndex >= 0)
			selectedTeacherTable.getItems().remove(teacherIndex);
	}

	@FXML
	private void onClickAddSubjectConstraint() {
		Subject selectedItem = subjectTable.getSelectionModel().getSelectedItem();
		if (selectedItem != null && !Utils.isEmptyTextField(subjectConstraintsKeyField))
			selectedItem.getConstraintModifiers().put(subjectConstraintsKeyField.getText(), subjectConstraintsValueField.getText());

	}

	@FXML
	private void onClickDeleteSubjectConstraint() {
		Subject selectedItem = subjectTable.getSelectionModel().getSelectedItem();

		if (selectedItem != null)
			selectedItem.getConstraintModifiers().clear();


	}
}

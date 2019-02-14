package org.mfd.timetable5k.gui.configstage;


import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.mfd.timetable5k.core.Teacher;
import org.mfd.timetable5k.gui.TimeTableDataModel;
import org.mfd.timetable5k.gui.Utils;

import java.util.Collections;
import java.util.List;

public class TeacherController {

	private       TimeTableDataModel model;
	@FXML
	private       TableView<Teacher> teacherTable;
	@FXML
	private       TextField          nameField;
	@FXML
	private       TextField          wuField;
	@FXML private GridPane           restrictionsGrid;

	@FXML
	private void clickAddTeacherButton() {
		/* todo support for unique teachers i.e no repetitions, 2 approaches either edit this method to check for dupl
		icates or extend and override  the add method in the observable list class. There is no direct way as the TAbl
		eColum#setItems only supports ObservableLists */
		if (Utils.isEmptyTextField(nameField, wuField))
			return;

		//get info and and create a teacher

		String name = nameField.getText();
		int wu = Integer.valueOf(wuField.getText());
		model.getTeacherObservableList().add(new Teacher(name, wu, Collections.emptyList()));


	}


	@FXML
	private void clickDeleteTeacherButton() {
		int focusedIndex = teacherTable.getSelectionModel().getFocusedIndex();

		if (focusedIndex >= 0) model.getTeacherObservableList().remove(focusedIndex);
	}


	@SuppressWarnings("unchecked")
	void initModel(TimeTableDataModel model) {
		this.model = model;


		teacherTable.setItems(model.getTeacherObservableList());
		TableColumn<Teacher, String> nameColumn = (TableColumn<Teacher, String>) teacherTable.getColumns().get(0);
		TableColumn<Teacher, Number> wuColumn = (TableColumn<Teacher, Number>) teacherTable.getColumns().get(1);

		//setup cellValueFactories for teacherTable
		nameColumn.setCellValueFactory((cdf) -> new ReadOnlyStringWrapper(cdf.getValue().getName()));
		wuColumn.setCellValueFactory((cdf) -> new ReadOnlyIntegerWrapper(cdf.getValue().getMaxWorkUnits()));


		teacherTable.getFocusModel().focusedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				createRestrictionsGridFor(newValue.getRestrictions());
				System.out.println(newValue.getName()+" : "+newValue.getRestrictions());

			}
		});


	}

	/**
	 The method initializes restrictionsGrid with radio buttons for each period restriction. if the radio button is dotted, then a period
	 cannot be alloted to the period represented by the radio button. The changes are reflected immediately and not after a new teacher is
	 selected.
	 */
	private void createRestrictionsGridFor(List<Integer> restrictions) {
		for (int i = 0; i < model.getNumOfDay(); i++) {
			for (int j = 0; j < model.getNumPeriodADay(); j++) {
				RadioButton rb = new RadioButton();

				int finalI = i;
				int finalJ = j;

				// calculate the correct absolute period no.
				final Integer periodNo = (finalI * model.getNumPeriodADay()) + finalJ;

				rb.selectedProperty().addListener((observable, oldValue, newValue) -> {
					if (newValue != null) {
						if (newValue.booleanValue() == true) {

							if (!restrictions.contains(periodNo))
							restrictions.add(periodNo);
						}
						else restrictions.remove(periodNo);
					}

				});

				// dot radiobutton if already present
				if (restrictions.contains(periodNo))rb.selectedProperty().setValue(true);

				restrictionsGrid.add(rb, j + 1, i+ 1);
			}
		}
	}


}
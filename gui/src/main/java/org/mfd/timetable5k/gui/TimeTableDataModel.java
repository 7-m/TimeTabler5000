package org.mfd.timetable5k.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.mfd.timetable5k.core.Classroom;
import org.mfd.timetable5k.core.Subject;
import org.mfd.timetable5k.core.Teacher;

import java.util.Map;

public class TimeTableDataModel {

	private Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> teacherTT;
	private int                       numPeriodADay;
	private int                       numOfDay;
	private ObservableList<Classroom> classroomObservableList      = FXCollections.observableArrayList();
	private ObservableList<Integer>   labConstraintsObservableList = FXCollections.observableArrayList();
	private ObservableList<Subject>   subjectObservableList        = FXCollections.observableArrayList();
	private ObservableList<Teacher>   teacherObservableList        = FXCollections.observableArrayList();

	public Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> getTeacherTT() {
		return teacherTT;
	}

	public void setTeacherTT(Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> teacherTT) {
		this.teacherTT = teacherTT;
	}

	public ObservableList<Classroom> getClassroomObservableList() {
		return classroomObservableList;
	}

	public int getNumPeriodADay() {
		return numPeriodADay;
	}

	public void setNumPeriodADay(int numPeriodADay) {
		this.numPeriodADay = numPeriodADay;
	}

	public int getNumOfDay() {
		return numOfDay;
	}

	public void setNumOfDay(int numOfDay) {
		this.numOfDay = numOfDay;
	}

	public ObservableList<Integer> getLabConstraintsObservableList() {
		return labConstraintsObservableList;
	}

	public ObservableList<Subject> getSubjectObservableList() {
		return subjectObservableList;
	}

	public ObservableList<Teacher> getTeacherObservableList() {
		return teacherObservableList;
	}
}

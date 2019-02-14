package org.mfd.timetable5k.gui;

import org.mfd.timetable5k.core.Classroom;
import org.mfd.timetable5k.core.Subject;
import org.mfd.timetable5k.core.Teacher;
import org.mfd.timetable5k.gui.TimeTableDataModel;
import org.mfd.timetable5k.gui.TimeTableDataModelDao;

import java.io.*;

public class FileBasedDao implements TimeTableDataModelDao {
	private File path;

	public FileBasedDao(File path) {
		this.path = path;

	}

	@Override
	public void writeDataModel(TimeTableDataModel model) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
		/*
		write -teachers
			-subjects
			-classrooms
			-constraints
			-days , periods
			convert lists to arrays and write them as we don't know its concrete classes
		 */
		oos.writeObject(model.getTeacherObservableList().toArray(new Teacher[0]));
		oos.writeObject(model.getSubjectObservableList().toArray(new Subject[0]));
		oos.writeObject(model.getClassroomObservableList().toArray(new Classroom[0]));
		oos.writeObject(model.getLabConstraintsObservableList().toArray(new Integer[0]));
		oos.writeInt(model.getNumOfDay());
		oos.writeInt(model.getNumPeriodADay());

		oos.flush();
		oos.close();
	}

	@Override
	public TimeTableDataModel loadDataModelInto(TimeTableDataModel model) throws IOException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
		resetModel(model);
		try {
			model.getTeacherObservableList().addAll((Teacher[]) ois.readObject());
			model.getSubjectObservableList().addAll((Subject[]) ois.readObject());
			model.getClassroomObservableList().addAll((Classroom[]) ois.readObject());
			model.getLabConstraintsObservableList().addAll((Integer[]) ois.readObject());
			model.setNumOfDay(ois.readInt());
			model.setNumPeriodADay(ois.readInt());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		ois.close();
		return model;
	}

	private void resetModel(TimeTableDataModel model) {
		model.getTeacherObservableList().clear();
		model.getSubjectObservableList().clear();
		model.getClassroomObservableList().clear();
		model.getLabConstraintsObservableList().clear();
		model.setNumPeriodADay(0);
		model.setNumOfDay(0);
	}
}

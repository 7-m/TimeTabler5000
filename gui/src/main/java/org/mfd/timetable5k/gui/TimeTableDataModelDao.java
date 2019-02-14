package org.mfd.timetable5k.gui;


import org.mfd.timetable5k.gui.TimeTableDataModel;

import java.io.IOException;

public interface TimeTableDataModelDao {
	void writeDataModel(TimeTableDataModel model)throws IOException;
	TimeTableDataModel loadDataModelInto(TimeTableDataModel model)throws  IOException;


}

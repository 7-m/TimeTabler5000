package org.mfd.timetable5k.core;

import java.util.Collections;

public class CheckUtils {
	//prepares for time tabler by padding with free period subject and teachers
	static public void normalize(int periodsADay, int noDaysAWeek, Classroom[] classrooms) {
		//for every class, find out its required period and compare with total availble periods
		//and pad it with free periods accordingly

		int totalPeriods = periodsADay * noDaysAWeek;

		for (Classroom c : classrooms) {
			int reqdPeriods = 0;
			for (Subject s : c.getSubjectsToTeach()) {
				reqdPeriods += s.getRequiredHours();
			}
			int freePeriods = totalPeriods - reqdPeriods;

			//for every free period required create a new free subject and a teacher for it

			for (int i = 0; i < freePeriods; i++) {
				String randstring = String.valueOf(Math.random());
				Subject f = new Subject("FREE" + randstring, 1);
				Teacher t = new Teacher("FREETEACHER" + randstring, 1, Collections.emptyList());

				f.addTeacher(t);
				//*******IMPORTANT : comment the following line if you plan on normalizing BEFORE assigning teachers
				c.assignTeacher(f,t);
			}
		}


	}
}

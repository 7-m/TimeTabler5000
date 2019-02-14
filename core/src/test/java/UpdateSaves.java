/*
import org.junit.jupiter.api.Test;
import org.mfd.timetable5k.core.Classroom;
import org.mfd.timetable5k.core.Subject;
import org.mfd.timetable5k.core.Teacher;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class UpdateSaves {
	@Test
	void updateSave() throws Exception {
		final String path = "/home/mfd/development/idea-workspace/TimeTabler5000/tbd-data/sem-3-4-5-ONLY-7.tbd";
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(
				path)));

		Teacher[] teachers = (Teacher[]) ois.readObject();
		Subject[] subjects = (Subject[]) ois.readObject();
		Classroom[] classrooms = (Classroom[]) ois.readObject();
		int[] labConstraints = Arrays.stream((Integer[]) ois.readObject()).mapToInt(i -> i).toArray();
		int daysAWeek = ois.readInt();
		int periodsADays = ois.readInt();

		// set the allocations
		Arrays.stream(teachers).forEach(t->t.setAllocatedPeriodsMap(new HashMap<>()));



		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path+"-ver2"));
		*/
/*
		write -teachers
			-subjects
			-classrooms
			-constraints
			-days , periods
			convert lists to arrays and write them as we don't know its concrete classes
		 *//*

		oos.writeObject(teachers);
		oos.writeObject(subjects);
		oos.writeObject(classrooms);
		oos.writeObject(Arrays.stream(labConstraints).mapToObj(i->new Integer(i)).toArray(Integer[]::new));
		oos.writeInt(daysAWeek);
		oos.writeInt(periodsADays);

		oos.flush();
		oos.close();
	}
}
*/

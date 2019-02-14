package org.mfd.timetable5k.core.tablerimplementations.backtracking;

import org.mfd.timetable5k.core.*;

import java.util.List;

/**
 Simple implementation for a timetable with just regular subjects and labs
 */
public class PeriodAllocatorImpl
		implements PeriodAllocator {
	@Override
	public void allocate(Subject s, List<Teacher> t, TimeTablerBacktrackImpl context, int i, int j) {
		Classroom classroom = context.getClassroom(j);
		if (s.getConstraintModifiers().containsKey("lab")) {
			context.assignPeriod(new Period(t, s), i, j);
			context.assignPeriod(new Period(t, s), i + 1, j);
			classroom.incHoursFor(s);
			classroom.incHoursFor(s);

			// update the teachers only for i+1 (i is updated for all conditions at the end)
			t.stream().forEach(teacher -> teacher.addAllocatedPeriod(i + 1, classroom.getName(), s.getName()));

		} else if (s.getConstraintModifiers().containsKey("elective")) {
			//assign all those peiods

			// first determine the classes with the same subject
			for (int k = 0; k < context.getnClass(); k++) {
				//check if the simultaneous period can be alloted
				if (context.getClassroom(k).getSubjectsToTeach().contains(s)) {
					context.assignPeriod(new Period(t, s), i, k);
					context.getClassroom(k).incHoursFor(s);
				}
			}
		} else {
			context.assignPeriod(new Period(t, s), i, j);
			classroom.incHoursFor(s);
		}

		// update the teachers for period i, commonly for all conditions
		t.stream().forEach(teacher -> teacher.addAllocatedPeriod(i, classroom.getName(), s.getName()));

	}

	@Override
	public void deallocate(TimeTablerBacktrackImpl context, int i, int j) {
		Classroom classroom = context.getClassroom(j);
		Subject subject = context.getPeriodAt(i, j).getSubject();
		List<Teacher> t = context.getPeriodAt(i, j).getTeachers();

		if (subject.getConstraintModifiers().containsKey("lab")) {
			classroom.decHoursFor(subject);
			classroom.decHoursFor(subject);
			context.assignPeriod(null, i, j);
			context.assignPeriod(null, i + 1, j);

			// update the teachers
			// remove only for i+1, i is removed at end
			t.stream().forEach(teacher -> teacher.removeAllocatedPeriod(i + 1));

		} else if (subject.getConstraintModifiers().containsKey("elective")) {
			//assign all those peiods

			// first determine the classes with the same subject
			for (int k = 0; k < context.getnClass(); k++) {
				//check if the simultaneous period can be alloted
				if (context.getClassroom(k).getSubjectsToTeach().contains(subject)) {
					context.assignPeriod(null, i, k);
					context.getClassroom(k).decHoursFor(subject);
				}
			}
		} else {
			classroom.decHoursFor(subject);
			context.assignPeriod(null, i, j);
		}
		// update the teachers
		t.stream().forEach(teacher -> teacher.removeAllocatedPeriod(i));

	}

}

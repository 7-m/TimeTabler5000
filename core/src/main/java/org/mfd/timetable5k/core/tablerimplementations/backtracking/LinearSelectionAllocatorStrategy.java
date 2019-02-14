package org.mfd.timetable5k.core.tablerimplementations.backtracking;


import org.mfd.timetable5k.core.PeriodAllocator;
import org.mfd.timetable5k.core.Subject;
import org.mfd.timetable5k.core.Teacher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class LinearSelectionAllocatorStrategy
		extends Allocator {

	PeriodAllocator allocator;

	/* to keep track of the subjects the algorithm has already tried */
	Iterator<Subject>[][] subjectIterators;


	public LinearSelectionAllocatorStrategy(PeriodAllocator allocator) {
		this.allocator = allocator;
	}

	@Override
	protected void init(TimeTablerBacktrackImpl timeTable) {
		super.init(timeTable);
		subjectIterators = new Iterator[timeTable.getnPeriods()][timeTable.getnClass()];

	}

	@Override
	protected boolean allocate(int i, int j) {

		Iterator<Subject> iter = subjectIterators[i][j];
		for (Subject s = iter.next(); iter.hasNext(); s = iter.next()) {
			List<Teacher> t = getTimeTabler().getClassrooms()[j].getTeacher(s);
			// if all constraints are satisfied by the subject and teacher for the given class(hours) and period(restrictions).
			Subject finalS = s;
			if (getTimeTabler().getConstraints().stream().allMatch(constraint -> constraint.satisfies(i, j, getTimeTabler(), finalS, t))) {

				getTimeTabler().getConstraints().forEach(c -> c.allocate(i, j, getTimeTabler(), finalS, t));

				/* follow constraints contract and call its allocate() */

				allocator.allocate(s, t, getTimeTabler(), i, j);
				return true;

			}
		}
		//reset the iterator at i,j
		subjectIterators[i][j] = null;
		return false; // no candidates found
	}

	@Override
	protected void deallocate(int i, int j) {
		/* follow constraints contract and call its deallocate() */
		getTimeTabler().getConstraints().forEach(c -> c.deallocate(i,
																   j,
																   getTimeTabler(),
																   getTimeTabler().getPeriodAt(i, j).getSubject(),
																   getTimeTabler().getPeriodAt(i, j).getTeachers()));
		allocator.deallocate(getTimeTabler(), i, j);

	}

	@Override
	protected boolean hasCandidateFor(int i, int j) {
		if (subjectIterators[i][j] == null) {
			ArrayList<Subject> subjects = new ArrayList<>(getTimeTabler().getClassrooms()[j].getSubjectsToTeach());
			Collections.shuffle(subjects);
			subjectIterators[i][j] = subjects.iterator();
		}

		return subjectIterators[i][j].hasNext();
	}
}

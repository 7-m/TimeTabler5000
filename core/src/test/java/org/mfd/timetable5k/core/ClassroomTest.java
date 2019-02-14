package org.mfd.timetable5k.core;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ClassroomTest {
	@Test
	public void hasTeacherAndAssignAndRemoveMethod() {
		Subject subject1 = new Subject("15cs31", 10);
		Subject subject2 = new Subject("15cs32", 10);
		List<Subject> subjects = Arrays.asList(subject1, subject2);

		Teacher ajoy = new Teacher("ajoy", 10, Collections.emptyList());
		Teacher nota = new Teacher("not ajoy", 10, Collections.emptyList());
		subject1.addTeacher(ajoy);
		subject2.addTeacher(nota);

		Classroom c = new Classroom("c", subjects);

		assertFalse(c.hasTeacherFor(subject1));

		c.assignTeacher(subject1, ajoy);
		assertTrue(c.hasTeacherFor(subject1));

		assertThrows(IllegalStateException.class, () -> c.assignTeacher(subject1, ajoy));

		c.removeSubjectTeacher(subject1);
		assertFalse(c.hasTeacherFor(subject1));

	}

}

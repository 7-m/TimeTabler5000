package org.mfd.timetable5k.core;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TeacherTest {

	@Test
	public void teacherDecrementException() {
		int maxWorkUnits = 10;
		List<Integer> restrictions = Arrays.asList(7, 4, 0, 1);
		Teacher t = new Teacher("t",maxWorkUnits, restrictions);

		assertThrows(IllegalStateException.class, () -> t.incWorkBy(11));

	}

	@Test
	public void teacherIncrementException() {
		Teacher t = new Teacher("t",0, Collections.emptyList());
		assertThrows(IllegalStateException.class, ()->t.decWorkBy(1));
	}

}

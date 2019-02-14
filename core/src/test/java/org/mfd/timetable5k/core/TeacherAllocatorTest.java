/*
package org.mfd.timetable5k.core;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class TeacherAllocatorTest {

	@Test

	public void assignTeacherMethod() {
		Teacher t0 = new Teacher("t0", 10, Collections.emptyList());
		Teacher t1 = new Teacher("t1", 7, Collections.emptyList());
		Teacher t2 = new Teacher("t2", 7, Collections.emptyList());
		Teacher t3 = new Teacher("t3", 7, Collections.emptyList());
		Teacher t4 = new Teacher("t4", 6, Collections.emptyList());
		Teacher t5 = new Teacher("t5", 6, Collections.emptyList());

		Subject s0 = new Subject("s0", 4, false);
		Subject s1 = new Subject("s1", 3, false);
		Subject s2 = new Subject("s2", 3, false);
		Subject s3 = new Subject("s3", 2, false);
		Subject s4 = new Subject("s4", 2, false);

		s0.addTeacher(t0, t1);
		s1.addTeacher(t2, t3);
		s2.addTeacher(t0, t3);
		s3.addTeacher(t0, t4);
		s4.addTeacher(t1, t5);

		Classroom c0 = new Classroom("c0", Arrays.asList(s0, s1, s2));
		Classroom c1 = new Classroom("c1", Arrays.asList(s2, s3, s4));
		Classroom c2 = new Classroom("c2", Arrays.asList(s0, s2, s4));
		Classroom c3 = new Classroom("c3", Arrays.asList(s1, s3, s4));

		TeacherAllocator tt=new TeacherAllocator(new Classroom[]{c0, c1, c2, c3});
		*/
/*TimeTablerReferenceImpl tt = new TimeTablerReferenceImpl(0, 0, new Classroom[]{c0, c1, c2, c3}, null);*//*

		tt.getAllocation(Long.MAX_VALUE, TimeUnit.DAYS); // wait till result

	}
}
*/

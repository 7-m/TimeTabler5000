package org.mfd.timetable5k.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SubjectTest {
	@Test
	public void equalsMethod() {
		Subject s1 = new Subject("geography", 10);
		Subject s2 = new Subject("gEOGRAPHY", 101);
		assertTrue(s1.equals(s2));

		s1 = new Subject("15cs32", 12);
		s2 = new Subject("15CS32 ", 12);// with spaces
		assertTrue(s1.equals(s2));

	}

}

package org.mfd.timetable5k.core;

import java.util.*;

public class Utils {


	public static Integer[] parseNoLabs(String lab) {
		lab=lab.trim();
		return Arrays.stream(lab.split("\\s*,\\s*")).map(Integer::valueOf).toArray(Integer[]::new);
	}
}

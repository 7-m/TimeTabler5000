package org.mfd.timetable5k.core.tablerimplementations.backtracking;

import java.io.*;

public class Utils {
	public static <T extends Serializable> T genericCopyOf(T obj) throws IOException, ClassNotFoundException {
		T copy;
		//for a clean copy, we'll serialize and deserialize it
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(obj);
		oos.flush();
		oos.close();

		// read it
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
		ois.close();
		copy = (T) ois.readObject();

		return copy;
	}
}

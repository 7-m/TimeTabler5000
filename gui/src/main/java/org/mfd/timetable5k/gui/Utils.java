package org.mfd.timetable5k.gui;

import javafx.scene.control.TextField;
import org.mfd.timetable5k.core.Classroom;

import java.io.*;

public class Utils {

	public static boolean isEmptyTextField(TextField... tfs) {
		for (TextField tf : tfs)
			if (tf.getText().isEmpty())
				return true;

		return false;
	}

	public static Classroom[] copyOf(Classroom[] classes) {
		Classroom[] copy = null;
		//for a clean copy, well serialize and deserialize it
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(classes);
			oos.flush();
			oos.close();

			// read it
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
			ois.close();
			copy = (Classroom[]) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return copy;
	}

	public static <T extends Serializable> T genericCopyOf(T obj) {
		try {
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
		} catch (IOException | ClassNotFoundException eZ) {
			throw new RuntimeException("Couldnt make copy of obejct");
		}
	}

	/*public static TimeTableDataModel copyOf(TimeTableDataModel orig){

		TimeTableDataModel copy=new TimeTableDataModel();

		//deepcopy the original, preserving all links

	}*/

	public static String camelCaseCopy(String str) {
		StringBuffer sb = new StringBuffer();

		//first letter  to upper case
		sb.append(Character.toUpperCase(str.charAt(0)));
		for (int i = 1; i < str.length(); i++) {
			char c = str.charAt(i);

			if (!Character.isLetter(c) && i + 1 < str.length() && Character.isLetter(str.charAt(i + 1)))
				sb.append(c).append(Character.toUpperCase(str.charAt(++i)));
			else
				sb.append(c);
		}
		return sb.toString();
	}



}

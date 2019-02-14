package org.mfd.timetable5k;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mfd.timetable5k.gui.configstage.BeginStageController;
import org.mfd.timetable5k.gui.TimeTableDataModel;

import java.net.URL;

public class TimeTablerApp extends Application {
	public static void main(String... args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		URL resource = getClass().getResource(
				"/org/mfd/timetable5k/layouts/configstage/main_gui.fxml");
		System.out.print(resource);
		FXMLLoader loader = new FXMLLoader(resource);
		Scene root =new Scene( loader.load());
		BeginStageController controller=loader.getController();
		controller.initModel(new TimeTableDataModel());
		stage.setScene(root);
		stage.show();


	}
}

package masonfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MasonFXApplication extends Application {
	public static MasonFXController controller;
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("FX + Mason");
		MasonFXView root = new MasonFXView();
		
		controller = new MasonFXController(root);
		Scene scene = new Scene(root, 300, 200, Color.BLACK);
		stage.setScene(scene);
		stage.show();
	}

}

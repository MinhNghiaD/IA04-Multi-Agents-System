package masonfx;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MasonFXView extends BorderPane {
   HBox box = new HBox();
   Button start = new Button("Start");
   Button stop = new Button("Stop");
   TextField value1 = new TextField();
   TextField value2 = new TextField();

public MasonFXView() {
	super();
	box.getChildren().addAll(start,stop,value1,value2);
	setTop(box);
}
   
}

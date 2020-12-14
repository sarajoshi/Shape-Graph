package GroupProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Create3DShapes extends Application {
	private PerspectiveCamera pCamera;
	private SubScene shapesSub;
	private BorderPane borderPane = new BorderPane();
	private GridPane gridPane = new GridPane();
	private GridPane gridPane2 = new GridPane();
	private Group shapesGroup = new Group();

	private Slider slider;

	private Button addShape;
	private Button addSphere;
	private Button addBox;
	private Button addCylinder;
	private Button modifyShape;
	private Button rotator;

	ChoiceBox<String> colors;
	String x = "";

	private TextField xField = new TextField();
	private TextField yField = new TextField();
	private TextField radiusField = new TextField();
	private TextField lengthField = new TextField();
	private TextField widthField = new TextField();
	private TextField heightField = new TextField();

	private double xLocation;
	private double yLocation;
	private double length;
	private double width;
	private double height;
	private double radius;

	private int selectedShapeID;
	MenuBar menuBar = new MenuBar();

	int sphereCount = 0;
	int boxCount = 0;
	int cylinderCount = 0;
	ArrayList<SphereObject> spheres = new ArrayList<>();
	ArrayList<CylinderObject> cylinders = new ArrayList<>();
	ArrayList<BoxObject> boxes = new ArrayList<>();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Menu fileMenu = new Menu("File");
		MenuItem openItem = new MenuItem("Open");
		MenuItem saveItem = new MenuItem("Save");
		fileMenu.getItems().addAll(openItem, saveItem);
		menuBar.getMenus().add(fileMenu);
		borderPane.setTop(menuBar);

		pCamera = new PerspectiveCamera(true);
		pCamera.getTransforms().addAll(new Translate(0, 0, -60));

		shapesSub = new SubScene(shapesGroup, 340, 340, true, SceneAntialiasing.DISABLED);
		shapesSub.setFill(Color.AZURE);
		shapesSub.setCamera(pCamera);


		createSubScene();
		Scene scene = new Scene(borderPane, 550, 440);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Create 3D Shapes");
		primaryStage.show();
		saveItem.setOnAction(event -> {
			takeSnapShot(shapesSub);
			FileChooser fileChooser = new FileChooser();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
			fileChooser.getExtensionFilters().add(extFilter);
			File file = fileChooser.showSaveDialog(primaryStage);
			save(file);
		});
	}

	public void createSubScene() {
		addShape = new Button("Add Shape");
		addShape.setOnAction(new AddShapeButtonHandler());

		Slider scaleSlider = createScaleSlider();

		HBox scaleHBox = new HBox(scaleSlider);
		scaleHBox.setAlignment(Pos.CENTER);
		scaleHBox.setPadding(new Insets(30,10,10,10));

		modifyShape = new Button("Modify");

		rotator = new Button("Rotate");

		colors = new ChoiceBox<>();
		colors.getItems().addAll("-","Red","Green","Blue");

		gridPane2.add(new Label("Color: "), 0, 0);
		gridPane2.add(colors, 1, 0);

		gridPane2.add(new Label("New X: "), 0, 1);
		gridPane2.add(xField = new TextField(), 1, 1);
		xField.setPrefWidth(100);

		gridPane2.add(new Label("New Y: "), 0, 2);
		gridPane2.add(yField = new TextField(), 1, 2);
		yField.setPrefWidth(100);

		gridPane2.setHgap(20);
		gridPane2.setVgap(10);

		VBox modifyVBox = new VBox(15, rotator, gridPane2, modifyShape);

		modifyVBox.setAlignment(Pos.CENTER);
		modifyVBox.setPadding(new Insets(0, 10, 10, 10));

		VBox vbox1 = new VBox(scaleHBox, modifyVBox);
		HBox hbox1 = new HBox(addShape);
		hbox1.setAlignment(Pos. CENTER);
		hbox1.setPadding(new Insets(0,0,30,0));
		borderPane.setTop(menuBar);
		borderPane.setCenter(shapesSub);
		borderPane.setRight(vbox1);
		borderPane.setBottom(hbox1);
		borderPane.setPadding(new Insets(10));
	}

	public Slider createScaleSlider() {
		slider = new Slider(0, 200, 100);
		slider.setShowTickLabels( true);
		slider.setShowTickMarks( true);

		slider.valueProperty().addListener((observable, oldValue, newValue) -> {
			double size = (slider.getValue())/100;

			for(SphereObject s: spheres) {
				if(selectedShapeID == s.getID()) {
					s.getScale().setY(size);
					s.getScale().setX(size);
					s.getScale().setZ(size);
					s.setSize(size);
				}
			}

			for(BoxObject b: boxes) {
				if(selectedShapeID == b.getID()) {
					b.getScale().setY(size);
					b.getScale().setX(size);
					b.getScale().setZ(size);
					b.setSize(size);
				}
			}

			for(CylinderObject c: cylinders) {
				if(selectedShapeID == c.getID()) {
					c.getScale().setY(size);
					c.getScale().setX(size);
					c.getScale().setZ(size);
					c.setSize(size);
				}
			}
		});
		return slider;
	}

	public void createSphereForm() {
		clearFields();

		addSphere = new Button("Add Sphere");
		addSphere.setOnAction(new AddSphereButtonHandler());
		Label xLabel = new Label("X location:");
		Label yLabel = new Label("Y Location:");
		Label radiusLabel = new Label("Radius:");

		gridPane.add(xLabel, 0, 1);
		gridPane.add(xField, 1, 1);
		gridPane.add(yLabel, 0, 2);
		gridPane.add(yField, 1, 2);
		gridPane.add(radiusLabel, 0, 3);
		gridPane.add(radiusField, 1, 3);

		gridPane.setHgap(20);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(10,30,30,28));

		HBox submitHbox = new HBox(addSphere);
		submitHbox.setAlignment(Pos.CENTER);
		submitHbox.setPadding(new Insets(0,0,50,0));

		borderPane.setCenter(gridPane);
		borderPane.setBottom(submitHbox);
	}

	public void createBoxForm() {
		clearFields();

		addBox = new Button("Add Box");
		addBox.setOnAction(new AddBoxButtonHandler());
		Label xLabel = new Label("X location:");
		Label yLabel = new Label("Y Location:");
		Label lengthLabel = new Label("Length:");
		Label widthLabel = new Label("Width:");
		Label heightLabel = new Label("Height:");

		gridPane.add(xLabel, 0, 1);
		gridPane.add(xField, 1, 1);
		gridPane.add(yLabel, 0, 2);
		gridPane.add(yField, 1, 2);
		gridPane.add(lengthLabel, 0, 3);
		gridPane.add(lengthField, 1, 3);
		gridPane.add(widthLabel, 0, 4);
		gridPane.add(widthField, 1, 4);
		gridPane.add(heightLabel, 0, 5);
		gridPane.add(heightField, 1, 5);

		gridPane.setHgap(20);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(10,30,30,28));

		HBox submitHbox = new HBox(addBox);
		submitHbox.setAlignment(Pos.CENTER);
		submitHbox.setPadding(new Insets(0,0,50,0));

		borderPane.setCenter(gridPane);
		borderPane.setBottom(submitHbox);	
	}

	public void createCylinderForm() {
		clearFields();

		addCylinder = new Button("Add Cylinder");
		addCylinder.setOnAction(new AddCylinderButtonHandler());
		Label xLabel = new Label("X location:");
		Label yLabel = new Label("Y Location:");
		Label radiusLabel = new Label("Radius:");
		Label heightLabel = new Label("Height:");

		gridPane.add(xLabel, 0, 1);
		gridPane.add(xField, 1, 1);
		gridPane.add(yLabel, 0, 2);
		gridPane.add(yField, 1, 2);
		gridPane.add(radiusLabel, 0, 3);
		gridPane.add(radiusField, 1, 3);
		gridPane.add(heightLabel, 0, 4);
		gridPane.add(heightField, 1, 4);

		gridPane.setHgap(20);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(10,30,30,28));

		HBox submitHbox = new HBox(addCylinder);
		submitHbox.setAlignment(Pos.CENTER);
		submitHbox.setPadding(new Insets(0,0,50,0));

		borderPane.setCenter(gridPane);
		borderPane.setBottom(submitHbox);
	}

	public void clearFields() {
		gridPane.getChildren().clear();
		xField.setText(null);
		yField.setText(null);
		radiusField.setText(null);
		lengthField.setText(null);
		widthField.setText(null);
		heightField.setText(null);
	}

	private void takeSnapShot(SubScene scene){
		WritableImage writableImage = new WritableImage((int)scene.getWidth(), (int)scene.getHeight());
		scene.snapshot(null, writableImage);

		File file = new File("snapshot.png");
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
		} catch (IOException ex) {
			Logger.getLogger(Create3DShapes.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void save(File file) {
		try {
			if (file != null) {
				PrintWriter writer = new PrintWriter(file);
				for(SphereObject s: spheres) {
					writer.write("sphere, ");
					writer.write((int)s.getY() + ", ");
					writer.write((int)s.getX() + ", ");
					//writer.write((int)s.getScale().getZ() + ", ");
					writer.write((int)s.getSize());
					writer.println();
				}

				for(BoxObject b: boxes) {
					writer.write("box, ");
					writer.write((int)b.getScale().getY() + ", ");
					writer.write((int)b.getScale().getX() + ", ");
					writer.write((int)b.getScale().getZ() + ", ");
					writer.write((int)b.getSize() + ", ");
					writer.println();
				}

				for(CylinderObject c: cylinders) {
					writer.write("cylinder, ");
					writer.write((int)c.getScale().getY() + ", ");
					writer.write((int)c.getScale().getX() + ", ");
					writer.write((int)c.getScale().getZ() + ", ");
					writer.write((int)c.getSize() + ", ");
					writer.println();
				}
				writer.close();
			}
		}
		catch(FileNotFoundException f) {
			f.printStackTrace();
		}
	}

	public void load() {
		try {
			Scanner reader = new Scanner(new File("SaveFile.txt"));
			String line;
			while(reader.hasNext()) {
				line = reader.nextLine();
				System.out.println(line);
			}
		}
		catch(FileNotFoundException f) {
			f.printStackTrace();
		}
	}

	class AddShapeButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Label shapeLabel = new Label("Select a 3D shape: ");
			RadioButton sphereRadio = new RadioButton("Sphere");
			RadioButton boxRadio = new RadioButton("Box");
			RadioButton cylinderRadio = new RadioButton("Cylinder");

			ToggleGroup radioGroup = new ToggleGroup();
			sphereRadio.setToggleGroup(radioGroup);
			boxRadio.setToggleGroup(radioGroup);
			cylinderRadio.setToggleGroup(radioGroup);

			sphereRadio.setOnAction(event2 -> { createSphereForm(); });
			boxRadio.setOnAction(event2 -> { createBoxForm(); });
			cylinderRadio.setOnAction(event2 -> { createCylinderForm(); });

			HBox radioHBox = new HBox(20, shapeLabel, sphereRadio, boxRadio, cylinderRadio);
			radioHBox.setAlignment(Pos.CENTER);
			radioHBox.setPadding(new Insets(30,0,0,0));
			borderPane.setRight(null);
			borderPane.setCenter(null);
			borderPane.setTop(radioHBox);
		}
	}

	class AddSphereButtonHandler implements EventHandler<ActionEvent> {
		double newSize;

		@Override
		public void handle(ActionEvent event) {
			try {				
				SphereObject sObj = createNewSphere();
				shapesGroup.getChildren().add(sObj.getSphere());
				createSubScene();

				newSize = (slider.getValue()) / 100;
				sObj.setSize(newSize);

				sObj.getSphere().setOnMouseClicked(event2 -> {
					selectedShapeID = sObj.getID();
					slider.setValue(sObj.getSize()*100);
					xField.clear();
					yField.clear();
					colors.getSelectionModel().selectPrevious();
					modifyShape.setOnAction(event3 -> {
						newSize = (slider.getValue()) / 100;
						sObj.setSize(newSize);

						String col = colors.getSelectionModel().getSelectedItem();
						if(col != null) {
							if(col.equals("Green")){
								sObj.changeColor("Green");
							}else if(col.equals("Red")){
								sObj.changeColor("Red");
							}else if(col.equals("Blue")){
								sObj.changeColor("Blue");
							}else{
								sObj.changeColor("White");
							}
						}

						try {
							xLocation = Double.parseDouble(xField.getText());
							yLocation = Double.parseDouble(yField.getText());
							sObj.getTranslate().setX(xLocation);
							sObj.getTranslate().setY(yLocation);
						} catch(Exception e) {

						}

					});
					rotator.setOnAction(eventr -> {
						sObj.rotated();
					});
				});


			} catch(Exception ex) {
				System.out.println("Invalid input.");
			}
		}

		private SphereObject createNewSphere() {
			xLocation = Double.parseDouble(xField.getText());
			yLocation = Double.parseDouble(yField.getText());
			radius = Double.parseDouble(radiusField.getText());

			Sphere thisSphere = new Sphere(radius);
			SphereObject sob = new SphereObject(thisSphere, selectedShapeID, xLocation, yLocation);
			spheres.add(sob);
			selectedShapeID++;

			return sob;
		}
	}

	class AddBoxButtonHandler implements EventHandler<ActionEvent> {
		double newSize;

		@Override
		public void handle(ActionEvent event) {
			try {

				BoxObject bObj = createNewBox();
				shapesGroup.getChildren().add(bObj.getBox());
				createSubScene();

				newSize = (slider.getValue()) / 100;
				bObj.setSize(newSize);

				bObj.getBox().setOnMouseClicked(event2 -> {
					selectedShapeID = bObj.getID();
					slider.setValue(bObj.getSize()*100);
					xField.clear();
					yField.clear();
					colors.getSelectionModel().selectPrevious();
					modifyShape.setOnAction(event3 -> {
						newSize = (slider.getValue()) / 100;
						bObj.setSize(newSize);

						String col = colors.getSelectionModel().getSelectedItem();
						if(col != null) {
							if(col.equals("Green")){
								bObj.changeColor("Green");
							} else if(col.equals("Red")){
								bObj.changeColor("Red");
							} else if(col.equals("Blue")){
								bObj.changeColor("Blue");
							} else {
								bObj.changeColor("White");
							}
						}

						try {
							xLocation = Double.parseDouble(xField.getText());
							yLocation = Double.parseDouble(yField.getText());
							bObj.getTranslate().setX(xLocation);
							bObj.getTranslate().setY(yLocation);
						} catch(Exception e) {

						}

					});
					rotator.setOnAction(eventr -> {
						bObj.rotated();
					});
				});


			} catch(Exception ex) {
				System.out.println("Invalid input.");
			}
		}

		private BoxObject createNewBox() {
			xLocation = Double.parseDouble(xField.getText());
			yLocation = Double.parseDouble(yField.getText());
			length = Double.parseDouble(lengthField.getText());
			width = Double.parseDouble(widthField.getText());
			height = Double.parseDouble(heightField.getText());

			Box thisBox = new Box(length, width, height);
			BoxObject bob = new BoxObject(thisBox, selectedShapeID, xLocation, yLocation);
			boxes.add(bob);
			selectedShapeID++;

			return bob;
		}
	}

	class AddCylinderButtonHandler implements EventHandler<ActionEvent> {
		double newSize;

		@Override
		public void handle(ActionEvent event) {
			try {

				CylinderObject cObj = createNewCylinder();
				shapesGroup.getChildren().add(cObj.getCylinder());
				createSubScene();

				newSize = (slider.getValue()) / 100;
				cObj.setSize(newSize);

				cObj.getCylinder().setOnMouseClicked(event2 -> {
					selectedShapeID = cObj.getID();
					slider.setValue(cObj.getSize()*100);
					xField.clear();
					yField.clear();
					colors.getSelectionModel().selectPrevious();
					modifyShape.setOnAction(event3 -> {
						newSize = (slider.getValue()) / 100;
						cObj.setSize(newSize);

						String col = colors.getSelectionModel().getSelectedItem();
						if(col != null) {
							if(col.equals("Green")){
								cObj.changeColor("Green");
							}else if(col.equals("Red")){
								cObj.changeColor("Red");
							}else if(col.equals("Blue")){
								cObj.changeColor("Blue");
							}else{
								cObj.changeColor("White");
							}
						}

						try {
							xLocation = Double.parseDouble(xField.getText());
							yLocation = Double.parseDouble(yField.getText());
							cObj.getTranslate().setX(xLocation);
							cObj.getTranslate().setY(yLocation);
						} catch(Exception e) {

						}

					});
					rotator.setOnAction(eventr -> {

						cObj.rotated();

					});

				});

			} catch(Exception ex) {
				System.out.println("Invalid input.");
			}
		}

		private CylinderObject createNewCylinder() {
			xLocation = Double.parseDouble(xField.getText());
			yLocation = Double.parseDouble(yField.getText());
			radius = Double.parseDouble(radiusField.getText());
			height = Double.parseDouble(heightField.getText());

			Cylinder thisCylinder = new Cylinder(radius, height);
			CylinderObject cob = new CylinderObject(thisCylinder, selectedShapeID, xLocation, yLocation);
			cylinders.add(cob);
			selectedShapeID++;

			return cob;
		}
	}
}

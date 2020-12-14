package GroupProject;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class BoxObject {
	private Box box;
	private int id;
	private double size;
	private double x;
	private double y;
	private Scale scale;
	private Translate translate;

	private Rotate rotatedx = new Rotate(45,Rotate.X_AXIS);
	private Rotate rotatedy = new Rotate(90,Rotate.Y_AXIS);
//	private Rotate rotatedz = new Rotate(30,Rotate.Z_AXIS);


	public BoxObject(Box box, int id, double x, double y) {
		this.box = box;
		this.id = id;
		scale = new Scale();
		box.getTransforms().add(scale);
		
		this.x = x;
		this.y = y;
		translate = new Translate(x, y);
		box.getTransforms().add(translate);
	}
	
	public int getID() {
		return id;
	}

	public Box getBox() {
		return box;
	}
	
	public double getSize() {
		return size;
	}
	
	public Scale getScale() {
		return scale;
	}
	
	public Translate getTranslate() {
		return translate;
	}
	
	public void setSize(double size) {
		this.size = size;
	}
	
	public void setScale(Scale scale) {
		this.scale = scale;
	}

	public void rotated(){box.getTransforms().addAll(rotatedx,rotatedy);}

	public void changeColor(String col) {
		if (col.equals("Green")) {
			box.setMaterial(new PhongMaterial(Color.GREEN));
		} else if(col.equals("Red")) {
			box.setMaterial(new PhongMaterial(Color.RED));
		} else if(col.equals("Blue")) {
			box.setMaterial(new PhongMaterial(Color.BLUE));
		} else {
			box.setMaterial(new PhongMaterial(Color.WHITE));
		}
	}

}
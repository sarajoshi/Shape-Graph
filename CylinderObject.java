package GroupProject;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class CylinderObject {
	private Cylinder cylinder;
	private int id;
	private double size;
	private double x;
	private double y;
	private Scale scale;
	private Translate translate;

	private Rotate rotatedx = new Rotate(45,Rotate.X_AXIS);
	private Rotate rotatedy = new Rotate(90,Rotate.Y_AXIS);
	private Rotate rotatedz = new Rotate(30,Rotate.Z_AXIS);
	
	public CylinderObject(Cylinder cylinder, int id, double x, double y) {
		this.cylinder = cylinder;
		this.id = id;
		scale = new Scale();
		cylinder.getTransforms().add(scale);
		
		this.x = x;
		this.y = y;
		translate = new Translate(x, y);
		cylinder.getTransforms().add(translate);
	}
	
	public int getID() {
		return id;
	}

	public Cylinder getCylinder() {
		return cylinder;
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

	public void rotated(){cylinder.getTransforms().addAll(rotatedx,rotatedy,rotatedz);}

	public void changeColor(String col) {
		if (col.equals("Green")) {
			cylinder.setMaterial(new PhongMaterial(javafx.scene.paint.Color.GREEN));
		} else if(col.equals("Red")) {
			cylinder.setMaterial(new PhongMaterial(javafx.scene.paint.Color.RED));
		} else if(col.equals("Blue")) {
			cylinder.setMaterial(new PhongMaterial(Color.BLUE));
		} else {
			cylinder.setMaterial(new PhongMaterial(Color.WHITE));
		}
	}
}

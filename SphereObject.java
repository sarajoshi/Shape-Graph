package GroupProject;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class SphereObject {
	private Sphere sphere;
	private int id;
	private double size;
	private double x;
	private double y;
	private double radius;
	private Scale scale;
	private Translate translate;
	private Rotate rotatedx = new Rotate(45,Rotate.X_AXIS);
	private Rotate rotatedy = new Rotate(90,Rotate.Y_AXIS);
	private Rotate rotatedz = new Rotate(30,Rotate.Z_AXIS);
	
	public SphereObject(Sphere sphere, int id, double x, double y) {
		this.sphere = sphere;
		this.id = id;
		scale = new Scale();
		sphere.getTransforms().add(scale);
		
		this.x = x;
		this.y = y;
		translate = new Translate(x, y);
		sphere.getTransforms().add(translate);
	}
	
	public int getID() {
		return id;
	}

	public Sphere getSphere() {
		return sphere;
	}
	
	public double getSize() {
		return size;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
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

	public void rotated(){sphere.getTransforms().addAll(rotatedx,rotatedy,rotatedz);}

	public void changeColor(String col) {
		if (col.equals("Green")) {
			sphere.setMaterial(new PhongMaterial(Color.GREEN));
		} else if(col.equals("Red")) {
			sphere.setMaterial(new PhongMaterial(Color.RED));
		} else if(col.equals("Blue")) {
			sphere.setMaterial(new PhongMaterial(Color.BLUE));
		} else {
			sphere.setMaterial(new PhongMaterial(Color.WHITE));
		}
	}
}


package shapes;
import java.net.URL;

import javax.media.j3d.Appearance;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.*;



public class Primitives extends Group {
	public Primitives(Appearance BlackPlasticApp) {

		Primitive cone = new Cone(0.04f,0.08f,BlackPlasticApp);
		Transform3D tr = new Transform3D();
		tr.set(new Vector3f(0.8f, 0.0f,0.0f));
		TransformGroup tg = new TransformGroup(tr);
		tg.addChild(cone);
		this.addChild(tg);
		
		Primitive cilindro = new Cylinder(0.04f,0.12f,BlackPlasticApp);
		Transform3D trCilindro = new Transform3D();
		trCilindro.set(new Vector3f(0.9f, 0.0f,0.1f));
		TransformGroup tgCilindro = new TransformGroup(trCilindro);
		tgCilindro.addChild(cilindro);
		this.addChild(tgCilindro);
		
	}


}

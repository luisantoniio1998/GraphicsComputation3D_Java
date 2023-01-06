package projeto3d2;

import java.awt.Color;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Group;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PointLight;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import com.sun.j3d.utils.geometry.*;
import shapes.GeometryInfoExample;

public class ColorOptions extends Group {
	public ColorOptions(Appearance BlackPlasticApp,Appearance appearanceGouraud,Appearance appearanceTransparente,Appearance appearanceFlat,Appearance appearanceLines) {


		
//Shape Transparente
		GeometryInfoExample GeometryInfoTeste1 = new GeometryInfoExample(appearanceTransparente);

		Transform3D GeometryInfot1 = new Transform3D();
		GeometryInfot1.setScale(0.1f);
		GeometryInfot1.setTranslation(new Vector3f(-0.5f, 0.7f, 0f));
		TransformGroup GeometryInfotG1 = new TransformGroup(GeometryInfot1);
		GeometryInfotG1.addChild(GeometryInfoTeste1);
		this.addChild(GeometryInfotG1);

//2
		GeometryInfoExample GeometryInfoTeste2 = new GeometryInfoExample(appearanceGouraud);

		Transform3D GeometryInfot2 = new Transform3D();
		GeometryInfot2.setScale(0.1f);
		GeometryInfot2.setTranslation(new Vector3f(-0.4f, 0.7f, 0f));
		TransformGroup GeometryInfotG2 = new TransformGroup(GeometryInfot2);
		GeometryInfotG2.addChild(GeometryInfoTeste2);
		this.addChild(GeometryInfotG2);

//3
		GeometryInfoExample GeometryInfoTeste3 = new GeometryInfoExample(appearanceLines);

		Transform3D GeometryInfot3 = new Transform3D();
		GeometryInfot3.setScale(0.1f);
		GeometryInfot3.setTranslation(new Vector3f(-0.3f, 0.7f, 0f));
		TransformGroup GeometryInfotG3 = new TransformGroup(GeometryInfot3);
		GeometryInfotG3.addChild(GeometryInfoTeste3);
		this.addChild(GeometryInfotG3);

// 4
		GeometryInfoExample GeometryInfoTeste4 = new GeometryInfoExample(appearanceFlat);

		Transform3D GeometryInfot4 = new Transform3D();
		GeometryInfot4.setScale(0.1f);
		GeometryInfot4.setTranslation(new Vector3f(-0.2f, 0.7f, 0f));
		TransformGroup GeometryInfotG4 = new TransformGroup(GeometryInfot4);
		GeometryInfotG4.addChild(GeometryInfoTeste4);
		this.addChild(GeometryInfotG4);
		

	}
}







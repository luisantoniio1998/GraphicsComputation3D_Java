package shapes;

import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;
import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

public class GeometryInfoExample extends Shape3D {
	public GeometryInfoExample(Appearance x) {
		IndexedTriangleArray ita = new IndexedTriangleArray(6, GeometryArray.COORDINATES, 24);
		Point3f[] coords = new Point3f[6];
		coords[0] = new Point3f(0,1,0);
		coords[1] = new Point3f(0.5f,0,0);
		coords[2] = new Point3f(0,0,0.5f);
		coords[3] = new Point3f(0,-1,0);
		coords[4] = new Point3f(-0.5f,0,0);
		coords[5] = new Point3f(0,0,-0.5f);
		
		ita.setCoordinates(0, coords);
		int[] index = {0,2,1,0,1,5,0,5,4,0,4,2,3,1,2,3,2,4,3,4,5,3,5,1};
		
		ita.setCoordinateIndices(0, index);
		
		GeometryInfo gi = new GeometryInfo(ita);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(gi);

		this.setGeometry(gi.getGeometryArray());
		
		this.setAppearance(x);
		
	
	}

}
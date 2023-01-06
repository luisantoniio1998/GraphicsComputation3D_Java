package shapes;

import javax.media.j3d.Shape3D;
import javax.vecmath.Point3d;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

public class PolygonArray extends Shape3D{
	   public PolygonArray() {
		   
	    GeometryInfo gi = new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
	    Point3d[] vertices = {new Point3d(0f,0f,0f),
	    new Point3d(0f,0f,1f),new Point3d(1f,0f,1f),new Point3d(1f,0f,0f),
	    new Point3d(1f,1f,0f),new Point3d(1f,1f,1f),new Point3d(0f,1f,0f),
	    new Point3d(0f,1f,1f)};
	    int[] indices = {0,1,2,3, 7,1,2,5, 7,6,0,1, 6,0,3,4, 5,4,3,2, 7,1,2,5};
	    gi.setCoordinates(vertices);
	    gi.setCoordinateIndices(indices);
	    int[] stripCounts = {4,4,4,4,4,4};
	    gi.setStripCounts(stripCounts);
	    
	    
	    NormalGenerator ng = new NormalGenerator();
	    ng.generateNormals(gi);
	    
	    this.setGeometry(gi.getGeometryArray());
	  }
	}
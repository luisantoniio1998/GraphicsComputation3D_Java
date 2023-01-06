package shapes;

import java.awt.Color;

import javax.media.j3d.Geometry;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.IndexedLineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

public class myCube extends Shape3D {

	public myCube() {
		this.setGeometry(createGeometry());
	};

	private Geometry createGeometry() {
		IndexedLineArray axisLines = new IndexedLineArray(4, GeometryArray.COORDINATES, 12);
		axisLines.setCoordinate(0, new Point3d(0, 0, 0));
		axisLines.setCoordinate(1, new Point3d(1, 0, 0));
		axisLines.setCoordinate(2, new Point3d(0, 1, 0));
		axisLines.setCoordinate(3, new Point3d(0, 0, 1));
		axisLines.setCoordinateIndex(0, 0);
		axisLines.setCoordinateIndex(1, 1);
		axisLines.setCoordinateIndex(2, 0);
		axisLines.setCoordinateIndex(3, 2);
		axisLines.setCoordinateIndex(4, 0);
		axisLines.setCoordinateIndex(5, 3);
		axisLines.setCoordinateIndex(6, 1);
		axisLines.setCoordinateIndex(7, 2);
		axisLines.setCoordinateIndex(8, 1);
		axisLines.setCoordinateIndex(9, 3);
		axisLines.setCoordinateIndex(10, 2);
		axisLines.setCoordinateIndex(11, 3);

		return axisLines;
	}
}
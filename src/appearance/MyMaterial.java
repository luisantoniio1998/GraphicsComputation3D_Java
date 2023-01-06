package appearance;

import javax.media.j3d.Material;

public class MyMaterial extends Material {
	public final static int PLASTIC = 0;
	public final static int ORANGE = 1;
	public final static int RED = 2;
	public final static int WALL = 3;
	public final static int WHITE_PLASTIC = 4;
	public final static int BLACK_PLASTIC = 5;
	public final static int GOURAUD = 6;

	public MyMaterial(int type) {
		switch (type) {
		case PLASTIC:
			this.setAmbientColor(0.0f, 0.0f, 0.0f);
			this.setDiffuseColor(0.0f, 0.0f, 0.0f);
			this.setSpecularColor(0.0f, 0.0f, 0.0f);
			this.setShininess(25.6f);
			break;
		case ORANGE:
			this.setAmbientColor(0.255f, 0.127f, 0.8f);
			this.setDiffuseColor(0.255f, 0.127f, 0.8f);
			this.setSpecularColor(0.255f, 0.127f, 0.8f);
			this.setShininess(25.6f);
			break;
		case RED:
			this.setAmbientColor(0.220f, 0.020f, 0.060f);
			this.setDiffuseColor(0.220f, 0.020f, 0.060f);
			this.setSpecularColor(0.220f, 0.020f, 0.060f);
			this.setShininess(25.6f);
			break;
		case WALL:
			this.setAmbientColor(0.019f, 0.015f, 0.034f);
			this.setDiffuseColor(0.019f, 0.015f, 0.034f);
			this.setSpecularColor(0.019f, 0.015f, 0.034f);
			this.setShininess(25.6f);
			break;
		case WHITE_PLASTIC:
			this.setAmbientColor(0.220f, 0.220f, 0.220f);
			this.setDiffuseColor(0.220f, 0.220f, 0.220f);
			this.setSpecularColor(0.220f, 0.220f, 0.220f);
			this.setShininess(25.6f);
			break;
		case BLACK_PLASTIC:
			this.setAmbientColor(0.255f, 0.0f, 0.0f);
			this.setDiffuseColor(0.2550f, 0.0f, 0.0f);
			this.setSpecularColor(0.550f, 0.0f, 0.0f);
			this.setShininess(25.6f);
			break;
		case GOURAUD:
			this.setAmbientColor(0.255f, 0.0f, 0.0f);
			this.setDiffuseColor(0.255f, 0.0f, 0.0f);
			this.setDiffuseColor(0.255f, 0.0f, 0.0f);
			this.setSpecularColor(1f, 1f, 1f);
			this.setShininess(0f);
			break;
		}
		
	}

}

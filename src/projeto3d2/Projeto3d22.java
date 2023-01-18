package projeto3d2;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.media.j3d.Alpha;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.AudioDevice;
import javax.media.j3d.Background;
import javax.media.j3d.Billboard;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.DistanceLOD;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.ImageComponent;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.MediaContainer;
import javax.media.j3d.Morph;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PointLight;
import javax.media.j3d.PointSound;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.ScaleInterpolator;
import javax.media.j3d.Screen3D;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Sound;
import javax.media.j3d.Switch;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Text3D;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureCubeMap;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.sun.j3d.audioengines.javasound.JavaSoundMixer;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.universe.SimpleUniverse;

import appearance.MyMaterial;
import shapes.PolygonArray;
import shapes.myCube;

public class Projeto3d22 extends Applet implements ActionListener, KeyListener, MouseListener {
    //========== BUTTONS ==========================
	private Button go = new Button("Start");
    private Button save = new Button("OffScreen");
    private Button reset = new Button("Reset View");
    //======== SIMPLE UNIVERSE =========================
    static SimpleUniverse su;
    //======= POINT LIGHT AND BOUNDS ========================
    PointLight pLight = null;
	BoundingSphere bounds = new BoundingSphere();

	//======= TRANSFORMGROUPS =================
    private TransformGroup objTrans;
    private TransformGroup objTrans1;
    private TransformGroup[] objTrans2 = new TransformGroup[30];
    private Transform3D trans = new Transform3D();  
    private TransformGroup[] objTrans3 = new TransformGroup[40];   
    //============== TIMER AND SIGN FOR KEY BEHAVIOR ============ 
    private float sign = 0;
    private Timer timer;
    //========== LOD IMAGES ==========================
	BufferedImage[] images = new BufferedImage[3];
	//====== LINE POSITION ==================== 
    private float linePosition = 0;
    //====== MOVE CAR AND CRASH VERIFY ===============
    private float xloc = 0.0f;
    //========= SKY VARIABLES ==============
    private int[] skyZPosition = new int[40];
    private int[] skyXPosition = new int[40];
    private int[] skyYPosition = new int[40];
    //======== DIFICULTY AND GAME OVER ===============
    private int dificuldade = 5;
    private TransformGroup[] objTrans4 = new TransformGroup[dificuldade];
    private int[] obsZPosition = new int[dificuldade];
    private int[] obsXPosition = new int[dificuldade];
     private int gameOver = 1;
     Text3D textFinal;
     //======== PICK CANVAS ===============
    PickCanvas pc = null;    
    //========= ROOT =========================
    BranchGroup objRoot = new BranchGroup(); 
    
    public void crashVerify(){
        for(int i = 0; i < dificuldade; i++){
            if(obsZPosition[i]*0.1f <= 0f && obsZPosition[i]*0.1f >= -0.6f){
                if(obsXPosition[i]*0.1f <= 0.05f+xloc && obsXPosition[i]*0.1f >= -0.05f+xloc){
                    if(gameOver == 1){
                        gameOver = 1000;
                        System.out.println("GAME OVER");
                    }
                }
            }
        }
    }
    
    public void buildObstacles(){
        Appearance aparencia = new Appearance();
        Color3f cor1 = new Color3f(0.6f, 0.6f, 0.1f);
        Color3f cor2 = new Color3f(0,0,0);
        aparencia.setMaterial(new Material(cor1, cor2, cor1, cor2, 1f));
        Cylinder cyl = new Cylinder();
        Transform3D[] pos3 = new Transform3D[40];
        for (int i = 0; i < dificuldade; i++) {
            objTrans4[i] = new TransformGroup(); 
            cyl = new Cylinder(0.05f, 0.2f, aparencia);
            pos3[i] = new Transform3D();
            obsZPosition[i] = ThreadLocalRandom.current().nextInt(-400, -200 + 1);
            obsXPosition[i] = ThreadLocalRandom.current().nextInt(-6, 6 + 1);
            pos3[i].setTranslation(new Vector3f(obsXPosition[i]*0.1f, -0.3f, obsZPosition[i]*0.1f));
            objTrans4[i].addChild(cyl);
            objTrans4[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTrans4[i].setTransform(pos3[i]);
            objRoot.addChild(objTrans4[i]);
        }
    }
    
    public void moveObstacles(){
        for(int i = 0; i<dificuldade; i++){
            if(obsZPosition[i]>=5f){
                obsZPosition[i] = ThreadLocalRandom.current().nextInt(-400, -200 + 1);
                obsXPosition[i] = ThreadLocalRandom.current().nextInt(-6, 6 + 1);
            }else{
                obsZPosition[i] = obsZPosition[i] + 1;
            }
            trans.setTranslation(new Vector3f(obsXPosition[i]*0.1f, -0.3f, obsZPosition[i]*0.1f));
            objTrans4[i].setTransform(trans);
        }
    }

    public void buildStreet() {
        objTrans1 = new TransformGroup(); 
        Appearance aparencia = new Appearance();
        aparencia.setMaterial(new Material(new Color3f(0.0f, 0.0f, 0.0f), new Color3f(0.0f, 0.0f, 0.0f), new Color3f(0f, 0f, 0f), new Color3f(0f, 0f, 0f), 10f));
        Box rua = new Box(0.7f, 0.1f, 40f, aparencia);
        Transform3D pos2 = new Transform3D();
        pos2.setTranslation(new Vector3f(0.0f, -0.5f, 0.0f));
        objTrans1.addChild(rua);
        objTrans1.setTransform(pos2);
        objRoot.addChild(objTrans1);
    }
     
    public void drawStreetLines() {
        Appearance aparencia = new Appearance();
        Color3f col = new Color3f(1.0f, 1.0f, 1.0f);
        ColoringAttributes ca = new ColoringAttributes(col, ColoringAttributes.NICEST);
        aparencia.setColoringAttributes(ca);
        Cone linha = new Cone();
        Transform3D[] pos3 = new Transform3D[30];
        for (int i = 0; i < 30; i++) {
            objTrans2[i] = new TransformGroup(); 
            linha = new Cone(0.01f, 0.01f, aparencia);
            pos3[i] = new Transform3D();
            pos3[i].setTranslation(new Vector3f(0.0f, -0.4f, 0.4f - 1f * i));
            objTrans2[i].addChild(linha);
            objTrans2[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTrans2[i].setTransform(pos3[i]);
            objRoot.addChild(objTrans2[i]);
        }
    }
    
    public void drawSkyEffects(){
        Appearance aparencia = new Appearance();
        aparencia.setMaterial(new Material(new Color3f(1.0f,1.0f,1.0f), new Color3f(0.5f, 0f, 0.4f), new Color3f(0.5f, 0.0f, 0.4f), new Color3f(0.5f, 0.0f, 0.4f), 1f));
        Sphere sphere = new Sphere();
        Transform3D[] pos3 = new Transform3D[40];
        for (int i = 0; i < 40; i++) {
            objTrans3[i] = new TransformGroup(); 
            sphere = new Sphere(0.01f, aparencia);
            pos3[i] = new Transform3D();
            skyZPosition[i] = ThreadLocalRandom.current().nextInt(-100, 10 + 1);
            skyYPosition[i] = ThreadLocalRandom.current().nextInt(-10, 10 + 1);
            skyXPosition[i] = ThreadLocalRandom.current().nextInt(-10, 10 + 1);
            pos3[i].setTranslation(new Vector3f(skyXPosition[i]*0.1f, skyYPosition[i]*0.1f, skyZPosition[i]*0.1f));
            objTrans3[i].addChild(sphere);
            objTrans3[i].setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTrans3[i].setTransform(pos3[i]);
            objRoot.addChild(objTrans3[i]);
        }
    }
    
    public void moveSkyEffects(){
        for(int i = 0; i<40; i++){
            if(skyZPosition[i]>=1.0f){
                skyZPosition[i] = ThreadLocalRandom.current().nextInt(-100, 10 + 1);
                skyYPosition[i] = ThreadLocalRandom.current().nextInt(-10, 10 + 1);
                skyXPosition[i] = ThreadLocalRandom.current().nextInt(-10, 10 + 1);
            }else{
                skyZPosition[i] = skyZPosition[i] + 1;
            }
            trans.setTranslation(new Vector3f(skyXPosition[i]*0.1f, skyYPosition[i]*0.1f, skyZPosition[i]*0.1f));
            objTrans3[i].setTransform(trans);
        }
    }

    public void moveStreetLines() {
            if (linePosition >= 1.0f) {
                linePosition = 0;
            }else{
                linePosition = linePosition + 0.1f;
            }
        for (int i = 0; i < 30; i++) {
            trans.setTranslation(new Vector3f(0.0f, -0.4f, linePosition + 0.4f - 1f * i));
            objTrans2[i].setTransform(trans);
        }
    }
    
	void loadImages() {
		URL filename = getClass().getClassLoader().getResource("images/redbull.png");
		try {
			images[0] = ImageIO.read(filename);
			AffineTransform xform = AffineTransform.getScaleInstance(0.5, 0.5);
			AffineTransformOp scaleOp = new AffineTransformOp(xform, null);
			for (int i = 1; i < 3; i++) {
				images[i] = scaleOp.filter(images[i - 1], null);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

    public void buildCar() {
        Appearance aparencia = new Appearance();
        Appearance aparencia1 = new Appearance();
        aparencia.setMaterial(new Material(new Color3f(0.0f, 0.0f, 0.6f), new Color3f(0.0f, 0.0f, 0.6f), new Color3f(1.0f, 1.0f, 0.6f), new Color3f(1.0f, 0f, 0.6f), 10f));
        objTrans = new TransformGroup(); 
        objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Box sphere = new Box(0.05f, 0.03f, 0.3f, aparencia); 
        aparencia1.setMaterial(new Material(new Color3f(0.0f, 0.0f, 0.0f), new Color3f(0.0f, 0.0f, 0.0f), new Color3f(0f, 0f, 0.0f), new Color3f(0f, 0f, 0f), 10f));
        Box sphere1 = new Box(0.04f, 0.01f, 0.31f, aparencia1);
        Box sphere2 = new Box(0.0501f, 0.01f, 0.27f, aparencia1);
        Transform3D pos1 = new Transform3D();
        pos1.setTranslation(new Vector3f(0.0f, -0.3f, 0.2f)); 
        objTrans.setTransform(pos1); 
        objTrans.addChild(sphere1); 
        objTrans.addChild(sphere);
        objTrans.addChild(sphere2);
        objRoot.addChild(objTrans); 
    }

    public BranchGroup createSceneGraph() {
    	
    	// ======== ROOT =============== 
    	 objRoot = new BranchGroup();
    	
    	//========== BILLBOARD LOAD TEXTURE ==================
    	Transform3D billboardtr = new Transform3D();
    	billboardtr.rotY(Math.PI/3);
    	Transform3D billboardtr2 = new Transform3D();
    	billboardtr2.setScale(0.15);
    	billboardtr.mul(billboardtr2);
    	TransformGroup objBillboard = new TransformGroup(billboardtr);
    	
    	// ======== LOADER ===================================
    	ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
    	Scene scene = null;
    	try {
    		scene = file.load(ClassLoader.getSystemResource("Horse.obj"));
    	} catch (Exception e) {
    	}
    	objBillboard.addChild(scene.getSceneGroup());
    	
    	// ============= BILLBOARD ====================
    	TransformGroup bbTg = new TransformGroup();
    	bbTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    	objRoot.addChild(bbTg);
    	Billboard bb = new Billboard(bbTg, Billboard.ROTATE_ABOUT_POINT, new Point3f(0.65f, 0.4f, 0.25f));
    	bb.setSchedulingBounds(bounds);
    	bbTg.addChild(bb);
    	Transform3D tr12 = new Transform3D();
    	tr12.setTranslation(new Vector3f(-0.8f, 0.5f, 0f));
    	TransformGroup tg12 = new TransformGroup(tr12);
    	tg12.addChild(objBillboard);
    	bbTg.addChild(tg12);
    	
    	//=============== ROTATION INTERPOLATOR ======================= 

		TransformGroup spinball = new TransformGroup();
		spinball.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objRoot.addChild(spinball);
		
		// object
		Appearance apPlaneta = createTextureAppearance();
		Sphere sphere22 = new Sphere(0.10f, Primitive.GENERATE_TEXTURE_COORDS, 50, apPlaneta);

		Transform3D tr00 = new Transform3D();
		tr00.set(new Vector3f(0.4f, 0.7f, 0.0f));
		TransformGroup tg00 = new TransformGroup(tr00);
		tg00.addChild(sphere22);

		spinball.addChild(tg00);

		// ========= ROTATOR ===================================================
		Alpha alpha22 = new Alpha(-1, 10000);
		RotationInterpolator rotator22 = new RotationInterpolator(alpha22, spinball);
		rotator22.setAxisOfRotation(tr00);
		rotator22.setSchedulingBounds(bounds);
		spinball.addChild(rotator22);
    	
		//============= MATERIAL =======================================
    	
		Appearance BlackPlasticApp = new Appearance();
		MyMaterial blackplastic = new MyMaterial(MyMaterial.BLACK_PLASTIC);
		BlackPlasticApp.setMaterial(blackplastic);
		
		// ============= POSITION INTERPOLATOR ========================
		TransformGroup spinball1 = new TransformGroup();
		spinball1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		spinball1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objRoot.addChild(spinball1);
		
		// object
		Appearance apPlaneta1 = createTextureAppearance();
		Sphere sphere222 = new Sphere(0.10f, Primitive.GENERATE_TEXTURE_COORDS, 50, apPlaneta1);

		Transform3D tr001 = new Transform3D();
		tr001.set(new Vector3f(-0.4f, 1.1f, 0.0f));
		TransformGroup tg001 = new TransformGroup(tr001);
		tg001.addChild(sphere222);
		
		spinball1.addChild(tg001);

		//============ INTERPOLATOR ============================
	    Alpha alpha1 = new Alpha(-1, 6000);
	    alpha1.setMode(Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE);
	    alpha1.setDecreasingAlphaDuration(6000);
	    PositionInterpolator translator = new PositionInterpolator(alpha1, spinball1);
	    translator.setSchedulingBounds(bounds);
	    translator.setEnable(true);
	    objRoot.addChild(translator);
	    
	    //=========== SCALE INTERPOLATOR ======================
	    
	    TransformGroup box = new TransformGroup();
	    box.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
	    box.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    objRoot.addChild(box);
	    
	    //object
	    Appearance apBox = createTextureAppearance();
	    Box box22 = new Box(0.1f,Primitive.GENERATE_TEXTURE_COORDS, 0.1f, apBox );
	    
	    Transform3D tr0 = new Transform3D();
	    tr0.set(new Vector3f(-0.8f, 1.1f, 0.0f));
	    tr0.setScale(0.1);
	    TransformGroup tg0 = new TransformGroup(tr0);
	    tg0.addChild(box22);
	    box.addChild(tg0);
	    
	    //======== INTERPOLATOR =================
	    Alpha alpha3 = new Alpha(-1, 6000);
	    alpha3.setMode(Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE);
	    alpha3.setDecreasingAlphaDuration(6000);
	    ScaleInterpolator zoom = new ScaleInterpolator(alpha3, box);
	    BoundingSphere bounds1 = new BoundingSphere(new Point3d(0,0,0),100);
	    zoom.setSchedulingBounds(bounds1);
	    zoom.setEnable(true);
	    objRoot.addChild(zoom);
	    
		
		//=======  SHAPE INDEXEDLINEARRAY =========================
		
		Appearance apPolygn = new Appearance();
		apPolygn.setMaterial(new Material());
		Shape3D cube = new myCube();
		cube.setAppearance(apPolygn);
		
		Transform3D trcube = new Transform3D();
		trcube.setScale(0.15);
		trcube.setTranslation(new Vector3f(0.8f, 0.1f, 0f));
		TransformGroup tgcube = new TransformGroup(trcube);
		tgcube.addChild(cube);
		objRoot.addChild(tgcube);
		
		//=========== POLYGON SHADES / TRANSPARENCY / FLAT / NICEST / GOURAUD ==========================
		
		//======= SHADE GOURAD ======================
		Color3f colour = new Color3f(Color.orange);
		Appearance appearanceGouraud = new Appearance();
		appearanceGouraud.setColoringAttributes(new ColoringAttributes(colour, ColoringAttributes.SHADE_GOURAUD));
		Material m = new Material();
		m.setAmbientColor(colour);
		m.setEmissiveColor(0.255f, 0f, 0f);
		m.setDiffuseColor(colour);
		m.setSpecularColor(1f, 1f, 1f);
		m.setShininess(0f);
		appearanceGouraud.setMaterial(m);
		
		//=========== TRANSPARENT ==================
		int tMode = TransparencyAttributes.BLENDED;
		float tValue = 0.6f;
		TransparencyAttributes ta = new TransparencyAttributes(tMode, tValue);
		Appearance appearanceTransparente = new Appearance();
		appearanceTransparente.setTransparencyAttributes(ta);
		Material m2 = new Material();
		m2.setAmbientColor(colour);
		m2.setEmissiveColor(0.255f, 0f, 0f);
		m2.setDiffuseColor(colour);
		m2.setSpecularColor(1f, 1f, 1f);
		m2.setShininess(0f);
		appearanceTransparente.setMaterial(m2);
		
		// ======= FLAT ==============
		Appearance appearanceFlat = new Appearance();
		ColoringAttributes ca2 = new ColoringAttributes();
		ca2.setShadeModel(ColoringAttributes.SHADE_FLAT);
		appearanceFlat.setColoringAttributes(ca2);
		appearanceFlat.setMaterial(null);
		RenderingAttributes ra2 = new RenderingAttributes();
		ra2.setIgnoreVertexColors(false);
		appearanceFlat.setRenderingAttributes(ra2);
		Material m3 = new Material();
		m3.setAmbientColor(colour);
		m3.setEmissiveColor(0.255f, 0f, 0f);
		m3.setDiffuseColor(colour);
		m3.setSpecularColor(1f, 1f, 1f);
		m3.setShininess(0f);
		appearanceFlat.setMaterial(m3);
		
		//============== LINES ===================================
		Appearance appearanceLines = new Appearance();
		appearanceLines.setPolygonAttributes(
		new PolygonAttributes(PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_BACK, 0));
		appearanceLines.setLineAttributes(new LineAttributes(3, LineAttributes.PATTERN_DASH, false));
		Material m4 = new Material();
		m4.setAmbientColor(colour);
		m4.setEmissiveColor(0.255f, 0f, 0f);
		m4.setDiffuseColor(colour);
		m4.setSpecularColor(1f, 1f, 1f);
		m4.setShininess(0f);
		appearanceLines.setMaterial(m3);
		
		ColorOptions colorOptions = new ColorOptions(BlackPlasticApp, appearanceGouraud, appearanceTransparente,
		appearanceFlat, appearanceLines);
		objRoot.addChild(colorOptions);

		// ======== CUSTOM GEOMETRY ==========================

		Appearance apMirror = createTextureAppearanceMirror();

		TexCoordGeneration tcg = new TexCoordGeneration();
		tcg.setGenMode(TexCoordGeneration.REFLECTION_MAP);
		tcg.setFormat(TexCoordGeneration.TEXTURE_COORDINATE_3);
		tcg.setPlaneR(new Vector4f(2, 0, 0, 0));
		tcg.setPlaneS(new Vector4f(0, 2, 0, 0));
		tcg.setPlaneT(new Vector4f(0, 0, 2, 0));
		apMirror.setTexCoordGeneration(tcg);

		PolygonArray polygon = new PolygonArray();
		polygon.setAppearance(apMirror);

		Transform3D trMyCustomShape = new Transform3D();
		trMyCustomShape.setScale(0.15f);
		trMyCustomShape.setTranslation(new Vector3f(0.78f, -0.16f, 0f));
		TransformGroup tgMyCustomShape = new TransformGroup(trMyCustomShape);
		tgMyCustomShape.addChild(polygon);
		objRoot.addChild(tgMyCustomShape);
    	
		// ========== LOD ====================	   
		Transform3D trml = new Transform3D();
		trml.setTranslation(new Vector3f(-0.8f, 0.1f, 0f));
		TransformGroup objTrans = new TransformGroup(trml);
		objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objRoot.addChild(objTrans);
		
		Switch sw = new Switch(0);
		sw.setCapability(Switch.ALLOW_SWITCH_READ);
		sw.setCapability(Switch.ALLOW_SWITCH_WRITE);
		objTrans.addChild(sw);

		// ===== 3 LEVELS OF VIEW ==================
		loadImages();
		Appearance apLod = createAppearance(0);
		sw.addChild(new Sphere(0.1f, Primitive.GENERATE_TEXTURE_COORDS, 40, apLod));
		apLod = createAppearance(1);
		sw.addChild(new Sphere(0.1f, Primitive.GENERATE_TEXTURE_COORDS, 20, apLod));
		apLod = createAppearance(2);
		sw.addChild(new Sphere(0.1f, Primitive.GENERATE_TEXTURE_COORDS, 10, apLod));

		// ===== DISTANCE LOD BEHAVIOR ============
		float[] distances = new float[2];
		distances[0] = 2.5f;
		distances[1] = 5.0f;
		DistanceLOD lod = new DistanceLOD(distances);
		lod.addSwitch(sw);
		BoundingSphere boundsLod = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 5.0);
		lod.setSchedulingBounds(boundsLod);
		objTrans.addChild(lod);
		
		//========= MORPHING ==========================
		Appearance appear = new Appearance();
		appear.setPolygonAttributes(
				new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_BACK, 0));
		appear.setLineAttributes(new LineAttributes(3, LineAttributes.PATTERN_SOLID, false));

		GeometryArray[] geoms = new GeometryArray[4];
		geoms[0] = morph1();
		geoms[1] = morph2();
		geoms[2] = morph3();
		geoms[3] = morph4();
		
		Morph morph = new Morph(geoms, appear);
		morph.setCapability(Morph.ALLOW_WEIGHTS_READ);
		morph.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
		Transform3D trm = new Transform3D();
		trm.setTranslation(new Vector3f(0.85f, 0.5f, -0.1f));
		trm.setScale(0.1);
		TransformGroup tgm = new TransformGroup(trm);
		tgm.addChild(morph);
		objRoot.addChild(tgm);
		
		// ======== BEHAVIOR NODE =========================
		Alpha alpha2 = new Alpha(-1, Alpha.INCREASING_ENABLE, 0, 0, 2000, 0, 0, 2000, 0, 0);
		MorphingBehavior mb = new MorphingBehavior(morph, alpha2);
		BoundingSphere bounds2 = new BoundingSphere();
		mb.setSchedulingBounds(bounds2);
		objRoot.addChild(mb);
		
		//============= TEXT 3D ============================
    	Appearance text3dap1 = new Appearance();
    	MyMaterial whitePlastic = new MyMaterial(MyMaterial.WHITE_PLASTIC);
    	text3dap1.setMaterial(whitePlastic);
    	
    	Appearance text3dap2 = new Appearance ();
    	MyMaterial wallapp = new MyMaterial(MyMaterial.WALL);
    	text3dap2.setMaterial(wallapp);
    	
    	Appearance text3dap3 = new Appearance();
    	MyMaterial orangeapp = new MyMaterial(MyMaterial.ORANGE);
    	text3dap3.setMaterial(orangeapp);
    	
    	Appearance text3dap4 = new Appearance();
    	MyMaterial gouradapp = new MyMaterial(MyMaterial.GOURAUD);
    	text3dap4.setMaterial(gouradapp);
    	
    	Appearance text3dap5 = new Appearance();
    	MyMaterial redapp = new MyMaterial(MyMaterial.RED);
    	text3dap5.setMaterial(redapp);
    	
    	Appearance text3dap6 = new Appearance();
    	MyMaterial plasticapp = new MyMaterial(MyMaterial.PLASTIC);
    	text3dap6.setMaterial(plasticapp);
    	
    	
    	Font3D font = new Font3D(new Font("SansSerif", Font.PLAIN, 1), new FontExtrusion());
    	Font3D font1 = new Font3D(new Font("Arial", Font.PLAIN, 1), new FontExtrusion());
    	Text3D text = new Text3D(font, "PROJETO 3D");
    	Shape3D shape3dtext = new Shape3D(text, text3dap1);
    	Transform3D textt = new Transform3D();
    	textt.setScale(0.2);
    	textt.setTranslation(new Vector3f(-0.6f, 0.28f, 0f));
    	TransformGroup ttext = new TransformGroup(textt);
    	objRoot.addChild(ttext);
    	ttext.addChild(shape3dtext);
    	
    	Appearance text3dap = new Appearance();
    	text3dap.setMaterial(new Material());
    	Text3D text1 = new Text3D(font, "Billboard");
    	Shape3D shape3dtext1 = new Shape3D(text1, text3dap);
    	Transform3D text1tr = new Transform3D();
    	text1tr.setScale(0.05);
    	text1tr.setTranslation(new Vector3f(-0.85f, 0.25f, 0f));
    	TransformGroup texttt = new TransformGroup(text1tr);
    	objRoot.addChild(texttt);
    	texttt.addChild(shape3dtext1);
    	
    	Text3D text2 = new Text3D(font, "LOD");
    	Shape3D shape3dtext2 = new Shape3D(text2, text3dap2);
    	Transform3D text2tr = new Transform3D();
    	text2tr.setScale(0.05);
    	text2tr.setTranslation(new Vector3f(-0.85f, -0.05f, 0f));
    	TransformGroup textttt = new TransformGroup(text2tr);
    	objRoot.addChild(textttt);
    	textttt.addChild(shape3dtext2);
    	
    	Text3D text3 = new Text3D(font, "Morph");
    	Shape3D shape3dtext3 = new Shape3D(text3, text3dap3);
    	Transform3D text3tr = new Transform3D();
    	text3tr.setScale(0.05);
    	text3tr.setTranslation(new Vector3f(0.77f, 0.35f, 0f));
    	TransformGroup text33 = new TransformGroup(text3tr);
    	objRoot.addChild(text33);
    	text33.addChild(shape3dtext3);
    	
    	Text3D text12 = new Text3D(font1 , "Indexed ");
    	Shape3D shape3dtext4 = new Shape3D(text12, text3dap4);
    	Transform3D tex4tr = new Transform3D();
    	tex4tr.setScale(0.05);
    	tex4tr.setTranslation(new Vector3f(0.75f, 0.05f, 0f));
    	TransformGroup text44 = new TransformGroup(tex4tr);
    	objRoot.addChild(text44);
    	text44.addChild(shape3dtext4);
    	
    	Text3D text22 = new Text3D(font1, "Custom");
    	Shape3D shape3dtext5 = new Shape3D(text22, text3dap5);
    	Transform3D text5tr = new Transform3D();
    	text5tr.setScale(0.05);
    	text5tr.setTranslation(new Vector3f(0.8f, -0.22f, 0f));
    	TransformGroup text55 = new TransformGroup(text5tr);
    	objRoot.addChild(text55);
    	text55.addChild(shape3dtext5);
    	
    	Text3D text333 = new Text3D(font, "Shades");
    	Shape3D shape3dtext6 = new Shape3D(text333, text3dap);
    	Transform3D text6tr = new Transform3D();
    	text6tr.setScale(0.05);
    	text6tr.setTranslation(new Vector3f(-0.4f, 0.55f, 0f));
    	TransformGroup text66 = new TransformGroup(text6tr);
    	objRoot.addChild(text66);
    	text66.addChild(shape3dtext6);
    	
    	Text3D text444 = new Text3D(font, "Rotation Interpolator");
    	Shape3D shape3dtext444 = new Shape3D(text444, text3dap6);
    	Transform3D text7tr = new Transform3D();
    	text7tr.setScale(0.05);
    	text7tr.setTranslation(new Vector3f(0.23f, 0.55f, 0f));
    	TransformGroup text77 = new TransformGroup(text7tr);
    	objRoot.addChild(text77);
    	text77.addChild(shape3dtext444);
    	
    	Text3D text555 = new Text3D(font, "Position Interpolator");
    	Shape3D shape3dtext555 = new Shape3D(text555, text3dap);
    	Transform3D text8tr= new Transform3D();
    	text8tr.setScale(0.05);
    	text8tr.setTranslation(new Vector3f(-0.2f, 0.9f, 0f));
    	TransformGroup text88 = new TransformGroup(text8tr);
    	objRoot.addChild(text88);
    	text88.addChild(shape3dtext555);
    	
    	Text3D text666 = new Text3D(font, "Scale Interpolator");
    	Shape3D shape3dtext666 = new Shape3D(text666, text3dap);
    	Transform3D text9tr= new Transform3D();
    	text9tr.setScale(0.05);
    	text9tr.setTranslation(new Vector3f(-1f, 0.8f, 0f));
    	TransformGroup text99 = new TransformGroup(text9tr);
    	objRoot.addChild(text99);
    	text99.addChild(shape3dtext666);
    	
    	//======= PICKING PERMISSIONS =============================
    	ttext.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
    	ttext.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    	ttext.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    	//======= SOUND =========================================
    	TransformGroup objTrans1 = new TransformGroup(trans);
        objTrans1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objTrans1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objRoot.addChild(objTrans1);
    	        
        // ====== SOUND 3D ==============================================
        PointSound sound = new PointSound();
        URL url = this.getClass().getClassLoader().getResource("images/sample3.au");
        MediaContainer mc = new MediaContainer(url);
        sound.setSoundData(mc);
        sound.setLoop(Sound.INFINITE_LOOPS);
        sound.setInitialGain(1f);
        sound.setEnable(true);
        float[] distances1 = {1f, 20f};
        float[] gains = {1f, 0.001f};
        sound.setDistanceGain(distances1, gains);
        BoundingSphere soundBounds =
        new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        sound.setSchedulingBounds(soundBounds);
        objTrans.addChild(sound);
        
        //=========== BACKGROUND ============================================
        url = getClass().getClassLoader().getResource("images/Split_Sky.jpg");
        BufferedImage bi = null;
        try {
          bi = ImageIO.read(url);
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi);
        Background background = new Background(image);
        BoundingSphere sphere = new BoundingSphere(new Point3d(0, 0, 0), 100.0);
        background.setApplicationBounds(sphere);
        objRoot.addChild(background);
       
        //======== BUILD AND DRAW FUNCTIONS ====================
        this.drawStreetLines();
        this.buildCar();
        this.buildStreet();
        this.drawSkyEffects();
        this.buildObstacles();
        
        BoundingSphere bounds3 = new BoundingSphere(new Point3d(0.0, 0.0, 0.0),50);
        Color3f light1Color = new Color3f(Color.white);
        Vector3f light1Direction = new Vector3f(0.0f, -7.0f, -10.0f);

        //============= DIRECTIONAL LIGHT ===========================
        DirectionalLight light1 = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds3);
        objRoot.addChild(light1); 

        // =========== AMBIENT LIGHT ==============================
        Color3f ambientColor = new Color3f(1.0f, 1.0f, 0.0f);
        AmbientLight ambientLightNode = new AmbientLight(ambientColor);
        ambientLightNode.setInfluencingBounds(bounds3);
        objRoot.addChild(ambientLightNode);

        //============= POINT LIGHT ======================== 
        pLight = new PointLight(new Color3f(Color.ORANGE), new Point3f(3f, 3f, 3f), new Point3f(1f, 0f, 0f));
		pLight.setCapability(PointLight.ALLOW_STATE_READ);
		pLight.setCapability(PointLight.ALLOW_STATE_WRITE);
		pLight.setInfluencingBounds(bounds3);
		objRoot.addChild(pLight); 
		
		return objRoot;
    }
    
    	private Canvas3D c,c1;
    	private Canvas3D offScreenCanvas;
    	private View view;

    public Projeto3d22() {
    	setLayout(new GridLayout(1, 2));
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        c1 = new Canvas3D(config);
        c = new Canvas3D(config); 
        add(c);
        add(c1);

        // ======= KEY AND MOUSE LISTENER ==========
        c.addKeyListener(this);
        c.addMouseListener(this);
        
        timer = new Timer(10, this);
        Panel p = new Panel();
        
        su = new SimpleUniverse(c);
        su.getViewingPlatform().setNominalViewingTransform();
        
        BranchGroup bgView = createView(c1, new Point3d(0, 2.7, 4), new Point3d(0, 0, 0), new Vector3d(1, 0, 1));
		su.addBranchGraph(bgView);
        
        p.add("South",go);
        p.add("South",reset);
        p.add("South", save);
        go.addActionListener(this);
        go.addKeyListener(this);
        reset.addActionListener(this);
        
        OrbitBehavior orbit = new OrbitBehavior(c);
		orbit.setSchedulingBounds(bounds);
		su.getViewingPlatform().setViewPlatformBehavior(orbit);
        
        //======== OFF SCREEN CANVAS =======================
        view = su.getViewer().getView();
        offScreenCanvas = new Canvas3D(config, true);
        Screen3D sOn = c.getScreen3D();
        Screen3D sOff = offScreenCanvas.getScreen3D();
        Dimension dim = sOn.getSize();
        sOff.setSize(dim);
        sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth());
        sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight());
        Point loc = c.getLocationOnScreen();
        offScreenCanvas.setOffScreenLocation(loc);
        save.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent ev) {
                BufferedImage bi = capture();
                save(bi);
              }
        });

        add(p);
        
        BranchGroup scene = createSceneGraph();

        
        //========== AUDIO DEVICE ============================
        AudioDevice audioDev = new JavaSoundMixer(su.getViewer().getPhysicalEnvironment());
    	//audioDev.initialize();
        su.getViewingPlatform().setNominalViewingTransform();
        su.addBranchGraph(scene);
    	pc = new PickCanvas(c, scene);
		pc.setMode(PickTool.GEOMETRY);
    }
    
     BranchGroup createView(Canvas3D cv, Point3d eye, Point3d center, Vector3d vup) {
		View view = new View();
		view.setProjectionPolicy(View.PARALLEL_PROJECTION);

		ViewPlatform vp = new ViewPlatform();

		view.addCanvas3D(cv);
		view.attachViewPlatform(vp);
		view.setPhysicalBody(new PhysicalBody());
		view.setPhysicalEnvironment(new PhysicalEnvironment());
		
		Transform3D trans = new Transform3D();
		trans.lookAt(eye, center, vup);
		trans.invert();
		TransformGroup tg = new TransformGroup(trans);
		tg.addChild(vp);
		
		BranchGroup bgView = new BranchGroup();
		bgView.addChild(tg);
		return bgView;
	}
    //=============== CREATE APPEARANCE FOR CUSTOM SHAPE ================================ 
    Appearance createTextureAppearanceMirror() {
		Appearance apMirror = new Appearance();
	    URL filename = 
	            getClass().getClassLoader().getResource("images/mirror.jpg");
	        TextureLoader loader = new TextureLoader(filename, this);
	        ImageComponent2D image1 = loader.getImage();


	        TextureCubeMap texture = new TextureCubeMap(Texture.BASE_LEVEL, Texture.RGBA,
	        	    image1.getWidth());
	        	    texture.setImage(0, TextureCubeMap.NEGATIVE_X, image1);
	        	    texture.setImage(0, TextureCubeMap.NEGATIVE_Y, image1);
	        	    texture.setImage(0, TextureCubeMap.NEGATIVE_Z, image1);
	        	    texture.setImage(0, TextureCubeMap.POSITIVE_X, image1);
	        	    texture.setImage(0, TextureCubeMap.POSITIVE_Y, image1);
	        	    texture.setImage(0, TextureCubeMap.POSITIVE_Z, image1);

		texture.setEnable(true);
		texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
		apMirror.setTexture(texture);

		TexCoordGeneration tcg = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR,
				TexCoordGeneration.TEXTURE_COORDINATE_3);
		tcg.setPlaneR(new Vector4f(1, 0, 0, 0));
		tcg.setPlaneS(new Vector4f(0, 1, 0, 0));
		tcg.setPlaneT(new Vector4f(0, 0, 1, 0));
		apMirror.setTexCoordGeneration(tcg);
		apMirror.setCapability(Appearance.ALLOW_TEXGEN_WRITE);
		return apMirror;
	}
    
    // ======= CREATE TEXTURE USING TEXTURE 2D ===============================
	Appearance createAppearance(int i) {
		Appearance appear = new Appearance();
		ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, images[i]);
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		texture.setEnable(true);
		texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
		texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
		appear.setTexture(texture);
		return appear;
	}
    
		// ========= MORPHS =============================
		GeometryArray morph1() {
			IndexedTriangleArray iaa = new IndexedTriangleArray(6, GeometryArray.COORDINATES, 24);
			Point3f[] coords = new Point3f[6];
			coords[0] = new Point3f(0, 1, 0);
			coords[1] = new Point3f(0.5f, 0, 0);
			coords[2] = new Point3f(0, 0, 0.5f);
			coords[3] = new Point3f(0, -1, 0);
			coords[4] = new Point3f(-0.5f, 0, 0);
			coords[5] = new Point3f(0, 0, -0.5f);

			iaa.setCoordinates(0, coords);
			int[] index = { 0, 2, 1, 0, 1, 5, 0, 5, 4, 0, 4, 2, 3, 1, 2, 3, 2, 4, 3, 4, 5, 3, 5, 1 };

			iaa.setCoordinateIndices(0, index);

			GeometryInfo geom = new GeometryInfo(iaa);
			NormalGenerator ng = new NormalGenerator();
			ng.generateNormals(geom);
			return geom.getGeometryArray();
		}

		GeometryArray morph2() {
			IndexedTriangleArray iaa = new IndexedTriangleArray(6, GeometryArray.COORDINATES, 24);
			Point3f[] coords = new Point3f[6];
			coords[0] = new Point3f(0, 0.5f, 0);
			coords[1] = new Point3f(0.3f, 0, 0);
			coords[2] = new Point3f(0, 0, 0.3f);
			coords[3] = new Point3f(0, -0.5f, 0);
			coords[4] = new Point3f(-0.3f, 0, 0);
			coords[5] = new Point3f(0, 0, -0.3f);

			iaa.setCoordinates(0, coords);
			int[] index = { 0, 2, 1, 0, 1, 5, 0, 5, 4, 0, 4, 2, 3, 1, 2, 3, 2, 4, 3, 4, 5, 3, 5, 1 };

			iaa.setCoordinateIndices(0, index);

			GeometryInfo geom = new GeometryInfo(iaa);
			NormalGenerator ng = new NormalGenerator();
			ng.generateNormals(geom);
			return geom.getGeometryArray();
		}

		GeometryArray morph3() {
			IndexedTriangleArray iaa = new IndexedTriangleArray(6, GeometryArray.COORDINATES, 24);
			Point3f[] coords = new Point3f[6];
			coords[0] = new Point3f(0, 0.5f, 0);
			coords[1] = new Point3f(0.2f, 0, 0);
			coords[2] = new Point3f(0, 0, 0.2f);
			coords[3] = new Point3f(0, -0.5f, 0);
			coords[4] = new Point3f(-0.2f, 0, 0);
			coords[5] = new Point3f(0, 0, -0.2f);

			iaa.setCoordinates(0, coords);
			int[] index = { 0, 2, 1, 0, 1, 5, 0, 5, 4, 0, 4, 2, 3, 1, 2, 3, 2, 4, 3, 4, 5, 3, 5, 1 };

			iaa.setCoordinateIndices(0, index);

			GeometryInfo geom = new GeometryInfo(iaa);
			NormalGenerator ng = new NormalGenerator();
			ng.generateNormals(geom);
			return geom.getGeometryArray();
		}

		GeometryArray morph4() {
			IndexedTriangleArray iaa = new IndexedTriangleArray(6, GeometryArray.COORDINATES, 24);
			Point3f[] coords = new Point3f[6];
			coords[0] = new Point3f(0, 1, 0);
			coords[1] = new Point3f(0.5f, 0, 0);
			coords[2] = new Point3f(0, 0, 0.5f);
			coords[3] = new Point3f(0, -1, 0);
			coords[4] = new Point3f(-0.5f, 0, 0);
			coords[5] = new Point3f(0, 0, -0.5f);

			iaa.setCoordinates(0, coords);
			int[] index = { 0, 2, 1, 0, 1, 5, 0, 5, 4, 0, 4, 2, 3, 1, 2, 3, 2, 4, 3, 4, 5, 3, 5, 1 };

			iaa.setCoordinateIndices(0, index);

			GeometryInfo geom = new GeometryInfo(iaa);
			NormalGenerator ng = new NormalGenerator();
			ng.generateNormals(geom);
			return geom.getGeometryArray();
		}
		
		//============ TEXTURE2D APPEARANCE ==================== 
		Appearance createTextureAppearance() {
			Appearance ap = new Appearance();
			URL filename = getClass().getClassLoader().getResource("images/image.jpg");
			TextureLoader loader = new TextureLoader(filename, this);
			ImageComponent2D image = loader.getImage();
			if (image == null) {
				System.out.println("can't find texture file.");
			}

			Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
			texture.setImage(0, image);
			texture.setEnable(true);
			texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
			texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
			ap.setTexture(texture);
			return ap;
		}
		
    	//=============== OFF SCREEN ============================
    public BufferedImage capture() {
        Dimension dim = c.getSize();
        view.stopView();
        view.addCanvas3D(offScreenCanvas);
        BufferedImage bImage =
        new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
        ImageComponent2D buffer =
        new ImageComponent2D(ImageComponent.FORMAT_RGB, bImage);
        offScreenCanvas.setOffScreenBuffer(buffer);
        view.startView();
        offScreenCanvas.renderOffScreenBuffer();
        offScreenCanvas.waitForOffScreenRendering();
        bImage = offScreenCanvas.getOffScreenBuffer().getImage();
        view.removeCanvas3D(offScreenCanvas);
        return bImage;
      }

    //========= SAVE IMAGE TO FILE ===========================
    public void save(BufferedImage bImage) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
          File oFile = chooser.getSelectedFile();
          try {
            ImageIO.write(bImage, "jpeg", oFile);
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'd') {
            sign = 1;
        }
        if (e.getKeyChar() == 'a') {
            sign = -1;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'd') {
            sign = 0;
        }
        if (e.getKeyChar() == 'a') {
            sign = 0;
        }
    }

    public void keyTyped(KeyEvent e) {
    	
    }

    //======= START GAME WHEN PRESSED PLAY BUTTON ======================== 
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == go) {
            if (!timer.isRunning()) {
                timer.start();
            }
        } else {
            xloc += .01 * sign;
            if(xloc >= .6){
                xloc -= 0.01;
            }
            if(xloc <= -.6){
                xloc += 0.01;
            }
            this.moveStreetLines();
            this.moveSkyEffects();
            this.moveObstacles();
            this.crashVerify();
            trans.setTranslation(new Vector3f(xloc, -0.3f, gameOver*0.2f));
            objTrans.setTransform(trans);
        }if(e.getSource() == reset) {
        	Transform3D teste = new Transform3D();
        	teste.set(new Vector3f(0.0f, 0.0f, 2.7f));
        	Projeto3d22.getSimpleU().getViewingPlatform().getViewPlatformTransform().setTransform(teste);
        }
    }
    
	public static SimpleUniverse getSimpleU() {
		return su;
	}
    //================= MAIN =============================== 
    static MainFrame mf;
    public static void main(String[] args) {
        System.out.println("Program Started");
        Projeto3d22 bb = new Projeto3d22();
        bb.addKeyListener(bb);
        mf = new MainFrame(bb, 900, 700);
    }

    //======== PICKING ==========================
	@Override
	public void mouseClicked(MouseEvent e) {
		pc.setShapeLocation(e);
		PickResult result = pc.pickClosest();
		if(result != null) {
		    System.out.println("1");
			TransformGroup tg = (TransformGroup) result.getNode(PickResult.TRANSFORM_GROUP);
			if(tg != null) {
				System.out.println("2");
				Transform3D tr = new Transform3D();
				tg.getTransform(tr);
				Transform3D rot = new Transform3D();
				rot.rotX(Math.PI);
				tr.mul(rot);
				tg.setTransform(tr);
			}
		}
			
}
	@Override
	public void mousePressed(MouseEvent e) {
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		
	}

}



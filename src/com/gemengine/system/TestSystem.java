package com.gemengine.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gemengine.component.CameraComponent;
import com.gemengine.component.CameraComponent.ViewportType;
import com.gemengine.component.DebugComponent;
import com.gemengine.component.PointComponent;
import com.gemengine.component.SpriteAtlasComponent;
import com.gemengine.component.SpriteComponent;
import com.gemengine.component.ui.UIContainerComponent;
import com.gemengine.component.ui.UILabelComponent;
import com.gemengine.component.ui.UIStageComponent;
import com.gemengine.entity.Entity;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;
import com.google.inject.Inject;

import game.component.AutoScript;
import game.component.ClickScript;
import game.component.EmptyScript;
import game.component.OverlayScript;
import game.component.ShowScript;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TestSystem extends TimedSystem {
	private final EntitySystem entitySystem;
	private final ComponentSystem componentSystem;
	private final SaveSystem saveSystem;
	private final AssetSystem assetSystem;
	private final TimingSystem timingSystem;

	@Inject
	public TestSystem(EntitySystem entitySystem, ComponentSystem componentSystem, SaveSystem saveSystem,
			ScriptSystem scr, AssetSystem assetSystem, TimingSystem timingSystem) {
		super();
		this.entitySystem = entitySystem;
		this.saveSystem = saveSystem;
		this.componentSystem = componentSystem;
		this.assetSystem = assetSystem;
		this.timingSystem = timingSystem;
	}

	@Override
	public void onInit() {
		assetSystem.loadFolder("assets/img/");
		doTest();
	}

	void activateDebug() {
		val debug = entitySystem.create("debug");
		debug.createComponent(DebugComponent.class);
	}

	void createCamera(String name) {
		val cam = entitySystem.create(name);
		cam.createComponent(PointComponent.class);
		cam.createComponent(CameraComponent.class).makeOrthographicCamera().setViewportType(ViewportType.Screen);
	}

	void createSpriteTest() {
		val gem = entitySystem.create("gem");
		gem.createComponent(PointComponent.class);
		gem.createComponent(SpriteComponent.class).setTexturePath("assets/img/gem.png").setSize(100, 100);
		gem.createComponent(ClickScript.class);
		gem.createComponent(OverlayScript.class);
		val cam = entitySystem.get("camera");
		cam.addChild(entitySystem.get("gem"));
	}

	void doSaveTest() {
		// saveSystem.save("assets/scene/test.json");
		saveSystem.load("assets/scene/test.json");
	}

	void createMultiple() {
		val cam = entitySystem.get("camera");
		int count = 0;
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 10; i++) {
				Entity gem = entitySystem.create("E" + count++);
				gem.createComponent(PointComponent.class).setRelativePosition(new Vector3(i * 20, j * 20, 0)).setRelativeScale(0.2f);
				gem.createComponent(SpriteAtlasComponent.class).setName("lipsy").setFrame(0)
						.setTexturePath("assets/atlas/lipsyBoss.atlas");
				gem.createComponent(AutoScript.class);
				gem.createComponent(OverlayScript.class);
				cam.addChild(gem);
			}
		}
	}

	void createButton(String texture, Vector3 pos, int size) {
		Entity ent = entitySystem.create("~" + texture);
		ent.createComponent(PointComponent.class).setRelativePosition(pos);
		ent.createComponent(SpriteComponent.class).setTexturePath(texture).setSize(size, size);
		val cam = entitySystem.get("camera");
		cam.addChild(ent);
	}

	void createButtons() {
		List<String> components = Arrays.asList("gem", "script", "camera", "point", "tag");
		int i = 0;
		for (String component : components) {
			i++;
			String componentImg = "assets/img/" + component + ".png";
			createButton(componentImg, new Vector3(i * 64 - 32, Gdx.graphics.getHeight() - 32, 0), 64);
		}
	}

	void createStage() {
		val stage = entitySystem.create("stage");
		stage.createComponent(UIStageComponent.class);

		val cam = entitySystem.get("uiCamera");
		cam.addChild(entitySystem.get("stage"));
	}

	void createShower() {
		Entity show = entitySystem.create("show");
		show.createComponent(ShowScript.class);
	}
	
	void crashIf(boolean doCrash) throws Exception{
		if(doCrash){
			throw new Exception("Test didnt pass");
		}
	}
	
	void doFunctionalityTests() throws Exception{
		Entity grandparent = entitySystem.create("grandparent");
		Entity parent = entitySystem.create("parent");
		Entity child = entitySystem.create("child");
		Entity grandchild = entitySystem.create("grandchild");
		grandparent.addChild(parent);
		parent.addChild(child);
		child.addParent(grandchild);
		crashIf(grandparent.getDescendants().size() == 3);
		log.debug("Success");
	}

	@Override
	public void onUpdate(float delta) {
	}

	void doTest() {
		try {
			doFunctionalityTests();
			log.debug("Test Passed");
		} catch (Exception e) {
			log.debug("Test Failed");
		}
		// activateDebug();
		//createCamera("camera");
		//createCamera("uiCamera");
		//createStage();
		//createShower();
		// createButtons();
		// createSpriteTest();
		//createMultiple();
		// createLabelTest();
		// doSaveTest();
	}
}

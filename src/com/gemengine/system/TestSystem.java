package com.gemengine.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gemengine.component.CameraComponent;
import com.gemengine.component.DebugComponent;
import com.gemengine.component.PointComponent;
import com.gemengine.component.SpriteComponent;
import com.gemengine.component.ui.UILabelComponent;
import com.gemengine.component.ui.UIStageComponent;
import com.gemengine.entity.Entity;
import com.gemengine.system.base.ComponentUpdaterSystem;
import com.google.inject.Inject;

import game.component.AutoScript;
import game.component.ClickScript;
import game.component.EmptyScript;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TestSystem extends ComponentUpdaterSystem {
	private final EntitySystem entitySystem;
	private final ComponentSystem componentSystem;
	private final SaveSystem saveSystem;
	private final AssetSystem assetSystem;

	@Inject
	public TestSystem(EntitySystem entitySystem, ComponentSystem componentSystem, SaveSystem saveSystem,
			ScriptSystem scr, AssetSystem assetSystem) {
		super(componentSystem);
		this.entitySystem = entitySystem;
		this.saveSystem = saveSystem;
		this.componentSystem = componentSystem;
		this.assetSystem = assetSystem;
	}

	@Override
	public void onInit() {
		assetSystem.loadFolder("assets/img/");
		doTest();
	}

	@Override
	public void onNext(Entity ent) {
		System.out.println(ent.getName());
	}

	void activateDebug() {
		val debug = entitySystem.create("debug");
		debug.createComponent(DebugComponent.class);
	}

	void createCamera() {
		val cam = entitySystem.create("camera");
		cam.createComponent(PointComponent.class);
		cam.createComponent(CameraComponent.class).makeOrthographicCamera();
	}

	void createLabelTest() {
		val scene = entitySystem.create("scene");
		scene.createComponent(PointComponent.class);
		scene.createComponent(UIStageComponent.class);
		val label = entitySystem.create("label");
		label.createComponent(PointComponent.class);
		label.createComponent(UILabelComponent.class).setText("text").setFont("assets/font/sea.fnt").setSize(100, 100);
		scene.addChild(label);
		val cam = entitySystem.get("camera");
		cam.addChild(entitySystem.get("scene"));
		//label.createComponent(ClickScript.class);
		//scene.createComponent(ClickScript.class);
	}

	void createSpriteTest() {
		val gem = entitySystem.create("gem");
		gem.createComponent(PointComponent.class);
		gem.createComponent(SpriteComponent.class).setTexturePath("assets/img/gem.png").setSize(100, 100);
		gem.createComponent(ClickScript.class);
		val cam = entitySystem.get("camera");
		cam.addChild(entitySystem.get("gem"));
	}

	void doSaveTest() {
		// saveSystem.save("assets/scene/test.json");
		// saveSystem.load("assets/scene/test.json");
	}

	void createMultiple() {
		val cam = entitySystem.get("camera");
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 10; i++) {
				Entity gem = entitySystem.create("e" + (i + " " + j));
				gem.createComponent(PointComponent.class).setRelativePosition(new Vector3(i * 20, j * 20, 0));
				gem.createComponent(SpriteComponent.class).setTexturePath("assets/img/gem.png").setSize(20, 20);
				gem.createComponent(AutoScript.class);
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

	void doTest() {
		// activateDebug();
		createCamera();
		// createButtons();
		// createSpriteTest();
		// createMultiple();
		createLabelTest();
		// doSaveTest();
	}
}

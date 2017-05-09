package com.gemengine.system;

import com.badlogic.gdx.math.Vector3;
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
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TestSystem extends ComponentUpdaterSystem {
	private final EntitySystem entitySystem;
	private final ComponentSystem componentSystem;
	private final SaveSystem saveSystem;

	@Inject
	public TestSystem(EntitySystem entitySystem, ComponentSystem componentSystem, SaveSystem saveSystem) {
		super(componentSystem);
		this.entitySystem = entitySystem;
		this.saveSystem = saveSystem;
		this.componentSystem = componentSystem;
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
		// label.createComponent(ClickScript.class);
		// scene.createComponent(ClickScript.class);
	}

	void createSpriteTest() {
		val gem = entitySystem.create("gem");
		gem.createComponent(PointComponent.class);
		gem.createComponent(SpriteComponent.class).setTexturePath("assets/img/gem-ico128.png").setSize(50, 50);
		gem.createComponent(ClickScript.class);
		val cam = entitySystem.get("camera");
		//cam.addChild(entitySystem.get("gem"));
	}

	void doSaveTest() {
		saveSystem.save("assets/scene/test.json");
		// saveSystem.load("assets/scene/test.json");
	}

	void createMultiple() {
		for (int i = 0; i < 1; i++) {
			val gem = entitySystem.create("e" + i);
			gem.createComponent(PointComponent.class).setRelativePosition(new Vector3(i*50, -i * 50, 0));
			gem.createComponent(SpriteComponent.class).setTexturePath("assets/img/gem-ico128.png").setSize(50, 50);
			gem.createComponent(AutoScript.class);
			val cam = entitySystem.get("camera");
			cam.addChild(gem);
		}
	}

	void doTest() {
		//activateDebug();
		createCamera();
		//createSpriteTest();
		createMultiple();
		//createLabelTest();
		doSaveTest();
	}
}

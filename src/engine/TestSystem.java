package engine;


import com.gemengine.entity.Entity;
import com.google.inject.Inject;

import engine.base.ComponentUpdaterSystem;
import engine.component.DragosScript;
import engine.component.Position;
import lombok.val;

public class TestSystem extends ComponentUpdaterSystem {
	private EntitySystem e;
	private ComponentSystem c;

	@Inject
	public TestSystem(EntitySystem entitySystem, ComponentSystem componentSystem) {
		super(componentSystem);
		//super(createConfiguration(Position.class), true, 5);
		this.e = entitySystem;
		this.c = componentSystem;
		doTest();
	}

	@Override
	public void onBeforeEntities() {
		// System.out.println("b");
	}

	@Override
	public void onInit() {
	}
	
	@Override
	public void onNext(Entity ent) {
		System.out.println(ent.getName());
	}

	void doTest() {
		val ent = e.create("Dragos");
		c.create(ent, DragosScript.class);
	}
}

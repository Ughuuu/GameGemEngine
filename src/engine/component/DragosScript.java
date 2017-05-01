package engine.component;

import com.gemengine.entity.Entity;
import com.google.inject.Inject;

import engine.ComponentSystem;
import engine.EntitySystem;
import engine.component.Script;
import lombok.val;

public class DragosScript extends Script {

	@Inject
	public DragosScript(EntitySystem entitySystem, ComponentSystem componentSystem) {
		super(entitySystem, componentSystem);
	}
	
	@Override
	public void onUpdate(float delta){
		//val ent = createEntity("nap");
	}
}

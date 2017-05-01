package engine;

import com.gemengine.entity.Entity;
import com.gemengine.system.base.SystemBase;
import com.google.inject.Inject;

import engine.base.ComponentUpdaterSystem;
import engine.component.Position;
import engine.component.Script;

public class ScriptSystem extends ComponentUpdaterSystem{

	public ScriptSystem() {
		super(createConfiguration(Script.class), true, 5);
	}
	
	@Override
	public void onNext(Entity ent) {
		System.out.println(ent.getName());
	}
}

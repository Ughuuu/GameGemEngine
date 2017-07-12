package game.component;

import com.gemengine.component.base.ScriptComponent;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class EmptyScript extends ScriptComponent {
	public EmptyScript(EntitySystem entitySystem, ComponentSystem componentSystem) {
		super(entitySystem, componentSystem);
	}
}

package game.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.gemengine.component.PointComponent;
import com.gemengine.component.base.ScriptComponent;
import com.gemengine.entity.Entity;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.google.inject.Inject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class EmptyScript extends ScriptComponent {
	public EmptyScript(EntitySystem entitySystem, ComponentSystem componentSystem) {
		super(entitySystem, componentSystem);
	}
}

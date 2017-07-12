package game.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.gemengine.component.base.PointComponent;
import com.gemengine.component.base.ScriptComponent;
import com.gemengine.entity.Entity;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.google.inject.Inject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ClickScript extends ScriptComponent {
	@Inject
	public ClickScript(EntitySystem entitySystem, ComponentSystem componentSystem) {
		super(entitySystem, componentSystem);
	}

	@Override
	public void onUpdate(float delta) {
		Entity owner = getOwner();
		Vector3 pos = owner.getComponent(PointComponent.class).getRelativePosition();
		Vector3 dir = new Vector3();
		dir.x = Gdx.input.getX();
		dir.y = (Gdx.graphics.getHeight() - Gdx.input.getY());
		//dir.scl(delta * 5);
		pos.set(dir);
		owner.getComponent(PointComponent.class).setRelativePosition(pos);
	}
}

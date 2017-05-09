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
public class ClickScript extends ScriptComponent {

	@Inject
	public ClickScript(EntitySystem entitySystem, ComponentSystem componentSystem) {
		super(entitySystem, componentSystem);
	}

	@Override
	public void onUpdate(float delta) {
		doLogic();
	}

	void doLogic() {
		Entity owner = getOwner();
		Vector3 pos = owner.getComponent(PointComponent.class).getRelativePosition();
		pos.x = Gdx.input.getX();
		pos.y = Gdx.graphics.getHeight() - Gdx.input.getY();
		owner.getComponent(PointComponent.class).setRelativePosition(pos);
	}
}

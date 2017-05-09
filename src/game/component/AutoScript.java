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
public class AutoScript extends ScriptComponent {
	private static int count = 0;
	private int dir = 1;

	@Inject
	public AutoScript(EntitySystem entitySystem, ComponentSystem componentSystem) {
		super(entitySystem, componentSystem);
		if (count % 2 == 0) {
			dir = -1;
		}
	}

	@Override
	public void onUpdate(float delta) {
		doLogic();
	}

	void doLogic() {
		Entity owner = getOwner();
		Vector3 pos = owner.getComponent(PointComponent.class).getRelativePosition();
		pos.x += dir * 5;
		if (pos.x < 0) {
			dir *= -1;
		}
		pos.x %= Gdx.graphics.getWidth();
		owner.getComponent(PointComponent.class).setRelativePosition(pos);
	}
}

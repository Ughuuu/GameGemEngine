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
	private Vector3 dir = new Vector3((float) Math.random() - 0.5f, (float) Math.random() - 0.5f, 0.0f).nor();
	Entity owner;
	Vector3 pos;

	@Inject
	public AutoScript(EntitySystem entitySystem, ComponentSystem componentSystem) {
		super(entitySystem, componentSystem);
	}

	@Override
	public void onInit() {
		owner = getOwner();
		pos = owner.getComponent(PointComponent.class).getRelativePosition();
	}

	@Override
	public void onUpdate(float delta) {
		pos.x += dir.x * 2;
		pos.y += dir.y * 20;
		if (pos.x < 0) {
			pos.x = 0;
			dir.x *= -1;
		}
		if (pos.x > Gdx.graphics.getWidth()) {
			pos.x = Gdx.graphics.getWidth();
			dir.x *= -1;
		}
		if (pos.y < 0) {
			pos.y = 0;
			dir.y *= -1;
		}
		if (pos.y > Gdx.graphics.getHeight()) {
			pos.y = Gdx.graphics.getHeight();
			dir.y *= -1;
		}
		owner.getComponent(PointComponent.class)
				.setRelativeRotation(new Vector3(0, 0, (float) Math.atan2(dir.y, dir.x) * 57.29f - 280));
		owner.getComponent(PointComponent.class).setRelativePosition(pos);
	}
}

package game.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.gemengine.component.base.PointComponent;
import com.gemengine.component.base.ScriptComponent;
import com.gemengine.component.twod.SpriteAtlasComponent;
import com.gemengine.entity.Entity;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.gemengine.system.twod.CameraSystem;
import com.gemengine.system.twod.CameraTrackerSystem;
import com.google.inject.Inject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class AutoScript extends ScriptComponent {
	private static int count = 0;
	private Vector3 dir;
	private Entity owner;
	private Vector3 pos;
	private SpriteAtlasComponent sprite;
	float frame = 0;
	private final CameraSystem cameraSystem;

	@Inject
	public AutoScript(EntitySystem entitySystem, ComponentSystem componentSystem, CameraSystem cameraSystem) {
		super(entitySystem, componentSystem);
		this.cameraSystem = cameraSystem;
	}

	@Override
	public void onInit() {
		owner = getOwner();
		pos = owner.getComponent(PointComponent.class).getRelativePosition();
		sprite = owner.getComponent(SpriteAtlasComponent.class);
		dir = new Vector3();
		dir.x = (float) (Math.random() - 0.5);
		dir.y = (float) (Math.random() - 0.5);
	}

	@Override
	public void onUpdate(float delta) {
		float width = cameraSystem.getWatchingCamera(getOwner()).getWidth();
		float height = cameraSystem.getWatchingCamera(getOwner()).getHeight();
		frame++;
		if (frame > 15 && frame < 16) {
			sprite.setFrame((sprite.getFrame() + 1) % 2);
		}
		if (frame > 40) {
			frame = 0;
			sprite.setFrame((sprite.getFrame() + 1) % 2);
		}
		pos.x += dir.x * 200 * delta;
		pos.y += dir.y * 200 * delta;
		if (pos.x < 0) {
			pos.x = 0;
			dir.x *= -1;
		}
		if (pos.x > width) {
			pos.x = width;
			dir.x *= -1;
		}
		if (pos.y < 0) {
			pos.y = 0;
			dir.y *= -1;
		}
		if (pos.y > height) {
			pos.y = height;
			dir.y *= -1;
		}
		PointComponent point = owner.getComponent(PointComponent.class);
		float ang = (float) Math.atan2(dir.y, dir.x) * 57.29f - 280;
		if (Math.abs(ang - point.getRelativeRotation().z) > 0.01f) {
			ang = ang - (ang - point.getRelativeRotation().z) / 2f;
		}
		point.setRelativeRotation(new Vector3(0, 0, ang));
		point.setRelativePosition(pos);
	}
}

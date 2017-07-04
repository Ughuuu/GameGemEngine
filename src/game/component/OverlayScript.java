package game.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.gemengine.component.PointComponent;
import com.gemengine.component.LabelComponent;
import com.gemengine.component.base.DrawableComponent;
import com.gemengine.component.base.ScriptComponent;
import com.gemengine.component.ui.UIContainerComponent;
import com.gemengine.component.ui.UILabelComponent;
import com.gemengine.component.ui.UIStageComponent;
import com.gemengine.entity.Entity;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.google.inject.Inject;

import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class OverlayScript extends ScriptComponent {
	private PointComponent followerPos;
	private Entity owner;
	private DrawableComponent draw;
	private LabelComponent labelComponent;

	@Inject
	public OverlayScript(EntitySystem entitySystem, ComponentSystem componentSystem) {
		super(entitySystem, componentSystem);
	}

	@Override
	public void onInit() {
		labelComponent = createLabel("label_" + getOwner().getName(), "camera")
				.setFontPath("assets/font/roboto_regular.fnt").setFontScale(0.4f);
		labelComponent.setHexColor(new Color(1, 1, 1, 0.5f).toString());
		followerPos = labelComponent.getOwner().getComponent(PointComponent.class);
		owner = getOwner();
		draw = owner.getComponent(DrawableComponent.class);
	}

	@Override
	public void onUpdate(float delta) {
		if (draw != null) {
			labelComponent.setOffsetY(draw.getHeight() * owner.getComponent(PointComponent.class).getScale().y);
		}
		PointComponent ownerPos = owner.getComponent(PointComponent.class);
		followerPos.setRelativePosition(ownerPos.getPosition());
	}
}

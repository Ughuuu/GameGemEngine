package game.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.gemengine.component.ui.UIContainerComponent;
import com.gemengine.component.ui.UILabelComponent;
import com.gemengine.component.ui.UIStageComponent;
import com.gemengine.component.base.DrawableComponent;
import com.gemengine.component.base.PointComponent;
import com.gemengine.component.base.ScriptComponent;
import com.gemengine.component.twod.CameraComponent;
import com.gemengine.component.twod.LabelComponent;
import com.gemengine.component.twod.CameraComponent.ViewportType;
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
    float deltaTime = 0.0f;

	@Inject
	public OverlayScript(EntitySystem entitySystem, ComponentSystem componentSystem) {
		super(entitySystem, componentSystem);
	}

	void createCamera(String name) {
		val cam = entitySystem.create(name);
		cam.createComponent(PointComponent.class);
		cam.createComponent(CameraComponent.class).makeOrthographicCamera().setViewportType(ViewportType.Screen);
	}
	
	protected LabelComponent createLabel(String name, String cameraName) {
		Entity text = createEntity(name);
		LabelComponent label = text.createComponent(LabelComponent.class).setText(name).setHAlign(Align.center);
		text.createComponent(PointComponent.class);
		Entity camera = entitySystem.get(cameraName);
		if(camera == null){
			createCamera(cameraName);
			camera = entitySystem.get(cameraName);
		}
		camera.addChild(text);
		return label;
	}
	
	@Override
	public void onUpdate(float delta) {
		if (draw != null) {
			labelComponent.setOffsetY(draw.getHeight() * owner.getComponent(PointComponent.class).getScale().y);
		} else {
			owner = getOwner();
			draw = owner.getComponent(DrawableComponent.class);
			if(draw == null){
				return;
			}
			labelComponent = createLabel("overlay_" + getOwner().getName(), "camera").setText(getOwner().getName())
					.setFontPath("assets/font/roboto_regular.fnt").setFontScale(0.4f);
			labelComponent.setHexColor(new Color(1, 1, 1, 1).toString());
			followerPos = labelComponent.getOwner().getComponent(PointComponent.class);
		}
		PointComponent ownerPos = owner.getComponent(PointComponent.class);
		followerPos.setRelativePosition(ownerPos.getPosition());
		
		

        deltaTime += (Gdx.graphics.getDeltaTime() - deltaTime) * 0.1f;
        float msec = deltaTime * 1000.0f;
        float fps = 1.0f / deltaTime;
        labelComponent.setText(fps + " ");
	}
}

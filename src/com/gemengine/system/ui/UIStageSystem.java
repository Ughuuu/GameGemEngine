package com.gemengine.system.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gemengine.component.CameraComponent;
import com.gemengine.component.base.DrawableComponent;
import com.gemengine.component.ui.UIStageComponent;
import com.gemengine.component.ui.UIWidgetComponent;
import com.gemengine.entity.Entity;
import com.gemengine.system.CameraSystem;
import com.gemengine.system.CameraTrackerSystem;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.gemengine.system.base.ComponentTrackerSystem;
import com.gemengine.system.base.ConstructorSystem;
import com.gemengine.system.listener.ComponentTrackerListener;
import com.google.inject.Inject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UIStageSystem extends ConstructorSystem<Stage, UIStageComponent>
		implements ComponentTrackerListener<CameraComponent, DrawableComponent> {
	private final CameraSystem cameraSystem;
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private final ComponentSystem componentSystem;

	@Inject
	public UIStageSystem(ComponentSystem componentSystem, EntitySystem entitySystem, CameraSystem cameraSystem,
			CameraTrackerSystem cameraTrackerSystem) {
		super(componentSystem, entitySystem, true, 10, UIStageComponent.class);
		this.cameraSystem = cameraSystem;
		this.componentSystem = componentSystem;
		cameraTrackerSystem.addListener(this);
	}

	@Override
	protected Stage create(UIStageComponent component) {
		CameraComponent cameraComponent = cameraSystem.getWatchingCamera(component.getOwner());
		if (cameraComponent == null) {
			return null;
		}
		Viewport view = cameraSystem.getViewport(cameraComponent);
		if (view == null) {
			return null;
		}
		return new Stage(view, spriteBatch);
	}

	@Override
	protected void onEvent(String event, UIStageComponent notifier, Stage stage) {
		add(notifier);
	}

	@Override
	public void onFound(CameraComponent notifier, Entity tracker) {
		UIStageComponent uiStageComponent = tracker.getComponent(UIStageComponent.class);
		if (uiStageComponent != null) {
			add(uiStageComponent);
			componentSystem.notifyFrom("update", uiStageComponent);
		}
	}

	@Override
	public void onLost(Entity tracker) {
		UIStageComponent uiStageComponent = tracker.getComponent(UIStageComponent.class);
		if (uiStageComponent != null) {
			remove(uiStageComponent);
			componentSystem.notifyFrom("remove", uiStageComponent);
		}
	}
}

package com.gemengine.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gemengine.component.CameraComponent;
import com.gemengine.component.Component;
import com.gemengine.component.PointComponent;
import com.gemengine.system.ComponentSystem;

import lombok.Getter;

public abstract class UIWidgetComponent extends UIComponent {
	@Getter
	private float width;
	@Getter
	private float height;

	public UIWidgetComponent(ComponentSystem componentSystem) {
		super(componentSystem);
	}

	public abstract Actor getActor();

	@Override
	public <T extends Component> void onNotify(String arg0, T arg1) {
		PointComponent point = (PointComponent) arg1;
		if (!(point instanceof PointComponent)) {
			return;
		}
		Actor actor = getActor();
		if (actor == null)
			return;
		actor.setOrigin(align);
		actor.setPosition(point.getPosition().x, point.getPosition().y);
		actor.setZIndex(depth);
		if (point.getScale().isZero() && point.getRotation().isZero()) {
		} else {
			actor.setScale(point.getScale().x, point.getScale().y);
			actor.setRotation(point.getRotation().z);
		}
	}

	public void onStageFound(CameraComponent camera) {
	}

	public void onStageLost() {
	}

	public UIWidgetComponent setHeight(float height) {
		this.height = height;
		doNotify("size");
		return this;
	}

	public UIWidgetComponent setSize(float width, float height) {
		this.width = width;
		this.height = height;
		doNotify("size");
		return this;
	}

	public UIWidgetComponent setWidth(float width) {
		this.width = width;
		doNotify("size");
		return this;
	}
}

package com.gemengine.component.base;

import com.gemengine.component.SpriteComponent;
import com.gemengine.component.base.NotifyComponent;
import com.gemengine.system.ComponentSystem;

import lombok.Getter;

public abstract class DrawableComponent extends NotifyComponent {
	@Getter
	private float width;
	@Getter
	private float height;

	public DrawableComponent(ComponentSystem componentSystem) {
		super(componentSystem);
	}

	public DrawableComponent setHeight(float height) {
		this.height = height;
		doNotify("size");
		return this;
	}

	public DrawableComponent setSize(float width, float height) {
		this.height = height;
		this.width = width;
		doNotify("size");
		return this;
	}

	public DrawableComponent setWidth(float width) {
		this.width = width;
		doNotify("size");
		return this;
	}
}

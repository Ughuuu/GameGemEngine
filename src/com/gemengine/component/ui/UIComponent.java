package com.gemengine.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gemengine.component.base.DrawableComponent;
import com.gemengine.system.ComponentSystem;

import lombok.Getter;

public abstract class UIComponent extends DrawableComponent {
	@Getter
	protected int align = Align.center;

	public UIComponent(ComponentSystem componentSystem) {
		super(componentSystem);
	}

	@JsonIgnore
	public abstract Actor getActor();
}

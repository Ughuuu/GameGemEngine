package com.gemengine.component.ui;

import com.badlogic.gdx.utils.Align;
import com.gemengine.component.base.DrawableComponent;
import com.gemengine.system.ComponentSystem;

public abstract class UIComponent extends DrawableComponent {
	protected int depth = 0;

	protected int align = Align.center;

	public UIComponent(ComponentSystem componentSystem) {
		super(componentSystem);
	}
}

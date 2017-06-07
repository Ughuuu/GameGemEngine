package com.gemengine.system.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.gemengine.component.ui.UIComponent;
import com.gemengine.component.ui.UIContainerComponent;
import com.gemengine.component.ui.UILabelComponent;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.gemengine.system.base.ConstructorSystem;
import com.gemengine.system.base.SystemBase;
import com.google.inject.Inject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UIContainerSystem extends SystemBase {
	private Map<Integer, Container> containers = new HashMap<Integer, Container>();

	@Inject
	public UIContainerSystem(ComponentSystem componentSystem, EntitySystem entitySystem) {
		super(true, 10);
	}

	public void createContainer(UIContainerComponent containerComponent, UIComponent uiComponent) {
		Actor actor = uiComponent.getActor();
		if (actor == null) {
			return;
		}
		Container container = new Container(actor);
		container.setFillParent(true);
		container.align(containerComponent.getAlign());
		container.pad(containerComponent.getPadTop(), containerComponent.getPadLeft(),
				containerComponent.getPadBottom(), containerComponent.getPadRight());
		container.fill(containerComponent.getFillX(), containerComponent.getFillY());
		containers.put(containerComponent.getId(), container);
	}

	public Actor getContainer(UIContainerComponent uiContainerComponent) {
		return containers.get(uiContainerComponent.getId());
	}
}

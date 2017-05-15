package com.gemengine.system.ui;

import com.gemengine.component.ui.UIStageComponent;
import com.gemengine.component.ui.UIWidgetComponent;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.gemengine.system.base.ComponentTrackerSystem;
import com.gemengine.system.helper.ListenerHelper;
import com.google.inject.Inject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UIStageTrackerSystem extends ComponentTrackerSystem<UIStageComponent, UIWidgetComponent> {
	@Inject
	public UIStageTrackerSystem(ComponentSystem componentSystem, EntitySystem entitySystem) {
		super(componentSystem, entitySystem, ListenerHelper.createConfiguration(UIStageComponent.class), true, 12,
				UIStageComponent.class, UIWidgetComponent.class);
	}
}

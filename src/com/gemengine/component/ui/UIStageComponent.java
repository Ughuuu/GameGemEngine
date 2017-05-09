package com.gemengine.component.ui;

import com.gemengine.system.ComponentSystem;
import com.gemengine.system.ui.UIStageTrackerSystem;
import com.google.inject.Inject;

public class UIStageComponent extends UIComponent {
	private final UIStageTrackerSystem uiStageSystem;

	@Inject
	public UIStageComponent(ComponentSystem componentSystem, UIStageTrackerSystem uiStageSystem) {
		super(componentSystem);
		this.uiStageSystem = uiStageSystem;
	}

	@Override
	public String toString() {
		return "UIStageComponent []";
	}

}

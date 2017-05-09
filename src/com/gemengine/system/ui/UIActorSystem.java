package com.gemengine.system.ui;

import java.util.List;
import java.util.Set;

import com.gemengine.component.ui.UIStageComponent;
import com.gemengine.component.ui.UIWidgetComponent;
import com.gemengine.entity.Entity;
import com.gemengine.system.base.ComponentTrackerSystem;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.listener.ComponentTrackerListener;
import com.google.inject.Inject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UIActorSystem extends SystemBase implements ComponentTrackerListener<UIStageComponent, UIWidgetComponent> {
	@Inject
	public UIActorSystem(UIStageTrackerSystem uiStageTrackerSystem) {
		super(true, 6);
		uiStageTrackerSystem.addListener(this);
	}

	@Override
	public void onFound(ComponentTrackerSystem<UIStageComponent, UIWidgetComponent> issuingSystem,
			UIStageComponent notifier, Entity component) {
		List<UIWidgetComponent> widgets = component.getComponents(UIWidgetComponent.class);
		UIWidgetComponent widget;
		log.debug("onFound {} {}", notifier, widgets);
	}

	@Override
	public void onLost(ComponentTrackerSystem<UIStageComponent, UIWidgetComponent> issuingSystem, Entity component) {
		log.debug("onLost {}", component);
	}
}

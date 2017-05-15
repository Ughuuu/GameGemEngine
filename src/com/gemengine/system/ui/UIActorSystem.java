package com.gemengine.system.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
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
	private final UIStageSystem uiStageSystem;
	private final Map<Actor, Table> actorToTable = new HashMap<Actor, Table>();

	@Inject
	public UIActorSystem(UIStageTrackerSystem uiStageTrackerSystem, UIStageSystem uiStageSystem) {
		super(true, 100);
		this.uiStageSystem = uiStageSystem;
		uiStageTrackerSystem.addListener(this);
	}

	@Override
	public void onFound(UIStageComponent notifier, Entity component) {
		if (uiStageSystem.get(notifier) == null) {
			return;
		}
		List<UIWidgetComponent> widgets = component.getComponents(UIWidgetComponent.class);
		for (UIWidgetComponent widget : widgets) {
			Stage stage = new Stage();
			Table table = new Table();
			Actor actor = widget.getActor();
			LabelStyle style = new LabelStyle();
			style.font = new BitmapFont();
			actor= new Label("asd", style);
			//Stage stage = uiStageSystem.get(notifier);
			actorToTable.put(actor, table);
			table.add(actor).width(100f);
			table.setFillParent(true);
			table.setPosition(1, 1);
			stage.addActor(table);
			stage.setDebugAll(true);
			stage.act();
		}
	}

	@Override
	public void onLost(Entity component) {
	}

	@Override
	public Set<String> getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
}

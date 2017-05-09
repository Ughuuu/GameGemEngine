package com.gemengine.system;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.gemengine.component.base.ScriptComponent;
import com.gemengine.entity.Entity;
import com.gemengine.system.base.ComponentUpdaterSystem;
import com.gemengine.system.helper.ListenerHelper;
import com.google.inject.Inject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ScriptSystem extends ComponentUpdaterSystem {
	private final ComponentSystem componentSystem;

	@Inject
	public ScriptSystem(ComponentSystem componentSystem) {
		super(componentSystem, ListenerHelper.createConfiguration(ScriptComponent.class), true, 5);
		this.componentSystem = componentSystem;
	}

	@Override
	public void onNext(Entity ent) {
		List<ScriptComponent> scripts = componentSystem.get(ent, ScriptComponent.class);
		for (ScriptComponent script : scripts) {
			if (!script.isEnable()) {
				continue;
			}
			try {
				script.onUpdate(Gdx.graphics.getDeltaTime());
			} catch (Throwable t) {
				log.warn("Script System update", t);
			}
		}
	}
}

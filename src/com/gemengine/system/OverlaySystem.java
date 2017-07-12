package com.gemengine.system;

import com.gemengine.entity.Entity;
import com.gemengine.listener.EntityListener;
import com.gemengine.system.base.SystemBase;
import com.google.inject.Inject;

import game.component.OverlayScript;

public class OverlaySystem extends SystemBase implements EntityListener {
	private final ComponentSystem componentSystem;
	private final EntitySystem entitySystem;

	@Inject
	protected OverlaySystem(ComponentSystem componentSystem, EntitySystem entitySystem) {
		this.componentSystem = componentSystem;
		this.entitySystem = entitySystem;
		entitySystem.addEntityListener(this);
	}

	@Override
	public void onChange(EntityChangeType change, Entity e1, Entity e2) {
		switch(change){
		case ADD:
			if(!e1.getName().contains("overlay_")){
				//e1.createComponent(OverlayScript.class);
			}
			break;
		case DELETE:
			if(!e1.getName().contains("overlay_")){
				//entitySystem.delete("overlay_" + e1.getName());
			}
			break;
		case DEPARENTED:
			break;
		case PARENTED:
			break;
		default:
			break;
		}
	}
}

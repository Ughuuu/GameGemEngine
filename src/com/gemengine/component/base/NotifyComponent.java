package com.gemengine.component.base;

import com.gemengine.component.Component;
import com.gemengine.listener.EntityComponentListener;
import com.gemengine.system.ComponentSystem;

/**
 * A notifier component extends an ({@link OwnedComponent} and implements an
 * {@link EntityComponentListener} and adds itself as the listener. It listens
 * to changes made to other components on the same entity as its owner.
 * 
 * @author Dragos
 *
 */
public abstract class NotifyComponent extends OwnedComponent implements EntityComponentListener {
	public NotifyComponent(ComponentSystem componentSystem) {
		super(componentSystem);
		componentSystem.addEntityComponentListener(this);
	}

	@Override
	public <T extends Component> void onNotify(String arg0, T arg1) {
	}
}
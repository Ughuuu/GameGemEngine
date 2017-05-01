package engine.component;

import com.gemengine.component.Component;
import com.gemengine.entity.Entity;
import com.google.inject.Inject;

import engine.ComponentSystem;
import engine.EntitySystem;

public abstract class Script extends Component {
	private EntitySystem entitySystem;
	private ComponentSystem componentSystem;

	@Inject
	public Script(EntitySystem entitySystem, ComponentSystem componentSystem) {
		this.entitySystem = entitySystem;
		this.componentSystem = componentSystem;
	}

	protected Entity getOwner() {
		return componentSystem.getOwner(getId());
	}

	protected void removeThis() {
		entitySystem.remove(getOwner().getId());
	}

	protected Entity createEntity(String name) {
		return entitySystem.create(name);
	}

	protected Entity findEntity(String name) {
		return entitySystem.find(name);
	}

	protected Entity findEntity(int id) {
		return entitySystem.find(id);
	}

	protected void removeEntity(String name) {
		entitySystem.remove(name);
	}

	protected void removeEntity(int id) {
		entitySystem.remove(id);
	}

	public void onUpdate(float delta) {
	}
}

package engine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gemengine.entity.Entity;
import com.gemengine.system.base.SystemBase;
import com.google.inject.Inject;

public class EntitySystem extends SystemBase {
	private int _id = 0;
	private final Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	private final Map<String, Integer> entityNameToId = new HashMap<String, Integer>();
	private final Map<Integer, List<Integer>> entityToComponent = new HashMap<Integer, List<Integer>>();
	private final ComponentSystem componentSystem;

	@Inject
	public EntitySystem(ComponentSystem componentSystem) {
		super(true, 2);
		this.componentSystem = componentSystem;
	}

	public Entity add(String name) {
		if (entityNameToId.get(name) != null) {
			return null;
		}
		Entity ent = new Entity(_id, name);
		entityNameToId.put(name, ent.getId());
		entities.put(_id++, ent);
		return ent;
	}

	public Entity get(String name) {
		Integer id = entityNameToId.get(name);
		if (id == null) {
			return null;
		}
		return get(id);
	}

	public Entity get(int id) {
		return entities.get(id);
	}

	public void remove(String name) {
		Integer id = entityNameToId.remove(name);
		if (id == null) {
			return;
		}
		entities.remove(id);
		for (int componentId : entityToComponent.get(id)) {
			componentSystem.remove(componentId);
		}
	}

	public void remove(Integer id) {
		Entity ent = entities.remove(id);
		if (ent == null) {
			return;
		}
		entityNameToId.remove(ent.getName());
		for (int componentId : entityToComponent.get(id)) {
			componentSystem.remove(componentId);
		}
	}
}
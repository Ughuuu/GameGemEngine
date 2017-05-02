package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.gemengine.component.Component;
import com.gemengine.entity.Entity;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.manager.SystemManager;
import com.google.inject.Inject;

import engine.base.ComponentUpdaterSystem;
import lombok.val;

public class ComponentSystem extends TimedSystem {
	private final Map<Integer, Component> components = new HashMap<Integer, Component>();
	private final Map<Integer, Entity> componentToEntity = new HashMap<Integer, Entity>();
	private final Map<Integer, Map<String, Integer>> entityToTypeToComponents = new HashMap<Integer, Map<String, Integer>>();

	private final Map<Integer, String> componentToType = new HashMap<Integer, String>();
	private final Map<String, List<Integer>> typeToComponents = new HashMap<String, List<Integer>>();

	private final SystemManager systemManager;
	private final Set<ComponentUpdaterSystem> componentUpdaterSystems = new TreeSet<ComponentUpdaterSystem>();
	private final Map<Set<String>, Set<Entity>> configurationToEntities = new HashMap<Set<String>, Set<Entity>>();

	@Inject
	public ComponentSystem(SystemManager systemManager) {
		super(1600, true, 3);
		this.systemManager = systemManager;
	}

	private void getSupertypes(Class<?> cls, List<String> supertypes) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		supertypes.add(cls.getName());
		getSupertypes(cls.getSuperclass(), supertypes);
	}

	private <T extends Component> void addComponent(Entity ent, T component) {
		int id = component.getId();
		components.put(id, component);
		componentToEntity.put(id, ent);
		componentToType.put(id, component.getClass().getName());
	}

	private <T extends Component> void addType(Entity ent, T component) {
		int ownerId = ent.getId();
		int id = component.getId();
		List<String> supertypes = new ArrayList<String>();
		getSupertypes(component.getClass(), supertypes);
		Map<String, Integer> typeToComponentLimited = entityToTypeToComponents.get(ownerId);
		if (typeToComponentLimited == null) {
			typeToComponentLimited = new HashMap<String, Integer>();
			entityToTypeToComponents.put(ownerId, typeToComponentLimited);
		}
		typeToComponentLimited.put(component.getClass().getName(), id);
		for (String supertype : supertypes) {
			List<Integer> typeToComponent = typeToComponents.get(supertype);
			if (typeToComponent == null) {
				typeToComponent = new ArrayList<Integer>();
				typeToComponents.put(supertype, typeToComponent);
			}
			typeToComponent.add(id);
		}
	}

	public <T extends Component> T create(Entity ent, Class<T> type) {
		if (ent == null) {
			return null;
		}
		T component = systemManager.inject(type);
		if (component == null) {
			return null;
		}
		addComponent(ent, component);
		addType(ent, component);
		return component;
	}

	public void addComponentListener(ComponentUpdaterSystem componentListener) {
		componentUpdaterSystems.add(componentListener);
	}

	public void clear(Entity ent) {
		clear(ent.getId());
	}

	public void clear(int ownerId) {
		val entityToComponent = entityToTypeToComponents.get(ownerId);
		for (val key : entityToComponent.entrySet()) {
			Component component = components.remove(key.getValue());
			if (component == null) {
				continue;
			}
			int id = component.getId();
			removeFromTypeMap(id, component.getClass());
			componentToEntity.remove(id);
		}
		entityToTypeToComponents.remove(ownerId);
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T find(Entity ent, Class<T> type) {
		int ownerId = ent.getId();
		val typeToComponent = entityToTypeToComponents.get(ownerId);
		if (typeToComponent == null) {
			return null;
		}
		return (T) components.get(typeToComponent.get(type.getName()));
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T find(Entity ent, int id) {
		Entity owner = componentToEntity.get(id);
		if (owner == null || owner != ent) {
			return null;
		}
		Component component = components.get(id);
		if (component == null) {
			return null;
		}
		return (T) component;
	}

	public Entity getOwner(int id) {
		return componentToEntity.get(id);
	}

	@Override
	public void onInit() {
	}

	@Override
	public void onUpdate(float delta) {
		for (val updater : componentUpdaterSystems) {
			updater.onBeforeEntities();
		}
		for (val updater : componentUpdaterSystems) {
			val configuration = updater.getConfiguration();
			val entities = getEntitiesFromConfiguration(configuration);
			System.out.println(typeToComponents);
			for (val entity : entities) {
				updater.onNext(entity);
			}
		}
		for (val updater : componentUpdaterSystems) {
			updater.onAfterEntities();
		}
	}

	public <T extends Component> void remove(Entity ent, Class<T> type) {
		T component = find(ent, type);
		remove(ent, component.getId());
	}

	public void remove(Entity ent, int id) {
		Component component = components.remove(id);
		if (component == null) {
			return;
		}
		removeFromTypeMap(id, component.getClass());
		removeFromEntityMap(ent, id, component.getClass());
	}

	public <T extends Component> void remove(Entity ent, T component) {
		remove(ent, component.getId());
	}

	private Set<Entity> getEntitiesFromConfiguration(Set<String> configuration) {
		Set<Entity> entityCollection = configurationToEntities.get(configuration);
		if (entityCollection == null) {
			entityCollection = new HashSet<Entity>();
			configurationToEntities.put(configuration, entityCollection);
		}
		entityCollection.clear();
		for (String type : configuration) {
			List<Integer> components = typeToComponents.get(type);
			if (components == null) {
				continue;
			}
			for (Integer component : components) {
				entityCollection.add(componentToEntity.get(component));
			}
		}
		return entityCollection;
	}

	private void removeFromEntityMap(Entity ent, int id, Class<?> type) {
		int ownerId = ent.getId();
		val entityToComponent = entityToTypeToComponents.get(ownerId);
		entityToComponent.remove(type.getName());
		componentToEntity.remove(id);
	}

	private void removeFromTypeMap(int id, Class<?> type) {
		componentToType.remove(id);
		List<String> supertypes = new ArrayList<String>();
		getSupertypes(type, supertypes);
		for (String typeName : supertypes) {
			val typeToComponent = typeToComponents.get(typeName);
			if (typeToComponent.size() == 1) {
				typeToComponents.remove(typeName);
			} else {
				typeToComponent.remove(id);
			}
		}
	}
}

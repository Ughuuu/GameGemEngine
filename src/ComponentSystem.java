import java.util.HashMap;
import java.util.Map;

import com.gemengine.component.Component;
import com.gemengine.entity.Entity;
import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.manager.SystemManager;
import com.google.inject.Inject;

public class ComponentSystem extends TimedSystem {
	private int _id = 0;
	private final Map<String, Map<Integer, Component>> componentsByType = new HashMap<String, Map<Integer, Component>>();
	private final Map<Integer, String> idToType = new HashMap<Integer, String>();
	private final SystemManager systemManager;

	@Inject
	public ComponentSystem(SystemManager systemManager) {
		super(16, true, 3);
		this.systemManager = systemManager;
	}
	
	//public  getComponents(Class<?> type){
	//	return componentsByType.get(type.getName()).keySet();
	//}
	
	@Override
	public void onUpdate(float delta){
		System.out.println("a");
	}

	public <T extends Component> T add(Class<T> type) {
		String typeName = type.getName();
		T component = systemManager.inject(type);
		if (component != null) {
			return null;
		}
		idToType.put(_id, typeName);
		Map<Integer, Component> components = componentsByType.get(typeName);
		if (components == null) {
			components = new HashMap<Integer, Component>();
			componentsByType.put(typeName, components);
		}
		components.put(_id++, component);
		return component;
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T get(int id) {
		String typeName = idToType.get(id);
		if (typeName == null) {
			return null;
		}
		Map<Integer, Component> components = componentsByType.get(typeName);
		if (components == null) {
			return null;
		}
		return (T) components.get(id);
	}

	public void remove(int id) {
		String typeName = idToType.get(id);
		if (typeName == null) {
			return;
		}
		Map<Integer, Component> components = componentsByType.get(typeName);
		if (components == null) {
			return;
		}
		components.remove(id);
	}
}

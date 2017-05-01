package engine.base;

import java.util.HashSet;
import java.util.Set;

import com.gemengine.component.Component;
import com.gemengine.entity.Entity;
import com.gemengine.system.base.SystemBase;

import lombok.Getter;

public abstract class ComponentUpdaterSystem extends SystemBase {
	protected static Set<String> createConfiguration(Class<? extends Component>... components) {
		Set<String> configuration = new HashSet<String>();
		for (Class<? extends Component> component : components) {
			configuration.add(component.getName());
		}
		return configuration;
	}

	@Getter
	private final Set<String> configuration;

	protected ComponentUpdaterSystem() {
		this.configuration = new HashSet<String>();
	}

	protected ComponentUpdaterSystem(Set<String> configuration, boolean enable, int priority) {
		super(enable, priority);
		this.configuration = configuration;
	}

	public void onAfterEntities() {
	}

	public void onBeforeEntities() {
	}

	public void onNext(Entity ent) {
	}
}

package game.component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemengine.component.Component;
import com.gemengine.component.ui.UIContainerComponent;
import com.gemengine.component.ui.UITextAreaComponent;
import com.gemengine.component.base.DrawableComponent;
import com.gemengine.component.base.InputScriptComponent;
import com.gemengine.component.base.PointComponent;
import com.gemengine.component.base.ScriptComponent;
import com.gemengine.component.twod.LabelComponent;
import com.gemengine.entity.Entity;
import com.gemengine.listener.EntityListener;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.gemengine.system.base.SaveSystem;
import com.gemengine.system.manager.SystemManager;
import com.google.inject.Inject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ShowScript extends InputScriptComponent implements EntityListener {
	private UITextAreaComponent followerLabel;
	private UIContainerComponent followerContainer;
	private SaveSystem saveSystem;
	private int selected = -1;
	private Set<Integer> entities = new HashSet<Integer>();
	private boolean freeze = false;

	@Inject
	public ShowScript(EntitySystem entitySystem, ComponentSystem componentSystem, SaveSystem saveSystem,
			SystemManager systemManager) {
		super(entitySystem, componentSystem, systemManager);
		entitySystem.addEntityListener(this);
		this.saveSystem = saveSystem;
	}

	protected UITextAreaComponent createUITextArea(String name, String stageName) {
		Entity textArea = createEntity(name);
		UITextAreaComponent textAreaComponent = textArea.createComponent(UITextAreaComponent.class).setText(name);
		Entity container = createEntity("container_" + name);
		Entity stageEntity = findEntity(stageName);
		stageEntity.addChild(container);
		container.addChild(textArea);
		container.createComponent(UIContainerComponent.class).setAlign(Align.center);
		return textAreaComponent;
	}
	
	@Override
	public void onInit() {
		// createUILabel("test123", "stage");
		String name = getOwner().getName();
		if (name == null) {
			name = "";
		}
		getOwner().createComponent(PointComponent.class);
		followerLabel = createUITextArea(name, "stage");
		followerLabel.setWidth(640);
		followerContainer = followerLabel.getOwner().getParent().getComponent(UIContainerComponent.class);
		followerContainer.setAlign(Align.right);
		followerContainer.setFillY(1);
	}

	void findEntity() {
		float minLen = Float.MAX_VALUE;
		int minEnt = -1;
		for (Integer id : entities) {
			if (id == getOwner().getId()) {
				continue;
			}
			Entity ent = entitySystem.get(id);
			PointComponent pointComponent = ent.getComponent(PointComponent.class);
			Vector3 ownerPosition = getOwner().getComponent(PointComponent.class).getPosition();
			if (pointComponent != null) {
				float len = pointComponent.getPosition().sub(ownerPosition).len2();
				if (len < minLen) {
					minLen = len;
					minEnt = id;
				}
			}
		}
		if (minLen < 1000) {
			selected = minEnt;
			try {
				followerLabel.setText(saveSystem.getEntityAsString(entitySystem.get(selected)));
			} catch (JsonProcessingException e) {
				followerLabel.setText(e.getMessage());
			}
		}
	}

	void setEnableComponents(boolean enable, int id) {
		if (id == -1) {
			return;
		}
		Entity selectedEntity = entitySystem.get(id);
		List<ScriptComponent> selectedComponents = selectedEntity.getComponents(ScriptComponent.class);
		if (selectedComponents == null) {
			return;
		}
		for (ScriptComponent comp : selectedComponents) {
			comp.setEnable(enable);
		}
	}

	void freezeTime(boolean freeze) {
		for (Integer id : entities) {
			if (id == getOwner().getId()) {
				continue;
			}
			Entity ent = entitySystem.get(id);
			setEnableComponents(!freeze, id);
		}
	}

	@Override
	public void onUpdate(float delta) {
		if (selected != -1 && !freeze) {
			try {
				followerLabel.setText(saveSystem.getEntityAsString(entitySystem.get(selected)));
			} catch (JsonProcessingException e) {
				followerLabel.setText(e.getMessage());
			}
		}
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void onChange(EntityChangeType event, Entity e1, Entity e2) {
		switch (event) {
		case ADD:
			entities.add(e1.getId());
			break;
		case DELETE:
			entities.remove(e1.getId());
			break;
		case DEPARENTED:
			break;
		case PARENTED:
			break;
		default:
			break;
		}
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.RIGHT) {
			freeze = !freeze;
			freezeTime(freeze);
		} else if (button == Input.Buttons.LEFT) {
			Entity owner = getOwner();
			Vector3 pos = owner.getComponent(PointComponent.class).getRelativePosition();
			pos.x = Gdx.input.getX();
			pos.y = Gdx.graphics.getHeight() - Gdx.input.getY();
			owner.getComponent(PointComponent.class).setRelativePosition(pos);
			findEntity();
		}
		return false;
	}
}

package com.gemengine.component.base;

import com.badlogic.gdx.utils.Align;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gemengine.component.LabelComponent;
import com.gemengine.component.PointComponent;
import com.gemengine.component.ui.UIContainerComponent;
import com.gemengine.component.ui.UILabelComponent;
import com.gemengine.component.ui.UITextAreaComponent;
import com.gemengine.entity.Entity;
import com.gemengine.listener.EntityComponentListener;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;

import game.component.OverlayScript;

/**
 * Convenience class to be extended if you make a custom script component. It
 * extends owned component, so you can also query the owner, as well as some
 * helper functions that query the {@link EntitySystem} and the
 * {@link ComponentSystem}.
 * 
 * @author Dragos
 *
 */
public abstract class ScriptComponent extends OwnedComponent {
	@JsonIgnore
	protected final EntitySystem entitySystem;
	@JsonIgnore
	protected final ComponentSystem componentSystem;

	public ScriptComponent(EntitySystem entitySystem, ComponentSystem componentSystem) {
		super(componentSystem);
		this.entitySystem = entitySystem;
		this.componentSystem = componentSystem;
	}

	public void onInit() {
	}

	public void onUpdate(float delta) {
	}

	protected Entity createEntity(String name) {
		return entitySystem.create(name);
	}

	protected Entity findEntity(int id) {
		return entitySystem.get(id);
	}

	protected Entity findEntity(String name) {
		return entitySystem.get(name);
	}

	protected void removeEntity(int id) {
		entitySystem.delete(id);
	}

	protected void removeEntity(String name) {
		entitySystem.delete(name);
	}

	protected LabelComponent createLabel(String name, String cameraName) {
		Entity text = createEntity(name);
		LabelComponent label = text.createComponent(LabelComponent.class).setText(name).setHAlign(Align.center);
		text.createComponent(PointComponent.class);
		Entity camera = entitySystem.get(cameraName);
		camera.addChild(text);
		return label;
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

	protected UILabelComponent createUILabel(String name, String stageName) {
		Entity label = createEntity(name);
		UILabelComponent labelComponent = label.createComponent(UILabelComponent.class).setText(name);
		Entity container = createEntity("container_" + name);
		Entity stageEntity = findEntity(stageName);
		stageEntity.addChild(container);
		container.addChild(label);
		container.createComponent(UIContainerComponent.class).setAlign(Align.center);
		//labelComponent.setWidth(100).setHeight(100);
		return labelComponent;
	}
}

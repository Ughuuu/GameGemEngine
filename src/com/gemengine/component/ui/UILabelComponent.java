package com.gemengine.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.ui.UILabelSystem;
import com.google.inject.Inject;

import lombok.Getter;

public class UILabelComponent extends UIWidgetComponent {
	private final UILabelSystem labelSystem;

	@Getter
	private String font = "";

	@Getter
	private String text = "";

	@Inject
	public UILabelComponent(ComponentSystem componentSystem, UILabelSystem labelSystem) {
		super(componentSystem);
		this.labelSystem = labelSystem;
	}

	@Override
	@JsonIgnore
	public Actor getActor() {
		return labelSystem.get(this);
	}

	public UILabelComponent setFont(String fontName) {
		font = fontName;
		doNotify("font");
		return this;
	}

	public UILabelComponent setText(String text) {
		this.text = text;
		doNotify("text");
		return this;
	}

	@Override
	public String toString() {
		return "UILabelComponent [font=" + font + ", text=" + text + ", width()=" + getWidth() + ", height()="
				+ getHeight() + "]";
	}
}

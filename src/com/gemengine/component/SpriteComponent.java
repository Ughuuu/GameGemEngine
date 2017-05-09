package com.gemengine.component;

import com.gemengine.component.base.DrawableComponent;
import com.gemengine.system.ComponentSystem;
import com.google.inject.Inject;

import lombok.Getter;

public class SpriteComponent extends DrawableComponent {
	@Getter
	private float width;
	@Getter
	private float height;
	@Getter
	private String texturePath;

	@Inject
	public SpriteComponent(ComponentSystem componentSystem) {
		super(componentSystem);
	}

	@Override
	public <T extends Component> void onNotify(String arg0, T arg1) {
		if (!(arg1 instanceof PointComponent)) {
			return;
		}
		doNotify("point");
	}

	public SpriteComponent setHeight(float height) {
		this.height = height;
		doNotify("size");
		return this;
	}

	public SpriteComponent setSize(float width, float height) {
		this.height = height;
		this.width = width;
		doNotify("size");
		return this;
	}

	public SpriteComponent setTexturePath(String texturePath) {
		this.texturePath = texturePath;
		doNotify("texturePath");
		return this;
	}

	public SpriteComponent setWidth(float width) {
		this.width = width;
		doNotify("size");
		return this;
	}

	@Override
	public String toString() {
		return "SpriteComponent [width=" + width + ", height=" + height + ", texturePath=" + texturePath + "]";
	}
}

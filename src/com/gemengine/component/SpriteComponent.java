package com.gemengine.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gemengine.component.base.DrawableComponent;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ComponentSystem;
import com.google.inject.Inject;

import lombok.Getter;

/**
 * Component that contains a Sprite from libGDX. If you use this, you will also
 * need to parent it to a camera.
 * 
 * @author Dragos
 *
 */
public class SpriteComponent extends DrawableComponent {
	@Getter
	protected String texturePath = "";
	@JsonIgnore
	protected final AssetSystem assetSystem;

	@Inject
	public SpriteComponent(ComponentSystem componentSystem, AssetSystem assetSystem) {
		super(componentSystem);
		this.assetSystem = assetSystem;
	}

	@JsonIgnore
	public void texture(Sprite sprite) {
		Texture texture = assetSystem.getAsset(texturePath);
		if (texture == null) {
			return;
		}
		sprite.setTexture(texture);
		this.width = texture.getWidth();
		this.height = texture.getHeight();
	}

	@Override
	public <T extends Component> void onNotify(String arg0, T arg1) {
		if (!(arg1 instanceof PointComponent)) {
			return;
		}
		doNotify("point");
	}

	public SpriteComponent setTexturePath(String texturePath) {
		this.texturePath = texturePath;
		doNotify("texturePath");
		return this;
	}

	@Override
	public String toString() {
		return "SpriteComponent [width=" + getWidth() + ", height=" + getHeight() + ", texturePath=" + texturePath
				+ "]";
	}
}

package com.gemengine.system;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.gemengine.component.CameraComponent;
import com.gemengine.component.Component;
import com.gemengine.component.PointComponent;
import com.gemengine.component.SpriteComponent;
import com.gemengine.entity.Entity;
import com.gemengine.listener.ComponentListener;
import com.gemengine.system.base.ComponentUpdaterSystem;
import com.gemengine.system.helper.ListenerHelper;
import com.google.inject.Inject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SpriteSystem extends ComponentUpdaterSystem implements ComponentListener {
	private final ComponentSystem componentSystem;
	private final AssetSystem assetSystem;
	private final SpriteBatch spriteBatch;
	private final Map<Integer, Sprite> idToSprite = new HashMap<Integer, Sprite>();
	private final CameraSystem cameraSystem;
	private CameraComponent currentCamera;

	@Inject
	public SpriteSystem(ComponentSystem componentSystem, AssetSystem assetSystem, CameraSystem cameraSystem) {
		super(componentSystem, ListenerHelper.createConfiguration(SpriteComponent.class), true, 5);
		this.componentSystem = componentSystem;
		this.assetSystem = assetSystem;
		this.cameraSystem = cameraSystem;
		spriteBatch = new SpriteBatch();
		componentSystem.addComponentListener(this);
	}

	@Override
	public void onAfterEntities() {
		spriteBatch.end();
	}

	@Override
	public void onBeforeEntities() {
		spriteBatch.begin();
		spriteBatch.setTransformMatrix(new Matrix4());
		if (currentCamera != null) {
			spriteBatch.setProjectionMatrix(cameraSystem.getCamera(currentCamera).combined);
		}
	}

	@Override
	public <T extends Component> void onChange(ComponentChangeType arg0, T arg1) {

	}

	@Override
	public void onNext(Entity ent) {
		SpriteComponent spriteComponent = ent.getComponent(SpriteComponent.class);
		if (!spriteComponent.isEnable()) {
			return;
		}
		Sprite sprite = getSprite(spriteComponent);
		if (sprite.getTexture() == null) {
			String texturePath = spriteComponent.getTexturePath();
			log.warn("Sprite System cannot load texture {}", texturePath);
			spriteComponent.setEnable(false);
			return;
		}
		CameraComponent newCamera = cameraSystem.getWatchingCamera(ent);
		if (newCamera == null) {
			spriteComponent.setEnable(false);
			return;
		}
		if (currentCamera != newCamera) {
			currentCamera = newCamera;
			spriteBatch.setProjectionMatrix(cameraSystem.getCamera(currentCamera).combined);
		}
		// log.debug(ent);
		sprite.draw(spriteBatch);
	}

	@Override
	public <T extends Component> void onNotify(String event, T component) {
		SpriteComponent spriteComponent = (SpriteComponent) component;
		switch (event) {
		case "texturePath":
			setTexture(spriteComponent);
			break;
		case "point":
			setPoint(spriteComponent, spriteComponent.getOwner().getComponent(PointComponent.class));
			break;
		default:
			setSize(spriteComponent, spriteComponent.getWidth(), spriteComponent.getHeight());
			break;
		}
		component.setEnable(true);
	}

	@Override
	public <T extends Component> void onTypeChange(Class<T> arg0) {

	}

	public void setPoint(SpriteComponent component, PointComponent point) {
		Sprite spr = getSprite(component);
		spr.setPosition(point.getPosition().x - spr.getWidth() / 2, point.getPosition().y - spr.getHeight() / 2);
		spr.setScale(point.getScale().x, point.getScale().y);
		spr.setRotation(point.getRotation().z);
	}

	public void setSize(SpriteComponent component, float width, float height) {
		String texturePath = component.getTexturePath();
		Texture texture = assetSystem.getAsset(texturePath);
		if (texture == null) {
			log.warn("Sprite System cannot load texture {}", texturePath);
			component.setEnable(false);
			return;
		}
		component.setEnable(true);
		Sprite spr = getSprite(component);
		spr.setOrigin(width / 2, height / 2);
		spr.setSize(width, height);
		PointComponent point = componentSystem.getOwner(component.getId()).getComponent(PointComponent.class);
		if (point != null) {
			setPoint(component, point);
		}
	}

	public void setTexture(SpriteComponent component) {
		String texturePath = component.getTexturePath();
		Texture texture = assetSystem.getAsset(texturePath);
		if (texture == null) {
			log.warn("Sprite System wrong texture path {}", texturePath);
			component.setEnable(false);
			return;
		}
		component.setEnable(true);
		Sprite sprite = getSprite(component);
		sprite.setTexture(texture);
		sprite.setRegion(0, 0, texture.getWidth(), texture.getHeight());
		setSize(component, texture.getWidth(), texture.getHeight());
	}

	private Sprite getSprite(SpriteComponent component) {
		int textureId = component.getId();
		Sprite sprite = idToSprite.get(textureId);
		if (sprite == null) {
			sprite = new Sprite();
			idToSprite.put(textureId, sprite);
		}
		return sprite;
	}
}

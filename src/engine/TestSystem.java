package engine;

import com.gemengine.system.base.SystemBase;
import com.gemengine.system.base.TimedSystem;

public class TestSystem extends TimedSystem{

	@Override
	public void onInit(){
		System.out.println("start");
	}

	@Override
	public void onUpdate(float delta){
		System.out.println("1");
	}
}

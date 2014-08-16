package gameObjects.WEAPONS;

import gameObjects.Data;

import java.util.ArrayList;
import java.util.List;

/**		Builder Class: The whole purpose of this class is to Build the Weapons and Return them.		*/
public class WeaponBuilder {
	
	private List<Weapon> weaponry = new ArrayList<>();
	
	public WeaponBuilder(){
		buildWeapons();
	}

	private void buildWeapons() {
		for (int i = 0; i < 6; i++){	weaponry.add(new Weapon(Data.weaponNames[i]));	}
	}
	
	public List<Weapon> getWeapons(){
		return weaponry;
	}

}

//import java.awt.Image;

public class Object {

	// Image img;
	String img;
	int x;
	int y;
	boolean walkable;
	boolean isTrap;
	boolean isSightable;
	int type;

	public Object() {

	}

	public Object(int x2, int y2, String string) {
		x = x2;
		y = y2;
		img = string;
	}

	public Object(int x2, int y2) {
		x = x2;
		y = y2;

	}

	public void setTrap() {
		char s = this.img.charAt(0);

		switch (s) {
		case 'H':
			img = "HouseTrap.png";
			break;
		default:
			img = "IceTrap.png";
		}
		isTrap = true;
		isSightable = true;

	}

	public void triggerTrap() {
		char s = this.img.charAt(0);

		switch (s) {
		case 'H':
			img = "HouseHole.png";
			break;
		default:
			img = "IceHole.png";
		}
		walkable = false;
		isSightable = true;
		isTrap = false;

	}

	// / ------- TYPES ------ //
	public void Stone(String area) {
		img = area + "Stone.png";
		walkable = false;
		isTrap = false;
		isSightable = false;
		type = 0;
	}

	public void Floor(String area) {
		img = area + "Walkable.png";
		walkable = true;
		isTrap = false;
		isSightable = true;
		type = 0;
	}

	public void Floor1(String area) {
		img = area + "Walkable1.png";
		walkable = false;
		isTrap = false;
		isSightable = true;
		type = 0;
	}

	public void Tree(String area) {
		img = area + "Tree.png";
		walkable = true;
		isTrap = false;
		isSightable = false;
		type = 3;
	}

	public void Presents(String area) {
		img = area + "Presents.png";
		walkable = true;
		isTrap = false;
		isSightable = false;
		type = 3;
	}

	public void Fence(String area) {
		img = area + "Fence.png";
		walkable = false;
		isTrap = false;
		isSightable = true;
		type = 0;
	}

	public void Coke(String area) {
		img = area + "Coke.png";
		walkable = false;
		isTrap = false;
		isSightable = false;
		type = 0;
	}

	public void Finish(String area) {
		img = area + "Finish.png";
		walkable = true;
		isTrap = false;
		isSightable = false;
		type = 1;
	}

	public boolean rapeChristmas() {
		if (this.type == 3){
			char s = this.img.charAt(0);
			
			switch (s) {
			case 'H':
				img = "HouseWalkable.png";
				break;
			default:
				img = "IceWalkable.png";
			}
			walkable = true;
			isSightable = true;
			isTrap = false;
			type = 0;
			return true;
		} else {
			return false;
		}
		
		
	}
}

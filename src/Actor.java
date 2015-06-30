
import java.awt.Point;

import java.util.Random;

public class Actor {
	public int animState;
	public String img;
	public int x;
	public int y;
	public boolean alive;
	public int type;
	public int view;
	int timer;
	int steps;
	public Actor(int xTile, int yTile, int _type) {
		type = _type;
		x = xTile * 32;
		y = yTile * 32;
		alive = true;
		switch (type) {
		case 0: img = "GrinchR.png"; break;
		default:img = "actorR.png"; break;
		
			
		}
		

	}

	
	public boolean move(int direction, World level) {
		view = direction;

		if (direction == 0) {
			switch (type) {
			case 0: img = "GrinchU.png"; break;
			default:img = "actorU.png"; break;
				
			}
			if (this.x % 32 < 9) {

				if ((this.y % 32 == 0)
						&& (level.blocks[this.x / 32][this.y / 32 - 1].walkable == false)) {
					// level.blocks[this.x / 32][this.y / 32].img =
					// "col.png";
				} else {
					this.y -= 2;
					return true;
				}

			} else {
				if ((this.y % 32 == 0)
						&& ((level.blocks[this.x / 32][this.y / 32 - 1].walkable == false) || (level.blocks[this.x / 32 + 1][this.y / 32 - 1].walkable == false))) {
					// level.blocks[this.x / 32][this.y / 32].img =
					// "col.png";
					// level.blocks[this.x / 32 + 1][this.y / 32].img =
					// "col.png";

				} else {
					this.y -= 2;
					return true;
				}

			}
		} else if (direction == 1) {
			
			switch (type) {
			case 0: img = "GrinchD.png"; break;
			default:img = "actorD.png"; break;
				
			}
			if (this.y % 32 == 2) {
				if (level.blocks[this.x / 32][this.y / 32 + 1].walkable == false) {
					// level.blocks[this.x / 32][this.y / 32 + 2].img
					// ="col.png";
				} else if ((this.x % 32 > 9)
						&& (level.blocks[this.x / 32 + 1][this.y / 32 + 1].walkable == false)) {
					// level.blocks[this.x / 32][this.y / 32 + 2].img
					// ="col.png";
					// level.blocks[this.x / 32 + 1][this.y / 32 +
					// 2].img ="col.png";

				} else {
					this.y += 2;
					return true;
				}

			} else {
				this.y += 2;
				return true;
			}

		} else if (direction == 2) {
			switch (type) {
			case 0: img = "GrinchL.png"; break;
			default:img = "actorL.png"; break;
				
			}
			if (this.x % 32 == 0) {
				if ((level.blocks[this.x / 32 - 1][this.y / 32].walkable == false)) {

				} else if ((this.y % 32 > 2)
						&& level.blocks[this.x / 32 - 1][this.y / 32 + 1].walkable == false) {

				} else {
					this.x -= 2;
					return true;
				}
			} else {
				this.x -= 2;
				return true;
			}

		} else if (direction == 3) {
			switch (type) {
			case 0: img = "GrinchR.png"; break;
			default:img = "actorR.png"; break;
				
			}
			if (this.x % 32 == 8) {
				if ((level.blocks[this.x / 32 + 1][this.y / 32].walkable == false)) {

				} else if ((this.y % 32 > 2)
						&& level.blocks[this.x / 32 + 1][this.y / 32 + 1].walkable == false) {

				} else {
					this.x += 2;
					return true;
				}
			} else {
				this.x += 2;
				return true;
			}

		}
		return false;
	}

	
	public Point center(){
		return new Point(this.x + 12, this.y+15);
	}
	public void moveRandom(World level) {
		boolean walk;
		walk = this.move(this.view, level);
		if (walk == false) {
			Random random = new Random();
			this.view = random.nextInt(4);
		}
	}
	
	public void moveRandom(World level, int time, int nextState) {
		
		boolean walk = this.move(this.view, level);
		if (walk == false) {
			Random random = new Random();
			this.view = random.nextInt(4);
		}
		steps ++;
		if (steps >= time){
			this.type = nextState;
		}
	}
	
	
	public void guardHorizontal(World level){
		
		timer++;
		
		if (timer == 5) {
			boolean walk = move(view,level);
			if (!walk){
				//switch (view){
					/*case 2: this.view = 3; break;
					case 3: this.view = 2; break;
					default: this.view = 2; break; */
					Random random = new Random();
					view = random.nextInt(4);
				//}
			}
			timer = 0;
		
		}
		
	}

	public void moveTo(Point center, World level) {
		if ((center.y/32 < this.center().y/32) && this.move(0, level));
		else if ((center.y/32 > this.center().y/32) && this.move(1, level));
		else if ((center.x/32 < this.center().x/32) && this.move(2, level));
		else  this.move(3, level);
		
	}

}

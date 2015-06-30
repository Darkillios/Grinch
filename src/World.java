import java.awt.Point;
import java.util.Random;


public class World {
	public Object[][] blocks;
	public int width;
	public int height;
	public Actor[] enemy;
	public Point[] trigger;
	
	public World(int x, int y){
		width = x;
		height = y;
		blocks = new Object[x][y];
		

	}

	
	public void randomWorld(String area){
		Random random = new Random();
		int x = width;
		int y = height;
		for (int i = 1; i < x-1; i++){
			for (int j = 1; j < y-1; j++){
				int dice =random.nextInt(100);
				if (dice < 66){
					blocks[i][j] = new Object(x,y);
					blocks[i][j].Floor(area);
				} else if (( dice == 66) || (dice == 67)) {
					blocks[i][j] = new Object(x,y);
					blocks[i][j].Tree(area);
				} else if (( dice == 68) || (dice == 69)) {
					blocks[i][j] = new Object(x,y);
					blocks[i][j].Presents(area);
				} else if (dice == 70){
					blocks[i][j] = new Object(x,y);
					blocks[i][j].Coke(area);
				} else {
					blocks[i][j] = new Object(x,y);
					blocks[i][j].Stone(area);
				}
				
			}
		}
		
		for (int i = 0; i < x; i++){
			blocks[i][0] = new Object(x,y);
			blocks[i][0].Stone(area);			
			blocks[i][y-1] = new Object(x,y);
			blocks[i][y-1].Stone(area);
		}
		
		for (int i = 0; i < y; i++){
			blocks[0][i] = new Object(x,y);
			blocks[0][i].Stone(area);
			blocks[x-1][i] = new Object(x,y);
			blocks[x-1][i].Stone(area);
		} 
	}
	
	
	public void patternWorld(String area){
		Pattern[][] patterns= new Pattern[4][4];
		
		
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				patterns[i][j] = new Pattern();
			}
		}
		
		
		/*for (int i = 1; i < 4; i++){
			for (int j = 1; j < 4; i++){
				while(! patterns[i][j-1].isCompatible(patterns[i][j], 2)){
					patterns[i][j] = new Pattern();
				}
			}
		} */
		
		
		
		//-------------------------------------
		Random random = new Random();
		
		for (int i = 0; i < 4; i++){
			for (int j = 0; j < 4; j++){
				for (int k = 0; k < 5; k++){
					for (int l = 0; l <5; l++){
						int r = random.nextInt(100);
						
						if (patterns[i][j].blocks[k][l] == true) {
							if (r < 86) {
								blocks[5*i + k][5*j + l] = new Object();
								blocks[5*i + k][5*j + l].Floor(area);
							} else if (r < 93){
								blocks[5*i + k][5*j + l] = new Object();
								blocks[5*i + k][5*j + l].Presents(area);
							} else {
								blocks[5*i + k][5*j + l] = new Object();
								blocks[5*i + k][5*j + l].Tree(area);
							}
						} else {
							if (r < 80) {
								blocks[5*i + k][5*j + l] = new Object();
								blocks[5*i + k][5*j + l].Stone(area);
							} else if (r < 90){
								blocks[5*i + k][5*j + l] = new Object();
								blocks[5*i + k][5*j + l].Fence(area);
							} else {
								blocks[5*i + k][5*j + l] = new Object();
								blocks[5*i + k][5*j + l].Coke(area);
							}
						}
					}
				}
			}
		}
		
		int x = width;
		int y = height;
		for (int i = 0; i < x; i++){
			blocks[i][0] = new Object(x,y);
			blocks[i][0].Stone(area);			
			blocks[i][y-1] = new Object(x,y);
			blocks[i][y-1].Stone(area);
		}
		
		for (int i = 0; i < y; i++){
			blocks[0][i] = new Object(x,y);
			blocks[0][i].Stone(area);
			blocks[x-1][i] = new Object(x,y);
			blocks[x-1][i].Stone(area);
		}
		
		int fx = 19, fy = 19;
		
		while (blocks[fx][fy].walkable == false){
			if (fx < fy) fy--;
			else fx--;
		}
		
		blocks[fx][fy].Finish(area);
		
	}
	public void emptyWorld(){
		int x = width;
		int y = height;
		for (int i = 1; i < x-1; i++){
			for (int j = 1; j < y-1; j++){
				
					blocks[i][j] = new Object(x,y,"IceWalkable.png");
					blocks[i][j].walkable = true;
				
				
			}
		}
		
		for (int i = 0; i < x; i++){
			blocks[i][0] = new Object(x,y,"IceStone.png");
			blocks[i][0].walkable = false;
			blocks[i][y-1] = new Object(x,y,"IceStone.png");
			blocks[i][y-1].walkable = false;
		}
		
		for (int i = 0; i < y; i++){
			blocks[0][i] = new Object(x,y,"IceStone.png");
			blocks[0][i].walkable = false;
			blocks[x-1][i] = new Object(x,y,"IceStone.png");
			blocks[x-1][i].walkable = false;
		} 
	}


	
}

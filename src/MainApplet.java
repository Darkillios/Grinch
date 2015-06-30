import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
//import java.nio.channels.ClosedByInterruptException;
import java.util.Random;

import javax.swing.JFrame;

//import com.oracle.xmlns.internal.webservices.jaxws_databinding.ExistingAnnotationsType;

public class MainApplet extends Applet implements Runnable, KeyListener {

	private static final long serialVersionUID = 3843509969844875059L;
	// ---BUFFER ETC----------//
	private Image dbImage;
	private Graphics dbg;
	MediaTracker tr;

	AudioClip music = null;
	// ---SOURCE-------------//
	Actor player;

	World level;
	Image img;

	// ---VARIABLES-------//
	int menuState = 0;
	int gameState = 0;
	String s = "";
	int gameTime = 0;
	int traps = 3;
	int alarm = 0;
	boolean seen = false;
	Point viewer;
	Random random = new Random();
	String worldType = "Ice";
	int enemyCount = 10;
	int rapedThings = 0;
	URL codeBase;

	// ---MAIN FUNCTIONS--//

	public MainApplet() {

	}

	public void init() {

		setBackground(Color.BLACK);
		addKeyListener(this);
		try {
			music = getAudioClip(ClassLoader.getSystemResource("Surface.mid"));
		} finally {

		}
		Thread th = new Thread(this);
		th.start();

		// level.emptyWorld();

	}

	public void start() {
		music.stop();
		try {
			music = getAudioClip(ClassLoader.getSystemResource("Surface.mid"));
		} finally {

		}
		music.loop();
		gameTime = 0;
		rapedThings = 0;
		traps = 3;
		alarm = 0;
		seen = false;
		level = new World(20, 20);
		// level.randomWorld("Ice");

		level.patternWorld(worldType);

		player = new Actor(1, 1, 0);
		while (level.blocks[player.x / 32][player.y / 32].walkable == false) {
			if (player.x > player.y) {
				player.x += 32;
			} else {
				player.y += 32;
			}
		}
		// level.blocks[5][5].Floor("Ice");
		player.timer = 50;

		level.enemy = new Actor[enemyCount];
		/*
		 * level.enemy[0] = new Actor(4, 8, 1); level.enemy[1] = new Actor(9, 3,
		 * 1); level.enemy[2] = new Actor(10, 9, 1); level.enemy[3] = new
		 * Actor(8, 16, 1);
		 */

		// level.enemy.view = random.nextInt(4);
		for (int i = 0; i < level.enemy.length; i++) {
			level.enemy[i] = new Actor(random.nextInt(18) + 1,
					random.nextInt(18) + 1, 1);
			level.enemy[i].view = random.nextInt(4);
			// level.enemy[i].type = 1;
		}

	}

	public void run() {
		
		while (true) {

			if (gameState == 1) {
				gameTime++;
				seen = false;

				switch (level.blocks[player.center().x / 32][player.center().y / 32].type) {
				case 1:
					gameState = 43;
					music.stop();
					music = getAudioClip(ClassLoader
							.getSystemResource("Win.mid"));
					music.play();
					break;
				case 2:
					player.timer = 0;
					break;
				default:
				}

				if (player.timer <= 0) {
					gameState = 42;
					music.stop();
					music = getAudioClip(ClassLoader
							.getSystemResource("Fail.mid"));
					music.play();
				}
				for (int i = 0; i < level.enemy.length; i++) {
					if (level.enemy[i].alive) {
						if ((level.enemy[i].center().x / 32 == player.center().x / 32)
								&& (level.enemy[i].center().y / 32 == player
										.center().y / 32)) {
							player.timer--;

						}
						switch (level.enemy[i].type) {
						case 1:
							level.enemy[i].guardHorizontal(level);
							break;
						case 2:
							level.enemy[i].moveRandom(level, 70, 1);
							break;
						case 3:
						default:
						}
						level.enemy[i].guardHorizontal(level);
						if (playerCaught(level.enemy[i], player)) {
							seen = true;
							viewer = level.enemy[i].center();
							level.enemy[i].moveTo(player.center(), level);
							level.enemy[i].timer = level.enemy[i].view;
							level.enemy[i].type = 3;

						} else if (level.enemy[i].type == 3) {
							seen = false;
							level.enemy[i].type = 2;
							level.enemy[i].steps = 0;
							for (int j = 0; i < 3; i++) {
								s = "Blah" + j;
								if (seen == false) {
									level.enemy[i].view = j;
									if (playerCaught(level.enemy[i], player)) {

										seen = true;
										viewer = level.enemy[i].center();
										level.enemy[i].moveTo(player.center(),
												level);

									}
								}

							}
							level.enemy[i].view = level.enemy[i].timer;

						}
						if (trapped(level.enemy[i]))
							level.enemy[i].alive = false;

					}

				}
				if (seen)
					player.timer--;
				s = "" + player.timer;
			}

			repaint();

			try {

				Thread.sleep(20);
			} catch (InterruptedException ex) {
				// do nothing
			}
		}

	}

	public void paint(Graphics g) {
		tr = new MediaTracker(this);
		g.setFont(new Font("Verdana", 20, 20));
		
		
		
		if ((gameState ==  1)) {
			//music.stop();
		for (int i = 0; i < level.width; i++) {
				for (int j = 0; j < level.height; j++) {
					if (level.blocks[i][j] != null) {

						img = getImage(ClassLoader.getSystemResource(level.blocks[i][j].img));
						g.drawImage(img, i * 32, j * 32, this);
					}
				}

			}

			// player.img = ;
		
			g.drawImage(getImage(ClassLoader.getSystemResource(player.img)),
					player.x, player.y, this);
			
			for (int i = 0; i < level.enemy.length; i++) {
				if (level.enemy[i].alive)
					g.drawImage(getImage(ClassLoader
							.getSystemResource(level.enemy[i].img)),
							level.enemy[i].x, level.enemy[i].y, this);
			}

			if (seen == true) {
				g.setColor(Color.red);
				g.drawLine(viewer.x, viewer.y, player.center().x,
						player.center().y);
				if (gameTime % 8 == 0) {
					g.fillRect(0, 0, (level.width) * 32, (level.height) * 32);
				}

			}
			g.setColor(Color.BLACK);

			g.drawImage(
					getImage(ClassLoader.getSystemResource("TrapCount.png")),
					450, 0, this);
			g.drawString("" + traps, 460, 30);

			g.drawImage(
					getImage(ClassLoader.getSystemResource("ThieveryCount.png")),
					300, 0, this);
			g.drawString("" + rapedThings, 400, 30);

			if (gameState > 20) {
				g.drawString("PAUSE", this.getWidth() / 2, this.getHeight() / 2);
			}  
			
		} else if (gameState == 0) {
			g.setColor(Color.black);

			// g.drawRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(getImage(ClassLoader.getSystemResource("Menu.png")), 0,
					0, this);
			g.setColor(Color.BLACK);

			// g.drawString("PRESS 'SPACE' TO BEGIN", 320,
			// 320);

			g.drawImage(getImage(ClassLoader.getSystemResource("Button.png")),
					70, 180, this);
			g.drawImage(getImage(ClassLoader.getSystemResource("Button.png")),
					70, 280, this);
			g.drawImage(getImage(ClassLoader.getSystemResource("Button.png")),
					70, 380, this);
			g.drawImage(getImage(ClassLoader.getSystemResource("Start.png")),
					70, 180, this);
			g.drawImage(getImage(ClassLoader.getSystemResource("Options.png")),
					70, 280, this);
			g.drawImage(
					getImage(ClassLoader.getSystemResource("Tutorial.png")),
					70, 380, this);

			switch (menuState) {
			case 0:
				g.drawImage(
						getImage(ClassLoader.getSystemResource("Pointer.png")),
						20, 210, this);
				break;
			case 1:
				g.drawImage(
						getImage(ClassLoader.getSystemResource("Pointer.png")),
						20, 310, this);
				break;
			case 2:
				g.drawImage(
						getImage(ClassLoader.getSystemResource("Pointer.png")),
						20, 410, this);
				break;
			default:
				break;
			}
		} else if (gameState == -1) {
			g.drawImage(getImage(ClassLoader.getSystemResource("Menu.png")), 0,
					0, this);
			g.drawImage(getImage(ClassLoader.getSystemResource("Button.png")),
					70, 180, this);
			g.drawImage(getImage(ClassLoader.getSystemResource("Button.png")),
					70, 280, this);
			g.drawImage(getImage(ClassLoader.getSystemResource("Button.png")),
					70, 380, this);
			g.drawImage(
					getImage(ClassLoader.getSystemResource("EnemyCount.png")),
					70, 180, this);
			g.setColor(Color.black);

			g.drawString("" + enemyCount, 148, 250);
			if (worldType.equals("Ice"))
				g.drawImage(
						getImage(ClassLoader.getSystemResource("WorldIce.png")),
						70, 280, this);
			else
				g.drawImage(getImage(ClassLoader
						.getSystemResource("WorldHouse.png")), 70, 280, this);
			g.drawImage(getImage(ClassLoader.getSystemResource("Back.png")),
					70, 380, this);
			switch (menuState) {
			case 0:
				g.drawImage(
						getImage(ClassLoader.getSystemResource("Pointer.png")),
						20, 210, this);
				break;
			case 1:
				g.drawImage(
						getImage(ClassLoader.getSystemResource("Pointer.png")),
						20, 310, this);
				break;
			case 2:
				g.drawImage(
						getImage(ClassLoader.getSystemResource("Pointer.png")),
						20, 410, this);
				break;
			default:
				break;
			}

		} else if (gameState == -2) {
			g.drawImage(getImage(ClassLoader.getSystemResource("Menu2.png")),
					0, 0, this);
			g.setColor(new Color(230, 255, 230));
			g.setFont(new Font("Verdana", 10, 10));
			if (menuState == 0) {
				g.drawString(
						" \" Ihr glaubt ich bin der Böse? Ihr irrt euch. \" ",

						200, 210);
				g.drawString(
						"Der Grinch stellt sich der weihnachtlichen Herausforderung. Helft ihm dabei, ein Labyrinth voller",
						30, 230);
				g.drawString(
						"blutrünstiger Weihnachtselfen zu durchqueren. Doch Vorsicht! Sollten Santas kleine Helferlein den",
						30, 250);
				g.drawString(
						"armen Grinch erwischen, wird es ungemütlich für ihn!",
						30, 270);
			} else if (menuState == 1) {
				g.drawString("Objekte:", 200, 210);

				g.drawImage(
						getImage(ClassLoader.getSystemResource("GrinchD.png")),
						69, 230, this);
				g.drawString("Der Grinch. Steuert ihn durch das Labyrinth.",
						100, 250);
				g.drawImage(
						getImage(ClassLoader.getSystemResource("ActorD.png")),
						69, 266, this);
				g.drawString("Weihnachtselfen, der Inbegriff des Bösen.", 100,
						280);
				g.drawString(
						"Geht diesen Kreaturen um jeden Preis aus dem Weg!",
						100, 290);
				g.drawImage(
						getImage(ClassLoader.getSystemResource("IceFinish.png")),
						64, 300, this);
				g.drawString(
						"Das Ziel befindet sich unten rechts und ist mit einer roten Flagge gekennzeichnet",
						100, 320);
				g.drawImage(getImage(ClassLoader
						.getSystemResource("IcePresents.png")), 30, 340, this);
				g.drawImage(
						getImage(ClassLoader.getSystemResource("IceTree.png")),
						64, 340, this);
				g.drawString(
						"Geschenke und Weihnachtsbäume dienen dem Grinch als Deckung",
						100, 355);
				g.drawString("vor den bösen Blicken der Elfen", 100, 365);
			} else if (menuState == 2) {
				g.drawString("Steuerung ", 200, 210);

				g.drawString("Pfeiltasten", 100, 250);
				g.drawString("Bewegt euch in die entsprechende Richtung", 190,
						250);
				g.drawString("STRG", 100, 270);
				g.drawString(
						"Platziert tödliche Fallen, um den Elfen Einhalt zu gebieten",
						190, 270);
				g.drawString("Leertaste", 100, 290);
				g.drawString(
						"Stehlt Geschenke und Bäume - Nieder mit dem Weihnachtswahn!",
						190, 290);
				g.drawString("Escape", 100, 310);
				g.drawString("Kehrt zum Hauptmenü zurück", 190, 310);
				g.drawString("R", 100, 330);
				g.drawString("Startet ein neues Labyrinth", 190, 330);
				g.drawString(
						"Anmerkung: Aus Abneigung gegen den Trend des Casual Gamings wurde die Pause-Funktion entfernt!",
						35, 400);
			}
			g.drawString(
					"Links/Rechts: Umblättern   |    Esc: Zurück zum Hauptmenü",
					190, 550);
			// g.drawImage(getImage(ClassLoader.getSystemResource("Button.png"),
			// 208, 500,
			// this);
			// g.drawImage(getImage(ClassLoader.getSystemResource("Back.png"),
			// 208, 500, this);

		} else if (gameState == 42) {
			// g.setColor(Color.black);
			// g.fillRect(0, 0, this.getWidth(), this.getHeight());
			// g.setColor(Color.white);
			// g.drawString("SANTA'S LITTLE HELPERS HAVE CRUSHED YOU",
			// this.getWidth() / 2, this.getHeight() / 2);
			for (int i = 0; i < level.width; i++) {
				for (int j = 0; j < level.height; j++) {
					if (level.blocks[i][j] != null) {

						img = getImage(ClassLoader
								.getSystemResource(level.blocks[i][j].img));
						g.drawImage(img, i * 32, j * 32, this);
					}
				}

			}
			g.drawImage(getImage(ClassLoader.getSystemResource("Button2.png")),
					208, 270, this);
			g.drawImage(getImage(ClassLoader.getSystemResource("Fail.png")),
					208, 270, this);
		} else if (gameState == 43) {
			for (int i = 0; i < level.width; i++) {
				for (int j = 0; j < level.height; j++) {
					if (level.blocks[i][j] != null) {

						img = getImage(ClassLoader
								.getSystemResource(level.blocks[i][j].img));
						g.drawImage(img, i * 32, j * 32, this);
					}
				}

			}
			g.drawImage(getImage(ClassLoader.getSystemResource("Button.png")),
					208, 270, this);
			g.drawImage(getImage(ClassLoader.getSystemResource("Win.png")),
					208, 270, this);
		}
		
		

	}

	// ---AUX FUNCTIONS----//
	boolean playerCaught(Actor watcher, Actor watched) {

		int watcherXTile = watcher.x / 32;
		int watcherYTile = watcher.y / 32;
		int playerXTile = (watched.x + 12) / 32;
		int playerYTile = (watched.y + 15) / 32;
		if ((watcherXTile == playerXTile) && (watcherYTile == playerYTile)) {
			return true;
		}

		switch (watcher.view) {
		case 0: {
			if (playerXTile != watcherXTile)
				return false;
			else if (playerYTile > watcherYTile)
				return false;
			else {
				for (int i = watcherYTile; i >= playerYTile; i--) {
					// level.blocks[watcherXTile][i].img = "col.png";
					if (level.blocks[watcherXTile][i].isSightable == false) {
						return false;
					}
				}
				return true;
			}
		}
		case 1: {
			if (playerXTile != watcherXTile)
				return false;
			else if (playerYTile < watcherYTile)
				return false;
			else {
				for (int i = watcherYTile; i <= playerYTile; i++) {
					// level.blocks[watcherXTile][i].img = "col.png";
					if (level.blocks[watcherXTile][i].isSightable == false) {
						return false;
					}
				}
				return true;
			}
		}
		case 3: {
			if (playerYTile != watcherYTile)
				return false;
			else if (playerXTile < watcherXTile)
				return false;
			else {
				for (int i = watcherXTile; i <= playerXTile; i++) {
					// level.blocks[i][watcherYTile].img = "col.png";
					if (level.blocks[i][watcherYTile].isSightable == false) {
						return false;
					}
				}
				return true;
			}
		}
		case 2: {
			if (playerYTile != watcherYTile)
				return false;
			else if (playerXTile > watcherXTile)
				return false;
			else {
				for (int i = watcherXTile; i >= playerXTile; i--) {
					// level.blocks[i][watcherYTile].img = "col.png";
					if (level.blocks[i][watcherYTile].isSightable == false) {
						return false;
					}
				}
				return true;
			}
		}
		default:
			return false;

		}

	}

	public boolean trapped(Actor enemy) {

		if (level.blocks[(enemy.x + 12) / 32][(enemy.y + 15) / 32].isTrap) {
			level.blocks[(enemy.x + 12) / 32][(enemy.y + 15) / 32]
					.triggerTrap();
			return true;
		}
		return false;
	}

	// ------KEYS---------------------------------------------------//

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_R) {
			music.stop();
			start();
			gameState = 1;
			repaint();
			//Thread th = new Thread(this);
			//th.start();
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = 0;
			menuState = 0;
			// repaint();
			// Thread th = new Thread(this);
			// th.start();
		}
		if (gameState == 1) {

			if (!seen) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					player.move(0, level);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					player.move(1, level);
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					player.move(2, level);
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					player.move(3, level);
				} else if ((e.getKeyCode() == KeyEvent.VK_CONTROL)
						&& (traps > 0)) {
					level.blocks[(player.x + 12) / 32][(player.y + 15) / 32]
							.setTrap();
					traps--;

				} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {

					if (level.blocks[(player.x + 12) / 32][(player.y + 15) / 32]
							.rapeChristmas()) {
						rapedThings++;
					}

				}
			}

		} else if (gameState == 0) {

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				menuState = (menuState + 2) % 3;
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				menuState = (menuState + 1) % 3;
				repaint();
			}

			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				switch (menuState) {
				case 0:
					
					gameState = 1;
					repaint();
					start();
				
					
					break;
				case 1:
					gameState = -1;
					menuState = 0;
					break;
				default:
					gameState = -2;
					menuState = 0;
				}

				repaint();
				/*
				 * if ((gameState == 0)){ Thread th = new Thread(this);
				 * th.start(); }
				 */

			}

		} else if (gameState == -1) {

			if (e.getKeyCode() == KeyEvent.VK_UP) {
				menuState = (menuState + 2) % 3;
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				menuState = (menuState + 1) % 3;
				repaint();
			} else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				switch (menuState) {
				case 0:

					enemyCount = (enemyCount + 1) % 16;
					if (enemyCount == 0)
						enemyCount = 5;
					break;
				case 1:
					if (worldType.equals("Ice"))
						worldType = "House";
					else
						worldType = "Ice";
					break;
				default:
					gameState = 0;
					menuState = 0;
				}

				repaint();
				/*
				 * if ((gameState == 0)){ Thread th = new Thread(this);
				 * th.start(); }
				 */
			}
		} else if (gameState == -2) {

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				menuState = (menuState + 2) % 3;

			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				menuState = (menuState + 1) % 3;

			}

			repaint();
			/*
			 * if ((gameState == 0)){ Thread th = new Thread(this); th.start();
			 * }
			 */

		} else if ((gameState == 42) || (gameState == 43)) {

			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				music.stop();
				try {
					music = getAudioClip(ClassLoader
							.getSystemResource("Surface.mid"));
				} finally {

				}
				music.loop();
				gameState = 0;

			}
			repaint();
			/*
			 * if ((gameState == 0)){ Thread th = new Thread(this); th.start();
			 * }
			 */

		}

	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	// -------NO NEED TO CHANGE----------------------//
	public void update(Graphics g) {
		// Initialisierung des DoubleBuffers
		if (dbImage == null) {
			dbImage = createImage(this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics();
		}

		// Bildschirm im Hintergrund löschen
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

		// Auf gelöschten Hintergrund Vordergrund zeichnen
		dbg.setColor(getForeground());
		paint(dbg);

		// Nun fertig gezeichnetes Bild Offscreen auf dem richtigen Bildschirm
		// anzeigen
		g.drawImage(dbImage, 0, 0, this);

	}

	public void stop() {

	}

	public void destroy() {

	}

	public static void main(String args[]) {
		// Zuerst wird eine Instanz der Klasse angelegt
		// und anschließend der Name der Programm-Art ausgegeben.
		MainApplet applet = new MainApplet();

		Frame frame = new Frame();
		frame.setBounds(20, 20, 640, 640);
		frame.add(applet);
		applet.init();
		// String name = new java.util.Scanner( System.in ).nextLine();
		frame.setVisible(true);

		// universalprogram.whoamI("applicaton");
	}
}

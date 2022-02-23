package JavaBrickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener{
	// Properties
	private boolean play = false; // don't want to start the game playing until we tell it to
	private int score = 0;
	
	private int totalBricks = 21; // can be changed later for increased difficulty
	
	private Timer timer;
	private int delay = 5;
	
	private int playerX = 310; // Starting position of our player slider
	
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGenerator map;
	
	public Gameplay()
	{
		map = new MapGenerator(3, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g)
	{
		// Add background
		g.setColor(Color.black);
		g.fillRect(1, 1, 692, 592);
		
		// Set borders
		g.setColor(Color.yellow);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		// Draw map of bricks
		map.draw((Graphics2D)g);
		
		// Create the paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		// Create the ball
		g.setColor(Color.yellow);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		// Draw Scores
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("" + score, 590, 30);
		
		// Handle game over when the ball passes the player paddle
		if (ballposY > 570)
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			
			// Display game over string
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over! \nScore: " + score, 190, 300);
			
			// Start over string
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart...", 230, 350);
		}
		
		// Handle game over when all bricks are gone, player wins
		if (totalBricks == 0)
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			
			// Display game over string
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You win! \nScore: " + score, 190, 300);
			
			// Start over string
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart...", 230, 350);
		}
	}

	// Interface Implementations
	@Override
	public void actionPerformed(ActionEvent e) { // The main loop for the game
		timer.start();
		if (play)
		{
			// Detecting when the ball hits the paddle
			if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8)))
			{
				ballYdir = -ballYdir;
			}
			
			// Handle collision of ball and the bricks
			A: for (int i = 0; i < map.map.length; i++)
			{
				for (int j = 0; j < map.map[0].length; j++)
				{
					if (map.map[i][j] > 0)
					{
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						// create the collision around the brick
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						// Check if it intersects
						if (ballRect.intersects(brickRect))
						{
							map.setBrickValue(0, i, j);
							totalBricks--;
							score += 5;
							
							// If the ball hits the brick, change it's direction
							if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width)
							{
								// Move the ball to the opposite direction
								ballXdir = -ballXdir;
							}
							else
							{
								ballYdir = -ballYdir;
							}
							break A; // Breaks us out of the whole loop, looks for the label A and breaks the loop at that level
						}
					}
				}
			}
			
			// Moving the ball
			ballposX += ballXdir;
			ballposY += ballYdir;
			if (ballposX < 0)
			{
				ballXdir = -ballXdir;
			}
			if (ballposY < 0)
			{
				ballYdir = -ballYdir;
			}
			if (ballposX > 670)
			{
				ballXdir = -ballXdir;
			}
		}
		
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {} // Don't need, but can't remove because of the interface

	@Override
	public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				if (playerX >= 600)
				{
					playerX = 600; // Don't let the player go outside the border
				}
				else
				{
					moveRight();
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT)
			{
				if (playerX < 10)
				{
					playerX = 10;
				}
				else
				{
					moveLeft();
				}
			}
			
			// Restart game after game over when pressing Enter
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				if (!play)
				{
					play = true;
					ballposX = 120;
					ballposY = 350;
					ballXdir = -1;
					ballYdir = -2;
					playerX = 310;
					score = 0;
					totalBricks = 21;
					map = new MapGenerator(3, 7);
					
					repaint();
				}
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {} // Don't need, but can't remove because of the interface
	
	// Movement Methods
	public void moveRight()
	{
		play = true;
		playerX += 20;
	}
	
	public void moveLeft()
	{
		play = true;
		playerX -= 20;
	}
	
}

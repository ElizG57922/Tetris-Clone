package tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;


// -- inherit from JFrame so that we can add custom functionality
public class Tetris extends JFrame {
	private GraphicPanel gp;
	private ControlPanel cp;
	private Gameboard gb;

	GraphicPanel getGraphicPanel()
	{
		return gp;
	}
	
	public Tetris(int height, int width) {
		setTitle("Tetris");
		setSize(width, height);
		setLocationRelativeTo(null);
		
		BufferedImage bi = null;		
		File inputfile = new File("./Smile.png");
		try {
			bi = ImageIO.read(inputfile);
		} catch (IOException e) {
			System.out.println("error reading logo, exiting");
			System.exit(0);
		}
		setIconImage(bi);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// -- set the layout manager and add items
		//    5, 5 is the border around the edges of the areas
		setLayout(new BorderLayout(5, 5));
        
		gb = new Gameboard();
		
		gp = new GraphicPanel();
		this.add(gp, BorderLayout.CENTER);

		cp = new ControlPanel();
		this.add(cp, BorderLayout.EAST);

		this.setVisible(true);
	}
    
	// -- Inner class for control panel, inherits from JPanel
	public class GraphicPanel extends JPanel {
		private Timer animationTimer = null;	
		private Graphics2D g2d;
		private MyKeyboardListener kl;        
	
        public Timer getAnimationTimer() {
			return animationTimer;
		}

		public GraphicPanel() {
			super();
			setLayout(new GridLayout(15, 5));
   			this.setBackground(Color.gray);
            
	    	kl = new MyKeyboardListener();//request focus here
			this.add(kl);
			kl.setFocusable(true);
			kl.requestFocus();
			   
			//    First parameter is the delay in mSec, second is the ActionListener
			animationTimer = new Timer(500, 
					new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							cp.getNPP().repaint();
							cp.getHPP().repaint();
							cp.setTextField("" + gb.getRowsCleared());
							gp.repaint();
							gb.update();
					}
				}
				);
		}

		public Dimension getPreferredSize() {
			return new Dimension(50, 50);
		}
		
		public void paint(Graphics g) {
			super.paintComponent(g);
            
			g2d = (Graphics2D)g;
			int h = this.getHeight();
			int w = this.getWidth();
			int r = gb.getRows();
			int c = gb.getCols();
			//draw grid
			g2d.setColor(Color.BLACK);
			for(int i = 1; i < c; i ++) {
				g2d.drawLine(0, (int)(i * (h / (float)c)), w, (int)(i * (h / (float)c)));
			}
			for(int i = 1; i < r; i ++) {
				g2d.drawLine((int)(i * (w / (float)r)), 0, (int)(i * (w / (float)r)), h);
			}
			
			//draw pieces
			g2d.setColor(Color.WHITE);
			if(gb.getCurrentPiece() != null)
    			gb.getCurrentPiece().draw(g2d, w, h, r , c, 0, 0);

			gb.drawFrozenPieces(g2d, w, h);
		}
		public MyKeyboardListener getKL() {
			return kl;
		}
		
		public class MyKeyboardListener extends JPanel{
			public MyKeyboardListener() {
				addKeyListener(new KeyListener() {
					public void keyPressed(KeyEvent e) {
						switch (e.getKeyCode()) {
						case KeyEvent.VK_RIGHT:
							if(gb.getCurrentPiece() != null)
				    			gb.getCurrentPiece().moveRight();
							break;
						case KeyEvent.VK_LEFT:
							if(gb.getCurrentPiece() != null)
	    						gb.getCurrentPiece().moveLeft();
							break;
						case KeyEvent.VK_UP:
							if(gb.getCurrentPiece() != null)
    							gb.getCurrentPiece().rotate(0);
							break;
						case KeyEvent.VK_DOWN:
							if(gb.getCurrentPiece() != null)
    							gb.getCurrentPiece().rotate(1);
							break;
						case KeyEvent.VK_SPACE:
							if(gb.getCurrentPiece() != null) {
    							while(gb.getCurrentPiece().canDescend()) {
        							gb.getCurrentPiece().tryDescend();
    							}
							}
							break;
						}
					}

					@Override
					public void keyTyped(KeyEvent e) {
					}

					@Override
					public void keyReleased(KeyEvent e) {
					}
				});
			}
		}
	}
	
	// -- Inner class for control panel, also inherits from JPanel
	public class ControlPanel extends JPanel {
        private JPanel buttonsPanel = new JPanel(new GridLayout(8, 1));
        private NextPiecePanel npp;
        private HoldPiecePanel hpp;
		private JTextField textfield;
		
		public ControlPanel() {
			setLayout(new GridLayout(3, 1, 4, 4));
			
            npp = new NextPiecePanel();
            hpp = new HoldPiecePanel();
			add(buttonsPanel);
			add(npp);
			add(hpp);
			
			// JButtons
			JButton startButton = new JButton("Go");
			startButton.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							gp.getAnimationTimer().start();
							gp.getKL().requestFocus();
						}
					}
				);
			
			JButton pauseButton = new JButton("Pause");
			pauseButton.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							gp.getAnimationTimer().stop();
							gp.getKL().requestFocus();
						}
					}
				);
			
			JButton hold = new JButton("Hold");
			hold.addActionListener(
					new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							gb.holdAPiece();
							gp.getKL().requestFocus();
						}
					}
				);
			
			textfield = new JTextField("0");
			textfield.setEditable(false);
			JLabel rowsCleared = new JLabel("Rows Cleared:");
			
			// add all controls
			buttonsPanel.add(startButton);
			buttonsPanel.add(pauseButton);
			buttonsPanel.add(hold);
			buttonsPanel.add(rowsCleared);
			buttonsPanel.add(textfield);
		}
		public String getTextField() {
			return textfield.getText();
		}
		
		public void setTextField(String t) {
			textfield.setText("" + t);
		}
		
		public NextPiecePanel getNPP() {
			return npp;
		}
		public HoldPiecePanel getHPP() {
			return hpp;
		}
		
		public Dimension getPreferredSize() {
			return new Dimension(100, 500);
		}
		
		public class NextPiecePanel extends JPanel {
			public NextPiecePanel() {
        		super();
    	    	setLayout(new GridLayout(2, 1, 3, 3));
    			this.setBackground(Color.gray);
        	}
    		public Dimension getPreferredSize() {
    			return new Dimension(50, 50);
    		}
    		
    		public void paint(Graphics g) {
    			super.paintComponent(g);
                
    			Graphics2D g2d = (Graphics2D)g;
    			g2d.drawString("Next Piece:", 0, 10);
    			int h = this.getHeight();
    			int w = this.getWidth();
    			//draw next piece
    			gb.getNextPiece().draw(g2d, w, h, 5, 6, 10, 20);
    		}
        }
		public class HoldPiecePanel extends JPanel {
			public HoldPiecePanel() {
        		super();
    	    	setLayout(new GridLayout(2, 1, 3, 3));
    			this.setBackground(Color.gray);
        	}
    		public Dimension getPreferredSize() {
    			return new Dimension(50, 50);
    		}
    		
    		public void paint(Graphics g) {
    			super.paintComponent(g);
                
    			Graphics2D g2d = (Graphics2D)g;
    			g2d.drawString("Hold:", 0, 10);
    			int h = this.getHeight();
    			int w = this.getWidth();
    			//draw held piece
    			if(gb.getHoldPiece() != null) {
        			gb.getHoldPiece().draw(g2d, w, h, 5, 6, 10, 20);
    			}
    		}
        }
	}
	
	public static void main(String[] args) {
		// instantiate an anonymous object
		new Tetris(600, 400);
	}
}
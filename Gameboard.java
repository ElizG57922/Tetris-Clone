package tetris;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

public class Gameboard {
	private boolean board[][];
	private Piece currentPiece;
	private Piece nextPiece;
	private Piece holdPiece;
	private int rowsCleared;
	
	public Gameboard() {
		board = new boolean[20][10];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = false;
			}
		}
		currentPiece = new Piece();
		nextPiece = new Piece();
		rowsCleared = 0;
	}
	public Gameboard(int r, int c) {
		board = new boolean[r][c];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = false;
			}
		}
		currentPiece = new Piece();
		nextPiece = new Piece();
	}
	public int getRowsCleared() {
		return rowsCleared;
	}
	public int getRows() {
		return board[0].length;
	}
	public int getCols() {
		return board.length;
	}
	public boolean isFilled(int r, int c) {
		return board[r][c];
	}
    public void tryRemoveRow() { //check if bottom row is filled
    	boolean removeable = true;
    	for (int i = board.length - 1; i >= 0; i--) {
        	for (int j = 0; j < board[i].length; j++) {
        		if(!board[i][j]) {
    	    		removeable = false;
    	    		break;
    		    }
        	}
        	if(removeable) {
        		rowsCleared++;
        		for(int j = 0; j < board[i].length; j++) {
        			for(int c = i; c >= 1; c--) {
        				board[c][j] = board[c - 1][j];
        			}
        			board[0][j] = false;
        		}
        	}
        	removeable = true;
    	}
    }
    
    public void holdAPiece() {
    	if(holdPiece == null) {
        	holdPiece = new Piece(currentPiece.getType());
        	currentPiece = nextPiece;
    	    nextPiece = new Piece();
    	}
    }
    public void update() {
    	if(currentPiece == null) {
    		if(holdPiece != null) {
    			currentPiece = new Piece(holdPiece.getType());
    			holdPiece = null;
    		}
    		else {
        		currentPiece = nextPiece;
        		nextPiece = new Piece();
    		}
    	}
    	currentPiece.tryDescend();
    	if(!currentPiece.canDescend) {
    		currentPiece = null;
    	}
    	tryRemoveRow();
    }
    
    public Piece getCurrentPiece() {
    	return currentPiece;
    }
    public Piece getNextPiece() {
    	return nextPiece;
    }
    public Piece getHoldPiece() {
    	return holdPiece;
    }
    
    public void drawFrozenPieces(Graphics2D g2d, int w, int h) {
		int c = getCols(); // for the sake of readability
		int r = getRows();
    	for(int i = 0; i < c; i ++) { //frozen pieces
			for(int j = 0; j < r; j ++) {
				if(isFilled(i, j)) {					
					g2d.setColor(Color.RED);
					g2d.fillRect((int)(j * (w / (float)r)), (int)(i * (h / (float)c)), w / r, h / c);

					g2d.setColor(Color.BLUE);
					g2d.drawRect((int)(j * (w / (float)r)), (int)(i * (h / (float)c)), w / r, h / c);

			
				}
			}
			
		}
    }
    
    public class Piece {
    	private Point s1;
    	private Point s2;
    	private Point s3;
    	private Point s4;
    	private int type;
    	private boolean canDescend;
    	
    	public Piece() {
    		canDescend = true;
    		Random rn = new Random();
    		type = rn.nextInt(7);
    		
    		switch(type) {
    		case 0:
    			s1 = new Point(0, 0);    // xxxx
    			s2 = new Point(1, 0);
    			s3 = new Point(2, 0);
    			s4 = new Point(3, 0);
    			break;
    		case 1:
    			s1 = new Point(1, 0);    //  x
    			s2 = new Point(1, 1);    // xx
    			s3 = new Point(0, 1);    // x
    			s4 = new Point(0, 2);
    			break;
    		case 2:
    			s1 = new Point(0, 0);    // x
    			s2 = new Point(0, 1);    // xx
    			s3 = new Point(1, 1);    //  x
    			s4 = new Point(1, 2);
    			break;
    		case 3:
    			s1 = new Point(0, 0);    // xx
    			s2 = new Point(1, 0);    //  x
    			s3 = new Point(1, 1);    //  x
    			s4 = new Point(1, 2);
    			break;
    		case 4:
    			s1 = new Point(0, 0);    // xx
    			s2 = new Point(1, 0);    // x
    			s3 = new Point(0, 1);    // x
    			s4 = new Point(0, 2);
    			break;
    		case 5:
    			s1 = new Point(0, 0);    // xxx
    			s2 = new Point(1, 0);    //  x
    			s3 = new Point(2, 0);
    			s4 = new Point(1, 1);
    			break;
    		case 6:
    			s1 = new Point(0, 0);    // xx
    			s2 = new Point(0, 1);    // xx
    			s3 = new Point(1, 1);
    			s4 = new Point(1, 0);
    			break;
    		}
    	}
    		public Piece(int type) {
        		canDescend = true;
        		this.type = type;
        		
        		switch(type) {
        		case 0:
        			s1 = new Point(0, 0);    // xxxx
        			s2 = new Point(1, 0);
        			s3 = new Point(2, 0);
        			s4 = new Point(3, 0);
        			break;
        		case 1:
        			s1 = new Point(1, 0);    //  x
        			s2 = new Point(1, 1);    // xx
        			s3 = new Point(0, 1);    // x
        			s4 = new Point(0, 2);
        			break;
        		case 2:
        			s1 = new Point(0, 0);    // x
        			s2 = new Point(0, 1);    // xx
        			s3 = new Point(1, 1);    //  x
        			s4 = new Point(1, 2);
        			break;
        		case 3:
        			s1 = new Point(0, 0);    // xx
        			s2 = new Point(1, 0);    //  x
        			s3 = new Point(1, 1);    //  x
        			s4 = new Point(1, 2);
        			break;
        		case 4:
        			s1 = new Point(0, 0);    // xx
        			s2 = new Point(1, 0);    // x
        			s3 = new Point(0, 1);    // x
        			s4 = new Point(0, 2);
        			break;
        		case 5:
        			s1 = new Point(0, 0);    // xxx
        			s2 = new Point(1, 0);    //  x
        			s3 = new Point(2, 0);
        			s4 = new Point(1, 1);
        			break;
        		case 6:
        			s1 = new Point(0, 0);    // xx
        			s2 = new Point(0, 1);    // xx
        			s3 = new Point(1, 1);
        			s4 = new Point(1, 0);
        			break;
        		}
    	}
    	public void tryDescend() {
    		if((s1.y < board.length - 1          
    			&& s2.y < board.length - 1
    			&& s3.y < board.length - 1
    			&& s4.y < board.length - 1) 
    			
    			&& (!(isFilled(s1.y + 1, s1.x)       
    			|| isFilled(s2.y + 1, s2.x)
    			|| isFilled(s3.y + 1, s3.x)
    			|| isFilled(s4.y + 1, s4.x)))) {
    			//move down
    			s1.y += 1;
    			s2.y += 1;
       			s3.y += 1;
    			s4.y += 1;
    		}
    		else { //freeze
    			board[s1.y][s1.x] = true;
    			board[s2.y][s2.x] = true;
    			board[s3.y][s3.x] = true;
    			board[s4.y][s4.x] = true;
    			canDescend = false;
    		}
    	}
    	public int getType() {
    		return type;
    	}
    	public void moveLeft() {
    		if((s1.x > 0          
    			&& s2.x > 0
    			&& s3.x > 0
    			&& s4.x > 0) 
    			
    			&& (!(isFilled(s1.y, s1.x - 1)       
    			|| isFilled(s2.y, s2.x - 1)
    			|| isFilled(s3.y, s3.x - 1)
    			|| isFilled(s4.y, s4.x - 1)))) {
   		    	    s1.x -= 1;
   			        s2.x -= 1;
   	    		    s3.x -= 1;
   		    	    s4.x -= 1;
    		}
    	}
        public void moveRight() {
       		if((s1.x < board[0].length - 1          
       			&& s2.x < board[0].length - 1
       			&& s3.x < board[0].length - 1
       			&& s4.x < board[0].length - 1) 
       			
       			&& (!(isFilled(s1.y, s1.x + 1)       
       			|| isFilled(s2.y, s2.x + 1)
       			|| isFilled(s3.y, s3.x + 1)
       			|| isFilled(s4.y, s4.x + 1)))) {
       			    s1.x += 1;
       			    s2.x += 1;
       			    s3.x += 1;
       			    s4.x += 1;
        	}
    	}
    	public void rotate(int direction) {
    		int pivotY = (s1.y + s2.y + s3.y + s4.y) / 4;
    		int pivotX = (s1.x + s2.x + s3.x + s4.x) / 4;
    		
    		s1.y -= pivotY;
    		s2.y -= pivotY;
    		s3.y -= pivotY;
    		s4.y -= pivotY;
    		s1.x -= pivotX;
    		s2.x -= pivotX;
    		s3.x -= pivotX;
    		s4.x -= pivotX;
            
    		if(direction == 0) {
    	    	int s1y = -s1.x + pivotY;
        		int s2y = -s2.x + pivotY;
    	    	int s3y = -s3.x + pivotY;
        		int s4y = -s4.x + pivotY;
        		s1.x = s1.y + pivotX;
    		    s2.x = s2.y + pivotX;
    	    	s3.x = s3.y + pivotX;
        		s4.x = s4.y + pivotX;
        		s1.y = s1y;
    		    s2.y = s2y;
    	    	s3.y = s3y;
        		s4.y = s4y;
    		}
    		else {
    			int s1x = -s1.y + pivotX;
        		int s2x = -s2.y + pivotX;
        		int s3x = -s3.y + pivotX;
        		int s4x = -s4.y + pivotX;
        		s1.y = s1.x + pivotY;
        		s2.y = s2.x + pivotY;
        		s3.y = s3.x + pivotY;
        		s4.y = s4.x + pivotY;
        		s1.x = s1x;
        		s2.x = s2x;
        		s3.x = s3x;
        		s4.x = s4x;
    		}
    		while(s1.x < 0          
    			|| s2.x < 0
    			|| s3.x < 0
    			|| s4.x < 0) {
    			s1.x ++;
    			s2.x ++;
    			s3.x ++;
    			s4.x ++;
    		}
    		while(s1.x > board[0].length - 1          
           			|| s2.x > board[0].length - 1
           			|| s3.x > board[0].length - 1
           			|| s4.x > board[0].length - 1) {
    			s1.x --;
    			s2.x --;
    			s3.x --;
    			s4.x --;
    		}
    	}
    	public void drop() {
    		if((s1.y < board.length - 1          
           			&& s2.y < board.length - 1
           			&& s3.y < board.length - 1
           			&& s4.y < board.length - 1) 
           			
           			&& (!(isFilled(s1.y + 1, s1.x)       
           			|| isFilled(s2.y + 1, s2.x)
           			|| isFilled(s3.y + 1, s3.x)
           			|| isFilled(s4.y + 1, s4.x)))) {
           			    s1.y += 1;
           			    s2.y += 1;
           			    s3.y += 1;
           			    s4.y += 1;
            	}
    	}
		public void draw(Graphics2D g2d, int w, int h, int r, int c, int hOffset, int vOffset) {
			g2d.setColor(Color.RED);			
			g2d.fillRect((int)(s1.x * (w / (float)r)) + hOffset, (int)(s1.y * (h / (float)c)) + vOffset, w / r, h / c);
			g2d.fillRect((int)(s2.x * (w / (float)r)) + hOffset, (int)(s2.y * (h / (float)c)) + vOffset, w / r, h / c);
			g2d.fillRect((int)(s3.x * (w / (float)r)) + hOffset, (int)(s3.y * (h / (float)c)) + vOffset, w / r, h / c);
			g2d.fillRect((int)(s4.x * (w / (float)r)) + hOffset, (int)(s4.y * (h / (float)c)) + vOffset, w / r, h / c);
			g2d.setColor(Color.BLUE);
			g2d.drawRect((int)(s1.x * (w / (float)r)) + hOffset, (int)(s1.y * (h / (float)c)) + vOffset, w / r, h / c);
			g2d.drawRect((int)(s2.x * (w / (float)r)) + hOffset, (int)(s2.y * (h / (float)c)) + vOffset, w / r, h / c);
			g2d.drawRect((int)(s3.x * (w / (float)r)) + hOffset, (int)(s3.y * (h / (float)c)) + vOffset, w / r, h / c);
			g2d.drawRect((int)(s4.x * (w / (float)r)) + hOffset, (int)(s4.y * (h / (float)c)) + vOffset, w / r, h / c);
		}
		public boolean canDescend() {
			return canDescend;
		}
    }
}
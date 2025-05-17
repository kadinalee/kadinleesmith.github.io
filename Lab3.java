import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class Lab3 extends JFrame implements ActionListener {
	static final long serialVersionUID = 1;
	JButton setSizeButton = new JButton("Set Size");
	JButton resetButton = new JButton("Reset");
	JButton solveButton = new JButton("Solve");
	JTextField widthTF = new JTextField(5);
	JTextField heightTF = new JTextField(5);
	JLabel widthLabel = new JLabel("Width:");
	JLabel heightLabel = new JLabel("Height:");
	JScrollPane scrollPane = new JScrollPane();
	int mazeWidth = 15;
	int mazeHeight = 10;
	Maze maze;

	public static void main(String args[]) {
		JFrame f = new Lab3();
		f.setVisible(true);
	}
	
	public Lab3() {
//set up the JFrame
		setLocation(5, 40);
		setTitle("Maze");
		setLayout(null);
		setSize(494, 409);
		setResizable(false);
//create an initial maze
		maze = new Maze(mazeWidth, mazeHeight);
		maze.setLocation(3, 3);
//set up a ScrollPane to contain the maze
		scrollPane.setLocation(20, 10);
		scrollPane.setSize(452, 330);
		scrollPane.add(maze);
		add(scrollPane);
//set up a Label for the width JTextField
		widthLabel.setLocation(20,351);
		widthLabel.setSize(43,16);
		add(widthLabel);
//set up the width JTextField
		widthTF.setLocation(65,348);
		widthTF.setSize(30,20);
		widthTF.setText(mazeWidth+"");
		add(widthTF);
//set up a Label for the height JTextField
		heightLabel.setLocation(110,351);
		heightLabel.setSize(50,16);
		add(heightLabel);
//set up the height JTextField
		heightTF.setLocation(157,348);
		heightTF.setSize(30,20);
		heightTF.setText(mazeHeight+"");
		add(heightTF);
//set up the setSize Button
		setSizeButton.setLocation(205,348);
		setSizeButton.setSize(85,20);
		add(setSizeButton);
		setSizeButton.addActionListener(this);
//set up the reset Button
		resetButton.setLocation(310,348);
		resetButton.setSize(70,20);
		add(resetButton);
		resetButton.addActionListener(this);
//set up the solve Button
		solveButton.setLocation(400,348);
		solveButton.setSize(70,20);
		add(solveButton);
		solveButton.addActionListener(this);
//catch the windowClosing event to quit the application
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == setSizeButton) {      //get width & height and create new maze
			try {
				int w = Integer.parseInt(widthTF.getText().trim());
				if (w > 16 || w < 1) {	//illegal width is reset to initial value of 15
					w = 15;
					widthTF.setText(w+"");
				}
				int h = Integer.parseInt(heightTF.getText().trim());
				if (h > 11 || h < 1) {	//illegal height is reset to initial value of 10
					h = 10;
					heightTF.setText(h+"");
				}
				mazeWidth = w;
				mazeHeight = h;
				scrollPane.remove(maze);
				maze = new Maze(mazeWidth,mazeHeight);
				scrollPane.add(maze);
			} catch (NumberFormatException nfe) {   //restore values if bad input
				widthTF.setText(mazeWidth+"");
				heightTF.setText(mazeHeight+"");
			}
		} else if (e.getSource() == resetButton) {  //clear doors and path
			maze.reset();
		} else if (e.getSource() == solveButton) {  //solve the current maze
			solveIt(maze,mazeWidth,mazeHeight);
		}
	}

//****************************************************************************************	
	void solveIt(Maze maze, int width, int height) {
		ArrayList<Point> path = new ArrayList<Point>();		
		boolean solved = false;
		// check if doors are right or left
		// door methods are (y , x)
		// width stays the same and height changes (y)
		// find edges, if door top, left, down, right. If top or bottom.
		// check if doors are top and bottom
		// height stays the same and width changes (x)
		for (int j = 0; j < height -1; j ++ ) {
			if (maze.doorRight(j, width - 1)) {
				path.add( new Point(width, j));
				path.add(new Point(width -1, j));
				// fix search method
				solved = search(maze, width - 1, j , path, width, height);
				// System.out.println("found door3");
				// System.out.println(solved);		
				if (solved == true) {
					maze.setPath(path);
					return;
				}			
				path.clear();
			}
			if (maze.doorLeft(j, 0)){
				path.add( new Point( -1 ,j));
				path.add(new Point(0,j));
				solved = search(maze, 0 , j, path, width, height);
				// System.out.println("found door4");
				// System.out.println(solved);	
				if (solved == true) {
					maze.setPath(path);
					return;
			
				}
				path.clear();			
			}
			
		}
		for (int i = 0; i < width - 1; i ++) {		
			if (maze.doorUp(0, i)) {
				path.add( new Point(i, -1));
				path.add(new Point(i, 0));	
				solved = search(maze, i, 0 , path, width, height);	
				// System.out.println("found door2");
				// System.out.println(solved);		
				if (solved == true) {
					maze.setPath(path);
					return;
				}		
				path.clear();	
			}
			
			if (maze.doorDown(height-1,i )) {
				path.add( new Point(i, height));
				
				path.add(new Point(i, height-1));
				solved = search(maze, i , height - 1 , path, width , height);		
				// System.out.println("found door1");
				// System.out.println(solved);	
				if (solved == true) {
					maze.setPath(path);
					return;
				}
				path.clear();
				
			} 
			
		}
		
		
		
		maze.setPath(path);
	}
	public boolean search (Maze maze, int x, int y, ArrayList<Point> path, int width, int height) {
		// need to return something 
		if (x < 0 || y < 0 || x > width - 1 || y > height - 1) {
			path.remove(path.size() - 1);
			path.remove(0);
			return true;
		}
		if (maze.doorRight(y, x) && !path.contains(new Point(x + 1,y))) {
			path.add (new Point(x +1, y));
			return search(maze, x + 1, y, path, width, height);
			// return true;
		}
		if (maze.doorLeft(y, x)&& !path.contains(new Point(x - 1,y))) {
			path.add(new Point(x -1 , y));
				return search(maze, x -1, y, path, width, height);	
				// return true;		
		}
		if (maze.doorDown(y , x) && !path.contains(new Point(x,y + 1))) {
			path.add(new Point(x , y + 1));
			return search(maze, x, y + 1, path, width, height);
			
		}
		if (maze.doorUp(y, x)&& !path.contains(new Point(x,y - 1))) {
			path.add(new Point(x , y -1));
			return search(maze, x, y -1 , path, width, height);
			// return true;
		}
		
		
		// if no doors or end 
		return false;
	}
	
//****************************************************************************************	
}


class Maze extends Canvas {
	static final long serialVersionUID = 1;
	private static final int WALL_SIZE = 28;
	private int maze[][];
	private ArrayList<Point> path = null;
	
	public Maze(int width, int height) {
	//create an array of the specified size
		maze = new int[height][width];
		reset();
	//set the size of the canvas
		setSize(width*WALL_SIZE+2, height*WALL_SIZE+2);
	//listen for clicks in the canvas
	//change a door into a wall or a wall into a door
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int x = Math.max(0,Math.min(e.getX(),maze[0].length*WALL_SIZE-1));
				int y = Math.max(0,Math.min(e.getY(),maze.length*WALL_SIZE-1));
				int j = x / (WALL_SIZE);
				int i = y / (WALL_SIZE);
				if (x % (WALL_SIZE) <= 2) {            //wall on left of this room
					maze[i][j]   = maze[i][j]   ^ 8;     //toggle left door
					if (j > 0)
						maze[i][j-1] = maze[i][j-1] ^ 2; //toggle right door in adjacent room
				} else if (x % (WALL_SIZE) >= WALL_SIZE - 2) {  //wall on right of this room
					maze[i][j]   = maze[i][j]   ^ 2;     //toggle right door
					if (j < maze[0].length-1)
						maze[i][j+1] = maze[i][j+1] ^ 8;      //toggle left door in adjacent room
				} else if (y % (WALL_SIZE) <= 2) {     //wall above this room
					maze[i][j]   = maze[i][j]   ^ 1;     //toggle upper door of this room
					if (i > 0)
						maze[i-1][j] = maze[i-1][j] ^ 4; //toggle lower door in adjacent room
				} else if (y % (WALL_SIZE) >= WALL_SIZE - 2) {  //wall below this room
					maze[i][j]   = maze[i][j]   ^ 4;     //toggle lower door of this room
					if (i < maze.length-1)
						maze[i+1][j] = maze[i+1][j] ^ 1; //toggle upper door in adjacent room
				}
				repaint();
			}
		});
	}
	
	public void reset() {
	//clear all doors and the path
		for (int i=0; i<maze.length; i++)
			for (int j=0; j<maze[0].length; j++)
				maze[i][j] = 0;
		path = null;
		repaint();
	}
	
	public void paint(Graphics g) {
	//draw the doors and the path if there is one
		drawDoors(g);
		if (path != null)
			drawPath(g);
	}
	
	private void drawDoors(Graphics g) {
		int i,j;
		//for each room, draw door/wall above and to the left
		for (i=0; i<maze.length; i++)
			for (j=0; j<maze[0].length; j++) {
				if ((maze[i][j] & 1) != 0) {   //door above this room
					g.drawLine(j*WALL_SIZE,                i*WALL_SIZE,      j*WALL_SIZE + WALL_SIZE/4,i*WALL_SIZE);
					g.drawLine(j*WALL_SIZE + 3*WALL_SIZE/4,i*WALL_SIZE,  (j+1)*WALL_SIZE,              i*WALL_SIZE);
				} else {
					g.drawLine(j*WALL_SIZE,i*WALL_SIZE,  (j+1)*WALL_SIZE,i*WALL_SIZE);
				}
				if ((maze[i][j] & 8) != 0) {   //door to the left in this room
					g.drawLine(j*WALL_SIZE,i*WALL_SIZE,                j*WALL_SIZE,    i*WALL_SIZE + WALL_SIZE/4);
					g.drawLine(j*WALL_SIZE,i*WALL_SIZE + 3*WALL_SIZE/4,j*WALL_SIZE,(i+1)*WALL_SIZE);
				} else {
					g.drawLine(j*WALL_SIZE,i*WALL_SIZE,j*WALL_SIZE,(i+1)*WALL_SIZE);
				}
		}
		//for bottom row, draw door/wall below
		i = maze.length;
		for (j=0; j<maze[0].length; j++)
			if ((maze[i-1][j] & 4) != 0) {   //door below this room
				g.drawLine(j*WALL_SIZE,                i*WALL_SIZE,      j*WALL_SIZE + WALL_SIZE/4,i*WALL_SIZE);
				g.drawLine(j*WALL_SIZE + 3*WALL_SIZE/4,i*WALL_SIZE,  (j+1)*WALL_SIZE,              i*WALL_SIZE);
			} else {
				g.drawLine(j*WALL_SIZE,i*WALL_SIZE,  (j+1)*WALL_SIZE,i*WALL_SIZE);
			}
		//for right column, draw door/wall to the right
		j = maze[0].length;
		for (i=0; i<maze.length; i++)
			if ((maze[i][j-1] & 2) != 0) {   //door to the right in this room
				g.drawLine(j*WALL_SIZE,i*WALL_SIZE,                j*WALL_SIZE,    i*WALL_SIZE + WALL_SIZE/4);
				g.drawLine(j*WALL_SIZE,i*WALL_SIZE + 3*WALL_SIZE/4,j*WALL_SIZE,(i+1)*WALL_SIZE);
			} else {
				g.drawLine(j*WALL_SIZE,i*WALL_SIZE,j*WALL_SIZE,(i+1)*WALL_SIZE);
			}
	}
	
	private void drawPath(Graphics g) {
	//for each entry in the ArrayList<Point>, draw a line from the 
	//middle of that room to the middle of the next room
		for (int n=0; n<(path.size()-1); n++) {
			Point here = (Point)path.get(n);
			Point next = (Point)path.get(n+1);
			g.setColor(Color.red);
			g.drawLine(here.x*WALL_SIZE+WALL_SIZE/2, here.y*WALL_SIZE+WALL_SIZE/2,
			           next.x*WALL_SIZE+WALL_SIZE/2, next.y*WALL_SIZE+WALL_SIZE/2);
			g.setColor(Color.black);
		}
	}
	
	public void setPath(ArrayList<Point> path) {
	//put the path in our instance variable and redraw the maze
		this.path = path;
		repaint();
	}
	
	public boolean doorUp(int i, int j) {
	//true if there is a door going up from this room
		return (maze[i][j] & 1) != 0;
	}
	
	public boolean doorRight(int i, int j) {
	//true if there is a door going to the right from this room
		return (maze[i][j] & 2) != 0;
	}
	
	public boolean doorDown(int i, int j) {
	//true if there is a door going down from this room
		return (maze[i][j] & 4) != 0;
	}
	
	public boolean doorLeft(int i, int j) {
	//true if there is a door going to the left from this room
		return (maze[i][j] & 8) != 0;
	}
}
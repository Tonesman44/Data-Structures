import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class MazeGridPanel extends JPanel {
	private int rows;
	private int cols;
	private Cell[][] maze;

	// Extra credit
	public void generateMazeByDFS() {
		boolean[][] visited;
		Stack<Cell> stack = new Stack<Cell>();
		Cell start = maze[0][0];
		stack.push(start);
	}

	public void solveMaze() {
		Stack<Cell> stack = new Stack<Cell>();
		Cell start = maze[0][0];
		start.setBackground(Color.GREEN);
		Cell finish = maze[rows - 1][cols - 1];
		finish.setBackground(Color.RED);
		stack.push(start);

		while (!stack.isEmpty()) {
			Cell currentCell = stack.peek();
			int row = currentCell.row;
			int col = currentCell.col;

			// If we have reached the finish cell, exit the loop
			if (currentCell == finish) {
				break;
			}

			// If statements to move in each direction
			if (row > 0 && !visited(row - 1, col) && !currentCell.northWall) { // North
				stack.push(maze[row - 1][col]);
				maze[row - 1][col].setBackground(Color.YELLOW); // Mark as visited
				continue;
			}
			if (col < cols - 1 && !visited(row, col + 1) && !currentCell.eastWall) { // East
				stack.push(maze[row][col + 1]);
				maze[row][col + 1].setBackground(Color.YELLOW); // Mark as visited
				continue;
			}
			if (row < rows - 1 && !visited(row + 1, col) && !currentCell.southWall) { // South
				stack.push(maze[row + 1][col]);
				maze[row + 1][col].setBackground(Color.YELLOW); // Mark as visited
				continue;
			}
			if (col > 0 && !visited(row, col - 1) && !currentCell.westWall) { // West
				stack.push(maze[row][col - 1]);
				maze[row][col - 1].setBackground(Color.YELLOW); // Mark as visited
				continue;
			}

			// If we reach here, it means we are at a dead-end
			currentCell.setBackground(Color.GRAY); // Mark as dead-end
			stack.pop(); // Backtrack
		}
	}


	public boolean visited(int row, int col) {
		Cell c = maze[row][col];
		Color status = c.getBackground();
		if (status.equals(Color.WHITE) || status.equals(Color.RED)) {
			return false;
		}

		return true;
	}

	public void generateMaze() {
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {

				if (row == 0 && col == 0) {
					continue;
				} else if (row == 0) {
					maze[row][col].westWall = false;
					maze[row][col - 1].eastWall = false;
				} else if (col == 0) {
					maze[row][col].northWall = false;
					maze[row - 1][col].southWall = false;
				} else {
					boolean north = Math.random() < 0.5;
					if (north) {
						maze[row][col].northWall = false;
						maze[row - 1][col].southWall = false;
					} else {
						maze[row][col].westWall = false;
						maze[row][col - 1].eastWall = false;
					}
					maze[row][col].repaint();
				}
			}
		}

		this.repaint();
	}

	public MazeGridPanel(int rows, int cols) {
		this.setPreferredSize(new Dimension(800, 800));
		this.rows = rows;
		this.cols = cols;
		this.setLayout(new GridLayout(rows, cols));
		this.maze = new Cell[rows][cols];
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				maze[row][col] = new Cell(row, col);
				this.add(maze[row][col]);
			}
		}

		this.generateMaze();
		this.solveMaze();
	}
}

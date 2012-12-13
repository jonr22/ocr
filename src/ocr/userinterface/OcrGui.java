package ocr.userinterface;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ocr.info.Coordinate;
import ocr.info.Grid;

/**
 * Display GUI with Grid where user can draw a letter, than use an Artificial Neural Network
 * processor to identify the letter.
 * 
 * The GUI will also be used to perform training, where it can create and view training data,
 * run the training and store the results, which can later be re-loaded on future runs.
 *
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class OcrGui {
	// Constants
	private static final int START_WIDTH = 500;
	private static final int START_HEIGHT = 500;
	private static final int DEFAULT_GRID_SIZE = 8;
	private static final String INFO_STRING = "Time: %d ms  |  Steps: %d  |  Path: %d units";
	private static final String DEFAULT_INFO_STRING = "Time: 0 ms  |  Steps: 0  |  Path: 0 units";
	private static final String GRID_SIZE_LABEL = "Grid Size:";
	private static final Color LETTER_COLOR = Color.DARK_GRAY;
	private static final Color BACKGROUND_COLOR = Color.WHITE;
	private static final Color GRID_COLOR = Color.BLACK;

	// instance variables
	private JFrame _frame;
	private GridBoard _gridBoard;
	private Grid _grid;
	private JLabel _infoLabel;

	/**
	 * Create the GUI and instantiate the grid displayed to the user
	 */
	public void buildGui() {

		// instantiate a new Grid
		_grid = new Grid(DEFAULT_GRID_SIZE);

		// create the frame with borders
		_frame = new JFrame("OCR");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		// create the top panel
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel leftTopPanel = new JPanel();
		leftTopPanel.setLayout(new FlowLayout());
		JPanel rightTopPanel = new JPanel();
		rightTopPanel.setLayout(new FlowLayout());

		// create buttons for the top panel
		JButton runBtn = new JButton("Run");
		JButton clearBtn = new JButton("Clear");

		// create and populate combobox for grid sizes
		Integer[] gridSizes = {8, 12, 16};
		JComboBox gridSizeList = new JComboBox(gridSizes);
		gridSizeList.setSelectedItem(DEFAULT_GRID_SIZE);

		// set actions for top panel buttons
		clearBtn.addActionListener(new ClearGridListener());
		runBtn.addActionListener(new  RunGridListener());
		gridSizeList.addActionListener(new SelectGridSizeListener());

		// create label for grid size combobox
		JLabel gridSizeLabel = new JLabel();
		gridSizeLabel.setText(GRID_SIZE_LABEL);

		// add buttons/comboboxes to top panel
		leftTopPanel.add(runBtn);
		leftTopPanel.add(clearBtn);
		rightTopPanel.add(gridSizeLabel);
		rightTopPanel.add(gridSizeList);

		// add panels to top panel
		topPanel.add(leftTopPanel, BorderLayout.WEST);
		topPanel.add(rightTopPanel, BorderLayout.EAST);

		// add top panel to main window
		background.add(topPanel, BorderLayout.NORTH);

		// create bottom panel to display statistics
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());

		// add default text to statistics bar
		_infoLabel = new JLabel();
		_infoLabel.setText(DEFAULT_INFO_STRING);

		bottomPanel.add(_infoLabel);

		background.add(bottomPanel, BorderLayout.SOUTH);

		// create GridBoard (the display of the Grid) and add listeners
		_gridBoard = new GridBoard();
		_gridBoard.addMouseListener(new UserInputListener());
		_gridBoard.addMouseMotionListener(new UserInputListener());
		background.add(_gridBoard, BorderLayout.CENTER);

		// display frame
		_frame.getContentPane().add(background);
		_frame.setSize(START_WIDTH, START_HEIGHT);
		_frame.setVisible(true);
	}

	/**
	 * JPanel display of Grid
	 */
	private class GridBoard extends JPanel {

		/**
		 * Generated Serial Version ID
		 */
		private static final long serialVersionUID = 1711881728225317214L;

		@Override
		public void paintComponent(Graphics g) {
			try {
				super.paintComponent(g);
				int lineThickness = 2;
				int width = getWidth();
				int height = getHeight();
				Graphics2D g2d = (Graphics2D) g;

				// draw background
				g2d.setPaint(BACKGROUND_COLOR);
				g2d.fill(new Rectangle2D.Double(0, 0, width, height));

				// draw nodes (do this before grid lines, so that the lines appear on top of blocks)
				for (int row = 0; row < _grid.getSize(); row++) {
					for (int col = 0; col < _grid.getSize(); col++) {
						// get appropriate color for NodeType
						if(_grid.getValue(new Coordinate(row, col))) {
							// fill in grid block with selected color
							g2d.setPaint(LETTER_COLOR);
							g2d.fill(new Rectangle2D.Double(
									(width * col) / _grid.getSize(),
									(height * row) / _grid.getSize(),
									width / _grid.getSize(),
									height / _grid.getSize()));
						}
					}
				}

				// draw grid
				g2d.setPaint(GRID_COLOR);
				g2d.setStroke(new BasicStroke(lineThickness));
				for (int lineNum = 0; lineNum <= _grid.getSize(); lineNum++) {
					g2d.draw(new Line2D.Double(0, (height * lineNum) / _grid.getSize(), width, (height * lineNum) / _grid.getSize()));
					g2d.draw(new Line2D.Double((width * lineNum) / _grid.getSize(), 0, (width * lineNum) / _grid.getSize(), height));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Clear the entire Grid
	 */
	private class ClearGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			_infoLabel.setText(DEFAULT_INFO_STRING);
			_grid.clear();
			_gridBoard.repaint();
		}
	}

	/**
	 * Run the selected algorithm
	 */
	private class RunGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				// call the algorithm
				// TODO: call the algorithm

				// display result
				// TODO: display result JOptionPane.showMessageDialog(null, "Unable to find a path!");

				// update the statistics info
				// TODO: update info label _infoLabel.setText(String.format(""));

				// update the GUI
				_gridBoard.repaint();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Set Grid Size
	 */
	private class SelectGridSizeListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				// get currently selected grid size
				JComboBox cb = (JComboBox)a.getSource();
				int size = (Integer)cb.getSelectedItem();

				// reset statistics and update the grid
				_infoLabel.setText(DEFAULT_INFO_STRING);
				_grid = new Grid(size);

				// update the GUI
				_gridBoard.repaint();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Handle mouse clicks (to set blocks used create letters)
	 */
	private class UserInputListener implements MouseListener, MouseMotionListener {
		private Coordinate _coord = new Coordinate(); // hold the currently clicked on coordinate

		/**
		 * Handle all mouse clicks
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			// find and set the coordinates of the current click
			_coord.setRow((e.getY() * _grid.getSize()) /_gridBoard.getHeight());
			_coord.setCol((e.getX() * _grid.getSize()) /_gridBoard.getWidth());

			// add the letter block
			addBlock();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// get coordinate
			Coordinate coord = new Coordinate(
					(e.getY() * _grid.getSize()) /_gridBoard.getHeight(),
					(e.getX() * _grid.getSize()) /_gridBoard.getWidth());

			// mouse drag will continue to pump messages after mouse has been
			//   dragged off the GUI, so make sure the drag is within the gui,
			//   also make sure that the coordinate is not the same as the last
			//   one (ie, message pump was too soon)
			if (coord.getRow() >= 0 && coord.getRow() < _grid.getSize() &&
					coord.getCol() >= 0 && coord.getCol() < _grid.getSize() &&
					!coord.isEqual(_coord)) {
				_coord = coord.clone();
				addBlock();
			}
		}

		// Ignore all other mouse events
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseMoved(MouseEvent e) {}

		/**
		 * Add/Remove a block/wall to the grid at the current Coordinate
		 */
		private void addBlock() {
			try {
				// flip value at the coordinate
				_grid.setValue(_coord, !_grid.getValue(_coord));
				_gridBoard.repaint();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Run GUI
	 * @param args
	 */
	public static void main(String[] args) {
		OcrGui gui = new OcrGui();
		gui.buildGui();
	}
}


package ocr.userinterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ocr.data.GridProcessor;
import ocr.data.NetworkManager;
import ocr.data.TrainingManager;
import ocr.data.TrainingSetManager;
import ocr.info.OutputValues;
import ocr.info.TrainingGrid;

/**
 * GUI to display for training stage of OCR, it lets you create, save, load training sets
 * and run them. A training set consists of drawings and the expected letter they represent.
 *
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class TrainGui {
	// Constants
	private static final int START_WIDTH = 500;
	private static final int START_HEIGHT = 500;

	// instance variables
	private JFrame _frame;
	private GridPanel _gridPanel;
	private TrainingSetManager _trainingSet;
	private char _expectedOutput;
	private TrainingGrid _currentTrainGrid;

	private JComboBox _expectedOutputList;
	private JButton _trainBtn;

	private NetworkManager _networkManager = new NetworkManager();
	private TrainingManager _trainingManager = new TrainingManager();
	
	private int _index = 0;

	/**
	 * Create the GUI and instantiate the grid displayed to the user
	 */
	public void buildGui() {
		// create the frame with borders
		_frame = new JFrame("OCR");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		// create the top panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());

		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(new FlowLayout());
		JPanel secondPanel = new JPanel();
		secondPanel.setLayout(new FlowLayout());

		// create buttons for the top panel
		JButton newSetBtn = new JButton("New Set");
		JButton loadSetBtn = new JButton("Load Set");
		JButton saveSetBtn = new JButton("Save Set");
		_trainBtn = new JButton("Train");
		JButton saveNetBtn = new JButton("Save Net");
		JButton loadNetBtn = new JButton("Load Net");
		JButton testNetBtn = new JButton("Test");

		// set actions for top panel buttons
		newSetBtn.addActionListener(new NewSetListener());
		loadSetBtn.addActionListener(new LoadSetListener());
		saveSetBtn.addActionListener(new SaveSetListener());
		_trainBtn.addActionListener(new TrainListener());
		saveNetBtn.addActionListener(new SaveNetListener());
		loadNetBtn.addActionListener(new LoadNetListener());
		testNetBtn.addActionListener(new TestNetListener());

		// add buttons to top panel
		firstPanel.add(newSetBtn);
		firstPanel.add(loadSetBtn);
		firstPanel.add(saveSetBtn);
		secondPanel.add(_trainBtn);
		secondPanel.add(saveNetBtn);
		secondPanel.add(loadNetBtn);
		secondPanel.add(testNetBtn);

		// add top panel to main window
		topPanel.add(firstPanel, BorderLayout.NORTH);
		topPanel.add(secondPanel, BorderLayout.SOUTH);
		background.add(topPanel, BorderLayout.NORTH);

		// create the bottom panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout());

		// create buttons for the bottom panel
		JButton saveBtn = new JButton("Add");
		JButton deleteBtn = new JButton("Delete");
		JButton clearBtn = new JButton("Clear");
		JButton leftBtn = new JButton("<<<");
		JButton rightBtn = new JButton(">>>");

		_expectedOutputList = new JComboBox(OutputValues.OUTPUT);

		// set actions for bottom panel buttons
		_expectedOutputList.addActionListener(new SelectOutputListener());
		saveBtn.addActionListener(new SaveGridListener());
		deleteBtn.addActionListener(new DeleteGridListener());
		clearBtn.addActionListener(new ClearGridListener());
		leftBtn.addActionListener(new LeftGridListener());
		rightBtn.addActionListener(new RightGridListener());

		// add buttons to bottom panel
		bottomPanel.add(_expectedOutputList);
		bottomPanel.add(saveBtn);
		bottomPanel.add(deleteBtn);
		bottomPanel.add(clearBtn);
		bottomPanel.add(leftBtn);
		bottomPanel.add(rightBtn);

		// add bottom panel to main window
		background.add(bottomPanel, BorderLayout.SOUTH);

		// create GridBoard (the display of the Grid) and add listeners
		_gridPanel = new GridPanel();
		_gridPanel.addMouseListener(new UserInputListener());
		_gridPanel.addMouseMotionListener(new UserInputListener());
		_gridPanel.setDoPaintGrid(false);
		background.add(_gridPanel, BorderLayout.CENTER);

		// display frame
		_frame.getContentPane().add(background);
		_frame.setSize(START_WIDTH, START_HEIGHT);
		_frame.setVisible(true);
	}

	/**
	 * Set the expected output
	 */
	private class SelectOutputListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				// get currently selected grid size
				JComboBox cb = (JComboBox)a.getSource();
				_expectedOutput = (Character)cb.getSelectedItem();
				if (_expectedOutput == ' ') { // TODO: this shouldn't be needed anymore
					_expectedOutput = 0;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Create a new Training Set
	 */
	private class NewSetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				_trainingSet = new TrainingSetManager();
				_gridPanel.setDoPaintGrid(true);
				_gridPanel.clear();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Load a Training Set from a file
	 */
	private class LoadSetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				_trainingSet = new TrainingSetManager();

				JFileChooser fileOpen = new JFileChooser();
				if (JFileChooser.APPROVE_OPTION != fileOpen.showOpenDialog(_frame)) {
					return;
				}

				_trainingSet.load(fileOpen.getSelectedFile());
				_gridPanel.setDoPaintGrid(true);

				_currentTrainGrid = _trainingSet.getNext();
				_gridPanel.setGrid(_currentTrainGrid.getGrid());
				setSelectList(_currentTrainGrid.getValue());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Save the Training Set
	 */
	private class SaveSetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				JFileChooser fileSave = new JFileChooser();
				fileSave.showSaveDialog(_frame);
				if (JFileChooser.APPROVE_OPTION != fileSave.showSaveDialog(_frame)) {
					return;
				}

				_trainingSet.save(fileSave.getSelectedFile());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Train the Network
	 */
	private class TrainListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			_trainingManager.setLearningRate(0.7);
			_trainingManager.setNetwork(_networkManager.getNetwork());
			_trainingManager.setTrainingSet(_trainingSet);
			try {
				_trainingManager.train(5000);
				JOptionPane.showMessageDialog(null, "Training Finished!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Save the Network
	 */
	private class SaveNetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				JFileChooser fileSave = new JFileChooser();
				fileSave.showSaveDialog(_frame);
				if (JFileChooser.APPROVE_OPTION != fileSave.showSaveDialog(_frame)) {
					return;
				}

				_networkManager.save(fileSave.getSelectedFile());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Load a saved Network
	 */
	private class LoadNetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				JFileChooser fileOpen = new JFileChooser();
				if (JFileChooser.APPROVE_OPTION != fileOpen.showOpenDialog(_frame)) {
					return;
				}

				_networkManager = new NetworkManager(fileOpen.getSelectedFile());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Test a Network
	 */
	private class TestNetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				char result = GridProcessor.process(_gridPanel.getGrid(), _networkManager.getNetwork());
				System.out.println(String.format("Result: %c", result));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Save the grid within the current Training Set
	 */
	private class SaveGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				// TODO: check if _trainingSet is valid, enable editing (not just saving multiple copies)
				_trainingSet.add(new TrainingGrid(_gridPanel.getGrid(), _expectedOutput));
				_gridPanel.clear();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Delete the grid within the current Training Set
	 */
	private class DeleteGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				// TODO: check if _trainingSet is valid, enable editing (not just saving multiple copies)
				_trainingSet.remove(_currentTrainGrid);

				_currentTrainGrid = _trainingSet.getPrevious();
				setSelectList(_currentTrainGrid.getValue());
				_gridPanel.setGrid(_currentTrainGrid.getGrid());
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
			_gridPanel.clear();
		}
	}

	/**
	 * Move left through the current Training Set
	 */
	private class LeftGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try { // TODO: revert this
				//_currentTrainGrid = _trainingSet.getPrevious();
				//if (_index <= 0) {
					_index = _trainingSet.getCount() - 1;
				//} else {
				//	_index -= 1;
				//}

				_currentTrainGrid = _trainingSet.getGrid(_index);
				setSelectList(_currentTrainGrid.getValue());
				_gridPanel.setGrid(_currentTrainGrid.getGrid());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Move right through the current Training Set
	 */
	private class RightGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				_currentTrainGrid = _trainingSet.getNext();

				setSelectList(_currentTrainGrid.getValue());
				_gridPanel.setGrid(_currentTrainGrid.getGrid());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Handle mouse clicks (to set blocks used create letters)
	 */
	private class UserInputListener implements MouseListener, MouseMotionListener {
		/**
		 * Handle all mouse clicks
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			// find and set the coordinates of the current click
			_gridPanel.selected(e.getX(), e.getY());
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// find and set the coordinates of the dragged mouse
			_gridPanel.dragged(e.getX(), e.getY());
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
	}

	private void setSelectList(char c) {
		_expectedOutputList.setSelectedItem(c);
	}
}

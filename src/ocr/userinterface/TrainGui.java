package ocr.userinterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

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
	private static final String DEFAULT_INDEX_LABEL = "0 of 0";
	private static final String INDEX_LABEL = "%d of %d";
	private static final String NEW_INDEX_LABEL = "? of %d";

	// instance variables
	private char _expectedOutput;
	private TrainingGrid _currentTrainGrid = null;
	private int _index = 0;
	
	private GridPanel _gridPanel = new GridPanel();
	private TrainingSetManager _trainingSet = new TrainingSetManager();
	private NetworkManager _networkManager = new NetworkManager();
	private TrainingManager _trainingManager = new TrainingManager();

	// frame
	private JFrame _frame;
	
	// menu bar titles
	private JMenu _fileMenu = new JMenu("File");
	private JMenu _editMenu = new JMenu("Edit");
	private JMenu _navigateMenu = new JMenu("Navigate");
	private JMenu _runMenu = new JMenu("Run");
	
	// file menu items
	private JMenuItem _newSetMenuItem = new JMenuItem("New Set");
	private JMenuItem _loadSetMenuItem = new JMenuItem("Load Set...");
	private JMenuItem _saveSetMenuItem = new JMenuItem("Save Set");
	private JMenuItem _saveSetAsMenuItem = new JMenuItem("Save Set As...");
	private JMenuItem _newNetMenuItem = new JMenuItem("New Network");
	private JMenuItem _loadNetMenuItem = new JMenuItem("Load Network...");
	private JMenuItem _saveNetMenuItem = new JMenuItem("Save Network");
	private JMenuItem _saveNetAsMenuItem = new JMenuItem("Save Network As...");
	
	// edit menu items
	private JMenuItem _addGridMenuItem = new JMenuItem("Add");
	private JMenuItem _deleteGridMenuItem = new JMenuItem("Delete");
	private JMenuItem _clearGridMenuItem = new JMenuItem("Clear");
	
	// navigate menu items
	private JMenuItem _leftGridMenuItem = new JMenuItem("Move Left");
	private JMenuItem _rightGridMenuItem = new JMenuItem("Move Right");
	private JMenuItem _firstGridMenuItem = new JMenuItem("Move to First Grid");
	private JMenuItem _lastGridMenuItem = new JMenuItem("Move to Last Grid");
	
	// run menu items
	private JMenuItem _executeMenuItem = new JMenuItem("Execute");
	private JMenuItem _trainMenuItem = new JMenuItem("Train");
	
	// sub menu items
	private JMenu _expectedOutputMenu = new JMenu("Expected Output");
	private ButtonGroup _expectedOutputGroup = new ButtonGroup();
	
	// top panel items
	private JComboBox _expectedOutputList = new JComboBox(OutputValues.OUTPUT);
	private JButton _addBtn = new JButton("Add");
	private JButton _clearBtn = new JButton("Clear");
	private JButton _executeBtn = new JButton("Execute");
	
	// bottom panel items
	private JButton _firstBtn = new JButton("|<");
	private JButton _leftBtn = new JButton("<");
	private JButton _rightBtn = new JButton(">");
	private JButton _lastBtn = new JButton(">|");
	private JLabel _indexLabel = new JLabel(DEFAULT_INDEX_LABEL);
	
	/**
	 * Create the GUI and instantiate the grid displayed to the user
	 */
	public void buildGui() {
		// -------------------Frame-------------------
		_frame = new JFrame("OCR");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		// -------------------Top-------------------
		// menu and top panel
		JMenuBar menubar = new JMenuBar();
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());
		
		// create sub-menu
		for (int i = 0; i < OutputValues.OUTPUT.length; i++) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(OutputValues.OUTPUT[i].toString());
			item.addActionListener(new SelectOutputMenuListener());
			_expectedOutputGroup.add(item);
			_expectedOutputMenu.add(item);
		}

		// set accelerators
		_saveSetMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, 
				InputEvent.CTRL_DOWN_MASK));
		_saveNetMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_S, 
				InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		_addGridMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		_clearGridMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		_leftGridMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.META_DOWN_MASK));
		_rightGridMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.META_DOWN_MASK));
		_firstGridMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_LEFT, 
				InputEvent.META_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		_lastGridMenuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_RIGHT, 
				InputEvent.META_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		_executeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));
		_trainMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK));
		
		// set actions
		_newSetMenuItem.addActionListener(new NewSetListener());
		_loadSetMenuItem.addActionListener(new LoadSetListener());
		_saveSetMenuItem.addActionListener(new SaveSetListener());
		_saveSetAsMenuItem.addActionListener(new SaveSetAsListener());
		_newNetMenuItem.addActionListener(new NewNetListener());
		_loadNetMenuItem.addActionListener(new LoadNetListener());
		_saveNetMenuItem.addActionListener(new SaveNetListener());
		_saveNetAsMenuItem.addActionListener(new SaveNetAsListener());
		_addGridMenuItem.addActionListener(new AddGridListener());
		_deleteGridMenuItem.addActionListener(new DeleteGridListener());
		_clearGridMenuItem.addActionListener(new ClearGridListener());
		_leftGridMenuItem.addActionListener(new LeftGridListener());
		_rightGridMenuItem.addActionListener(new RightGridListener());	
		_firstGridMenuItem.addActionListener(new FirstGridListener());
		_lastGridMenuItem.addActionListener(new LastGridListener());
		_executeMenuItem.addActionListener(new ExecuteListener());
		_trainMenuItem.addActionListener(new TrainListener());
		_expectedOutputList.addActionListener(new SelectOutputListener());
		_addBtn.addActionListener(new AddGridListener());
		_clearBtn.addActionListener(new ClearGridListener());
		_executeBtn.addActionListener(new ExecuteListener());
		
		// set disabled
		_saveSetMenuItem.setEnabled(false);
		_saveSetAsMenuItem.setEnabled(false);
		_deleteGridMenuItem.setEnabled(false);
		_leftGridMenuItem.setEnabled(false);
		_rightGridMenuItem.setEnabled(false);
		_firstGridMenuItem.setEnabled(false);
		_lastGridMenuItem.setEnabled(false);
		_trainMenuItem.setEnabled(false);

		// add file menu items
		_fileMenu.add(_newSetMenuItem);
		_fileMenu.add(_loadSetMenuItem);
		_fileMenu.add(_saveSetMenuItem);
		_fileMenu.add(_saveSetAsMenuItem);
		_fileMenu.addSeparator();
		_fileMenu.add(_newNetMenuItem);
		_fileMenu.add(_loadNetMenuItem);
		_fileMenu.add(_saveNetMenuItem);
		_fileMenu.add(_saveNetAsMenuItem);
		
		// add edit menu items
		_editMenu.add(_addGridMenuItem);
		_editMenu.add(_deleteGridMenuItem);
		_editMenu.addSeparator();
		_editMenu.add(_expectedOutputMenu);
		_editMenu.addSeparator();
		_editMenu.add(_clearGridMenuItem);
		
		// add navigate menu items
		_navigateMenu.add(_leftGridMenuItem);
		_navigateMenu.add(_rightGridMenuItem);
		_navigateMenu.addSeparator();
		_navigateMenu.add(_firstGridMenuItem);
		_navigateMenu.add(_lastGridMenuItem);
		
		// add run menu items
		_runMenu.add(_executeMenuItem);
		_runMenu.add(_trainMenuItem);
		
		// add top panel items
		topPanel.add(_expectedOutputList);
		topPanel.add(_addBtn);
		topPanel.add(_clearBtn);
		topPanel.add(_executeBtn);
		
		// setup menu and top panel
		menubar.add(_fileMenu);
		menubar.add(_editMenu);
		menubar.add(_navigateMenu);
		menubar.add(_runMenu);
		_frame.setJMenuBar(menubar);
		background.add(topPanel, BorderLayout.NORTH);
		
		// -------------------Bottom-------------------
		// create the bottom panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());

		JPanel navPanel = new JPanel();
		JPanel infoPanel = new JPanel();
		
		// set actions
		_firstBtn.addActionListener(new FirstGridListener());
		_leftBtn.addActionListener(new LeftGridListener());
		_rightBtn.addActionListener(new RightGridListener());
		_lastBtn.addActionListener(new LastGridListener());

		// set disabled
		_firstBtn.setEnabled(false);
		_leftBtn.setEnabled(false);
		_rightBtn.setEnabled(false);
		_lastBtn.setEnabled(false);
		
		// add buttons
		navPanel.add(_firstBtn);
		navPanel.add(_leftBtn);
		navPanel.add(_rightBtn);
		navPanel.add(_lastBtn);
		
		// add info
		infoPanel.add(_indexLabel);

		// set up bottom panel
		bottomPanel.add(navPanel, BorderLayout.NORTH);
		bottomPanel.add(infoPanel, BorderLayout.SOUTH);
		background.add(bottomPanel, BorderLayout.SOUTH);

		// -------------------Middle-------------------
		// add listeners
		_gridPanel.addMouseListener(new UserInputListener());
		_gridPanel.addMouseMotionListener(new UserInputListener());
		
		// don't paint grid yet
		//_gridPanel.setDoPaintGrid(false);
		
		// add to background
		background.add(_gridPanel, BorderLayout.CENTER);

		// -------------------Display-------------------
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
				setSelectList(_expectedOutput);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Set the expected output
	 */
	private class SelectOutputMenuListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				JRadioButtonMenuItem item = (JRadioButtonMenuItem)a.getSource();
				_expectedOutput = item.getText().charAt(0);
				setSelectList(_expectedOutput);
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
				if (_trainingSet.getHasChanged()) {
					int confirm = confirmQuestion("Set has unsaved changes. Would you like to save?");
					if (confirm == JOptionPane.CANCEL_OPTION) {
						return;
					}
					if (confirm == JOptionPane.YES_OPTION) {
						SaveSetListener saveset = new SaveSetListener();
						saveset.actionPerformed(null);
					}
				}
				
				_trainingSet = new TrainingSetManager();
				_index = 0;
				setTrainingGrid(null);
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
				if (_trainingSet.getHasChanged()) {
					int confirm = confirmQuestion("Set has unsaved changes. Would you like to save?");
					if (confirm == JOptionPane.CANCEL_OPTION) {
						return;
					}
					if (confirm == JOptionPane.YES_OPTION) {
						SaveSetListener saveset = new SaveSetListener();
						saveset.actionPerformed(null);
					}
				}

				JFileChooser fileOpen = new JFileChooser();
				if (JFileChooser.APPROVE_OPTION != fileOpen.showOpenDialog(_frame)) {
					return;
				}

				_trainingSet.load(fileOpen.getSelectedFile());
				_index = 0;

				if (_trainingSet.getCount() > 0) {
					setTrainingGrid(_trainingSet.getGrid(_index));
				} else {
					setTrainingGrid(null);
				}
				 
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private class SaveSetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				if (!_trainingSet.getHasFileSet()) {
					SaveSetAsListener saveset = new SaveSetAsListener();
					saveset.actionPerformed(null);
				} else {
					_trainingSet.save();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Save the Training Set
	 */
	private class SaveSetAsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				JFileChooser fileSave = new JFileChooser();
				fileSave.showSaveDialog(_frame);
				if (JFileChooser.APPROVE_OPTION != fileSave.showSaveDialog(_frame)) {
					return;
				}

				_trainingSet.saveAs(fileSave.getSelectedFile());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private class NewNetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				_networkManager = new NetworkManager();
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
	 * Save the Network
	 */
	private class SaveNetListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				if (!_networkManager.getHasFileSet()) {
					SaveNetAsListener savenet = new SaveNetAsListener();
					savenet.actionPerformed(null);
				} else {
					_networkManager.save();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Save the Network
	 */
	private class SaveNetAsListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				JFileChooser fileSave = new JFileChooser();
				fileSave.showSaveDialog(_frame);
				if (JFileChooser.APPROVE_OPTION != fileSave.showSaveDialog(_frame)) {
					return;
				}

				_networkManager.saveAs(fileSave.getSelectedFile());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Save the grid within the current Training Set
	 */
	private class AddGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				_trainingSet.add(new TrainingGrid(_gridPanel.getGrid(), _expectedOutput));
				_index = _trainingSet.getCount(); // TODO: does this work? one more than available as it's new
				setTrainingGrid(null);				
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
				_trainingSet.remove(_currentTrainGrid);
				
				if (_trainingSet.getCount() > 0) {
					if (_index > 0) { _index--; }
					setTrainingGrid(_trainingSet.getGrid(_index));	
				} else {
					_index = 0;
					setTrainingGrid(null);
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
			_gridPanel.clear();
		}
	}

	/**
	 * Move left through the current Training Set
	 */
	private class LeftGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				if (_index <= 0) {
					JOptionPane.showConfirmDialog(
							_frame, 
							"Already at the first image in the set!", 
							_frame.getTitle(), 
							JOptionPane.OK_OPTION, 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				_index--;
				setTrainingGrid(_trainingSet.getGrid(_index));
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
				if (_index >= _trainingSet.getCount() - 1) {
					JOptionPane.showConfirmDialog(
							_frame, 
							"Already at the last image in the set!", 
							_frame.getTitle(), 
							JOptionPane.OK_OPTION, 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				_index++;
				setTrainingGrid(_trainingSet.getGrid(_index));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Move left through the current Training Set
	 */
	private class FirstGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				if (_index <= 0) {
					JOptionPane.showConfirmDialog(
							_frame, 
							"Already at the first image in the set!", 
							_frame.getTitle(), 
							JOptionPane.OK_OPTION, 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				_index = 0;
				setTrainingGrid(_trainingSet.getGrid(_index));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Move right through the current Training Set
	 */
	private class LastGridListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				if (_index >= _trainingSet.getCount() - 1) {
					JOptionPane.showConfirmDialog(
							_frame, 
							"Already at the last image in the set!", 
							_frame.getTitle(), 
							JOptionPane.OK_OPTION, 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				_index = _trainingSet.getCount() - 1;
				setTrainingGrid(_trainingSet.getGrid(_index));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Execute the neural net
	 */
	private class ExecuteListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				char result = GridProcessor.process(_gridPanel.getGrid(), _networkManager.getNetwork());
				System.out.println(String.format("Result: %c", result)); // TODO: print this somewhere else
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
				// TODO: disable all
				_trainingManager.train(5000);
				JOptionPane.showMessageDialog(null, "Training Finished!");
				// TODO: enable all
			} catch (Exception e) {
				e.printStackTrace();
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
		
		Enumeration<AbstractButton> e = _expectedOutputGroup.getElements();
	    while (e.hasMoreElements()) {
	    	AbstractButton btn = e.nextElement();
	    	if (c == btn.getText().charAt(0)) {
	    		btn.setSelected(true);
	    		break;
	    	}
	    	
	    }
	}
	
	private void setTrainingGrid(TrainingGrid trainGrid) {
		if (trainGrid == null) {
			_currentTrainGrid = null;
			setSelectList((char)0);
			_gridPanel.clear();
		} else {
			_currentTrainGrid = trainGrid;
			setSelectList(_currentTrainGrid.getValue());
			_gridPanel.setGrid(trainGrid.getGrid());
		}
		checkEnableDisable();
	}
	
	private int confirmQuestion(String message) {
		return JOptionPane.showConfirmDialog(
				_frame, 
				message, _frame.getTitle(), 
				JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.QUESTION_MESSAGE);
	}
	
	private void checkEnableDisable() {
		if (_trainingSet.getCount() > 0) {
			_saveSetMenuItem.setEnabled(true);
			_saveSetAsMenuItem.setEnabled(true);
			_trainMenuItem.setEnabled(true);
		} else {
			_saveSetMenuItem.setEnabled(false);
			_saveSetAsMenuItem.setEnabled(false);
			_trainMenuItem.setEnabled(false);
		}
		
		if (_currentTrainGrid == null) {
			_deleteGridMenuItem.setEnabled(true);
		} else {
			_deleteGridMenuItem.setEnabled(true);
		}
		
		if (_index > 0) {
			_leftGridMenuItem.setEnabled(true);
			_leftBtn.setEnabled(true);
			_firstGridMenuItem.setEnabled(true);
			_firstBtn.setEnabled(true);
		} else {
			_leftGridMenuItem.setEnabled(false);
			_leftBtn.setEnabled(false);
			_firstGridMenuItem.setEnabled(false);
			_firstBtn.setEnabled(false);
		}
		
		if (_index < _trainingSet.getCount() - 1) {
			_rightGridMenuItem.setEnabled(true);
			_rightBtn.setEnabled(true);
			_lastGridMenuItem.setEnabled(true);
			_lastBtn.setEnabled(true);
		} else {
			_rightGridMenuItem.setEnabled(false);
			_rightBtn.setEnabled(false);
			_lastGridMenuItem.setEnabled(false);
			_lastBtn.setEnabled(false);
		}
		
		if (_index >= _trainingSet.getCount()) {
			_indexLabel.setText(String.format(NEW_INDEX_LABEL, _trainingSet.getCount()));
		} else {
			_indexLabel.setText(String.format(INDEX_LABEL, _index + 1, _trainingSet.getCount()));
		}
	}
}


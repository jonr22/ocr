package ocr.userinterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ocr.data.TrainingSetManager;
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
        topPanel.setLayout(new FlowLayout());

        // create buttons for the top panel
        JButton newSetBtn = new JButton("New Set");
        JButton loadSetBtn = new JButton("Load Set");
        JButton saveSetBtn = new JButton("Save Set");

        // set actions for top panel buttons
        newSetBtn.addActionListener(new NewSetListener());
        loadSetBtn.addActionListener(new LoadSetListener());
        saveSetBtn.addActionListener(new SaveSetListener());

        // add buttons to top panel
        topPanel.add(newSetBtn);
        topPanel.add(loadSetBtn);
        topPanel.add(saveSetBtn);

        // add top panel to main window
        background.add(topPanel, BorderLayout.NORTH);

        // create the bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        // create buttons for the bottom panel
        JButton newBtn = new JButton("New");
        JButton saveBtn = new JButton("Save");
        //JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        JButton leftBtn = new JButton("<<<");
        JButton rightBtn = new JButton(">>>");

        JComboBox expectedOutputList = new JComboBox(new Character[]{' ', 'A'});

        // set actions for bottom panel buttons
        expectedOutputList.addActionListener(new SelectOutputListener());
        newBtn.addActionListener(new NewGridListener());
        saveBtn.addActionListener(new SaveGridListener());
        clearBtn.addActionListener(new ClearGridListener());
        leftBtn.addActionListener(new LeftGridListener());
        rightBtn.addActionListener(new RightGridListener());

        // add buttons to bottom panel
        bottomPanel.add(expectedOutputList);
        bottomPanel.add(newBtn);
        bottomPanel.add(saveBtn);
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
                if (_expectedOutput == ' ') {
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
                _gridPanel.setGrid(_trainingSet.getNext().getGrid());
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
     * Save the grid within the current Training Set
     */
    private class NewGridListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent a) {
            try {
                // TODO: implement New, check if _trainingSet if valid
                _gridPanel.clear();
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
                _gridPanel.repaint();
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
                TrainingGrid trainGrid = _trainingSet.getPrevious();
                // TODO: update expected output
                _gridPanel.setGrid(trainGrid.getGrid());
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
                TrainingGrid trainGrid = _trainingSet.getNext();
                // TODO: update expected output
                _gridPanel.setGrid(trainGrid.getGrid());
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
}

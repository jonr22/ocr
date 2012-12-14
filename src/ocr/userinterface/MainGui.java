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
import javax.swing.JFrame;
import javax.swing.JPanel;

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
public class MainGui {
    // Constants
    private static final int START_WIDTH = 500;
    private static final int START_HEIGHT = 500;

    // instance variables
    private JFrame _frame;
    private GridPanel _gridPanel;

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
        JButton runBtn = new JButton("Run");
        JButton clearBtn = new JButton("Clear");

        // set actions for top panel buttons
        clearBtn.addActionListener(new ClearGridListener());
        runBtn.addActionListener(new  RunGridListener());

        // add buttons to top panel
        topPanel.add(runBtn);
        topPanel.add(clearBtn);

        // add top panel to main window
        background.add(topPanel, BorderLayout.NORTH);

        // create GridBoard (the display of the Grid) and add listeners
        _gridPanel = new GridPanel();
        _gridPanel.addMouseListener(new UserInputListener());
        _gridPanel.addMouseMotionListener(new UserInputListener());
        background.add(_gridPanel, BorderLayout.CENTER);

        // display frame
        _frame.getContentPane().add(background);
        _frame.setSize(START_WIDTH, START_HEIGHT);
        _frame.setVisible(true);
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
     * Run the OCR
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
                _gridPanel.repaint();
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

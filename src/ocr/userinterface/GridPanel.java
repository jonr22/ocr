package ocr.userinterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import ocr.info.Coordinate;
import ocr.info.Grid;

public class GridPanel extends JPanel {
    /**
     * Generated Serial Version ID
     */
    private static final long serialVersionUID = 1711881728225317214L;

    // constants
    private static final Color LETTER_COLOR = Color.DARK_GRAY;
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color GRID_COLOR = Color.BLACK;
    private static final int GRID_SIZE = 8;

    // instance variables
    private Grid _grid = new Grid(GRID_SIZE);
    private Coordinate _coord = new Coordinate();
    private boolean _doPaintGrid = true;

    /**
     * Get the grid used by GridPanel
     * @return Grid
     */
    public Grid getGrid() {
        return _grid.clone();
    }

    /**
     * Set the grid to use
     * @param grid - the grid to set
     */
    public void setGrid(Grid grid) {
        _grid = grid.clone();
        repaint();
    }

    /**
     * Clear the grid
     */
    public void clear() {
        _grid.clear();
        repaint();
    }

    public void setDoPaintGrid(boolean doPaint) {
        _doPaintGrid = doPaint;
    }

    public void selected(int x, int y) {
        _coord.setCol((x * _grid.getSize()) / getWidth());
        _coord.setRow((y * _grid.getSize()) / getHeight());

        // set the value
        flipCoord();
    }

    public void dragged(int x, int y) {
        // get coordinate
        Coordinate coord = new Coordinate(
                (y * _grid.getSize()) / getHeight(),
                (x * _grid.getSize()) / getWidth());

        // mouse drag will continue to pump messages after mouse has been
        //   dragged off the GUI, so make sure the drag is within the gui,
        //   also make sure that the coordinate is not the same as the last
        //   one (ie, message pump was too soon)
        if (coord.getRow() >= 0 && coord.getRow() < _grid.getSize() &&
                coord.getCol() >= 0 && coord.getCol() < _grid.getSize() &&
                !coord.isEqual(_coord)) {
            _coord = coord.clone();
            flipCoord();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        try {
            super.paintComponent(g);

            if (!_doPaintGrid) {
                return;
            }

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

    /**
     * Select/de-select node at coord
     * @param coord - coordinate to flip
     */
    private void flipCoord() {
        try {
            // flip value at the coordinate
            _grid.setValue(_coord, !_grid.getValue(_coord));
            repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

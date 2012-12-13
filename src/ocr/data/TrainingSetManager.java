package ocr.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import ocr.info.TrainingGrid;

/**
 * Manage training data for OCR.
 * The TrainingDataManager class is a container for a set of grids and
 * their expected values. These sets can be saved so that they can be
 * loaded at a later time. TrainingGrids can be added, removed, and edited
 * within a set.
 *
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class TrainingSetManager {
    // instance variables
    private ArrayList<TrainingGrid> _grids = new ArrayList<TrainingGrid>();
    private int _index = -1;

    /**
     * Add a TrainingGrid to the set
     * @param grid - TrainingGrid to add
     */
    public void add(TrainingGrid grid) {
        _grids.add(grid);
    }

    /**
     * Remove an existing TrainingGrid from the set
     * @param grid - TrainingGrid to remove
     * @throws Exception
     */
    public void remove(TrainingGrid grid) throws Exception {
        if (!_grids.contains(grid)) {
            throw new Exception("Grid could not be removed from TrainingDataManager as it doesn't exist");
        }

        _grids.remove(grid);
    }

    /**
     * Replace an existing TrainingGrid with a new one in the set
     * @param original - TrainingGrid to replace
     * @param edited - TrainingGrid to replace with
     * @throws Exception
     */
    public void edit(TrainingGrid original, TrainingGrid edited) throws Exception {
        if (!_grids.contains(original)) {
            throw new Exception("Grid could not be edited in TrainingDataManager as it doesn't exist");
        }

        // add edited grid at the same location original was at
        int index = _grids.indexOf(original);
        _grids.remove(original);
        _grids.add(index, edited);
    }

    /**
     * Clear all TrainingGrids from the set
     */
    public void clear() {
        _grids = new ArrayList<TrainingGrid>();
    }

    /**
     * Save the set of TrainingGrids
     * @param filename - file to save
     * @throws IOException
     */
    public void save(String filename) throws IOException {
        FileOutputStream fs = new FileOutputStream(filename);
        ObjectOutputStream os = new ObjectOutputStream(fs);
        os.writeObject(_grids);
        os.close();
    }

    /**
     * Load a set of TrainingGrids
     * @param filename - file to load
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void load(String filename) throws Exception {
        try {
            FileInputStream fs = new FileInputStream(filename);
            ObjectInputStream os = new ObjectInputStream(fs);
            _grids = (ArrayList<TrainingGrid>) os.readObject();
            os.close();
        } catch (Exception ex) {
            _grids = new ArrayList<TrainingGrid>();
            throw ex;
        }
    }

    /**
     * Get count of TrainingGrids in set
     * @return count
     */
    public int getCount() {
        return _grids.size();
    }

    /**
     * Get TrainingGrid at a specific index
     * @param index - index of TrainingGrid
     * @return TrainingGrid at index
     * @throws Exception
     */
    public TrainingGrid getGrid(int index) throws Exception {
        if (index < 0 || index >= _grids.size()) {
            throw new Exception(String.format(
                    "getGrid called on TrainingSetManager with invalid iindex, expected index to be within %d not %d",
                    _grids.size(),
                    index));
        }

        return _grids.get(index);
    }

    /**
     * Get the next TrainingGrid in the set (starts at index 0 and continuously loops)
     * @return TrainingGrid
     * @throws Exception
     */
    public TrainingGrid getNext() throws Exception {
        if (_grids.isEmpty()) {
            throw new Exception("getNext() called in TrainingDataManager on empty data set");
        }

        _index = (_index + 1) % _grids.size();
        return _grids.get(_index);
    }

    /**
     * Get the previous TrainingGrid in the set (continuously loops backward through training sets)
     * @return
     * @throws Exception
     */
    public TrainingGrid getPrevious() throws Exception {
        if (_grids.isEmpty()) {
            throw new Exception("getPrevious() called in TrainingDataManager on empty data set");
        }

        _index = (_index - 1) % _grids.size();
        return _grids.get(_index);
    }
}

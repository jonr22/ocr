package ocr.data;

import java.io.File;
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
    private ArrayList<TrainingGrid> _trainingGrids = new ArrayList<TrainingGrid>();
    private int _index = -1;

    /**
     * Add a TrainingGrid to the set
     * @param trainingGrid - TrainingGrid to add
     */
    public void add(TrainingGrid trainingGrid) {
        _trainingGrids.add(trainingGrid);
    }

    /**
     * Remove an existing TrainingGrid from the set
     * @param trainingGrid - TrainingGrid to remove
     * @throws Exception
     */
    public void remove(TrainingGrid trainingGrid) throws Exception {
        if (!_trainingGrids.contains(trainingGrid)) {
            throw new Exception("Grid could not be removed from TrainingDataManager as it doesn't exist");
        }

        _trainingGrids.remove(trainingGrid);
    }

    /**
     * Replace an existing TrainingGrid with a new one in the set
     * @param original - TrainingGrid to replace
     * @param edited - TrainingGrid to replace with
     * @throws Exception
     */
    public void edit(TrainingGrid original, TrainingGrid edited) throws Exception {
        if (!_trainingGrids.contains(original)) {
            throw new Exception("Grid could not be edited in TrainingDataManager as it doesn't exist");
        }

        // add edited grid at the same location original was at
        int index = _trainingGrids.indexOf(original);
        _trainingGrids.remove(original);
        _trainingGrids.add(index, edited);
    }

    /**
     * Clear all TrainingGrids from the set
     */
    public void clear() {
        _trainingGrids = new ArrayList<TrainingGrid>();
    }

    /**
     * Save the set of TrainingGrids
     * @param file - File to save
     * @throws IOException
     */
    public void save(File file) throws IOException {
        FileOutputStream fs = new FileOutputStream(file);
        ObjectOutputStream os = new ObjectOutputStream(fs);
        os.writeObject(_trainingGrids);
        os.close();
    }

    /**
     * Load a set of TrainingGrids
     * @param file - File to load
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public void load(File file) throws Exception {
        try {
            FileInputStream fs = new FileInputStream(file);
            ObjectInputStream os = new ObjectInputStream(fs);
            _trainingGrids = (ArrayList<TrainingGrid>) os.readObject();
            os.close();
        } catch (Exception ex) {
            _trainingGrids = new ArrayList<TrainingGrid>();
            throw ex;
        }
    }

    /**
     * Get count of TrainingGrids in set
     * @return count
     */
    public int getCount() {
        return _trainingGrids.size();
    }

    /**
     * Get TrainingGrid at a specific index
     * @param index - index of TrainingGrid
     * @return TrainingGrid at index
     * @throws Exception
     */
    public TrainingGrid getGrid(int index) throws Exception {
        if (index < 0 || index >= _trainingGrids.size()) {
            throw new Exception(String.format(
                    "getGrid called on TrainingSetManager with invalid iindex, expected index to be within %d not %d",
                    _trainingGrids.size(),
                    index));
        }

        return _trainingGrids.get(index);
    }

    /**
     * Get the next TrainingGrid in the set (starts at index 0 and continuously loops)
     * @return TrainingGrid
     * @throws Exception
     */
    public TrainingGrid getNext() throws Exception {
        if (_trainingGrids.isEmpty()) {
            throw new Exception("getNext() called in TrainingDataManager on empty data set");
        }

        _index = (_index + 1) % _trainingGrids.size();
        return _trainingGrids.get(_index);
    }

    /**
     * Get the previous TrainingGrid in the set (continuously loops backward through training sets)
     * @return
     * @throws Exception
     */
    public TrainingGrid getPrevious() throws Exception {
        if (_trainingGrids.isEmpty()) {
            throw new Exception("getPrevious() called in TrainingDataManager on empty data set");
        }

        _index = (_index - 1) % _trainingGrids.size();
        if (_index < 0 ) { _index += _trainingGrids.size(); }
        return _trainingGrids.get(_index);
    }
}

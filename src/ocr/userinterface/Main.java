package ocr.userinterface;

/**
 * Contains Main Method for OCR
 * @author Jonathan Reimels
 * @version 1.0.0
 */
public class Main {
    /**
     * Run the GUI
     * @param args
     */
    public static void main(String[] args) {
        OcrGui gui = new OcrGui();
        gui.buildGui();
    }
}

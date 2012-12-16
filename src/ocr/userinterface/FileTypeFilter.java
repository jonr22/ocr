package ocr.userinterface;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Creates FileFilters to be used with FileChooser
 * Code from:
 * 	  http://www.codejava.net/java-se/swing/add-file-filter-for-jfilechooser-dialog
 */
public class FileTypeFilter extends FileFilter {
	// instance variables
	private String extension;
	private String description;

	/**
	 * Constructor
	 * @param extension - file extension (ie ".txt")
	 * @param description - file description (ie "Text Document")
	 */
	public FileTypeFilter(String extension, String description) {
		this.extension = extension;
		this.description = description;
	}

	/**
	 * Determine if file can be accepted
	 */
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}
		return file.getName().endsWith(extension);
	}

	/**
	 * File type display
	 */
	@Override
	public String getDescription() {
		return description + String.format(" (*%s)", extension);
	}
}


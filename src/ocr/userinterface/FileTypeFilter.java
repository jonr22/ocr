package ocr.userinterface;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Creates FileFilters to be used with FileChooser
 * Code from: 
 * 	  http://www.codejava.net/java-se/swing/add-file-filter-for-jfilechooser-dialog
 */
public class FileTypeFilter extends FileFilter {
	private String extension;
	private String description;

	public FileTypeFilter(String extension, String description) {
		this.extension = extension;
		this.description = description;
	}

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		}
		return file.getName().endsWith(extension);
	}

	@Override
	public String getDescription() {
		return description + String.format(" (*%s)", extension);
	}
}

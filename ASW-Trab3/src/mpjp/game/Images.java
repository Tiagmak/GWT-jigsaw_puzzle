package mpjp.game;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Images {
	static Set<String> extensions;
	static File imageDirectory;
	
	private static final String MPJP_RESOURCES = "test/mpjp/resources/";

	/**
	 * Current set of valid extensions
	 * @return set of extensions
	 */
	public static Set<String> getExtensions() {
		if (extensions == null) {
			extensions = new HashSet<>();
			extensions.add("jpg");
		}
		return extensions;
	}
	/**
	 * Current image directory
	 * @return imageDirectory
	 */
	public static File getImageDirectory() {
		if(imageDirectory == null) {
			imageDirectory = new File(MPJP_RESOURCES);
		}
		return imageDirectory;
	}
	
	/*
	 * Change image directory
	 */
	public static void setImageDirectory(File imageDirectory) {
			Images.imageDirectory = imageDirectory;
	}

	/**
	 * Add an extension to the current list
	 * @param Extension
	 */
	public static void addExtension(String Extension) {
		getExtensions().add(Extension);
	}

	/**
	 * A set of image names that can be used in MPJP jigsaw puzzles
	 * @return set
	 */
	static Set<String> getAvailableImages() {
		Set<String> tmp = new HashSet<>();

		for (File f : getImageDirectory().listFiles()) {
			String name = f.getName();
			for (String s : getExtensions()) {
				if (name.endsWith(s)) {
					tmp.add(name);
				}
			}
		}
		return tmp;
	}
}

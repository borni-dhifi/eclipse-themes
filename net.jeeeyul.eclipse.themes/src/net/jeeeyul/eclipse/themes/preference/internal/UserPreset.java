package net.jeeeyul.eclipse.themes.preference.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.jeeeyul.eclipse.themes.JThemesCore;
import net.jeeeyul.eclipse.themes.preference.preset.IJTPreset;

public class UserPreset implements IJTPreset {
	private Properties properties = new Properties();
	private File file;
	private String name;

	public UserPreset(File file) throws IOException {
		this.name = file.getName().substring(0, file.getName().length() - 4);
		this.file = file;
		FileInputStream is = new FileInputStream(file);
		properties.load(is);
		is.close();
	}

	public UserPreset(String name) {
		setName(name);
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void save() throws IOException {
		if (file != null && file.exists()) {
			file.delete();
		}
		file = new File(getPresetFolder(), this.getName() + ".epf");
		FileOutputStream os = new FileOutputStream(file);
		properties.store(os, "Jeeeyul's Eclipse Themes Custom Preset File");
		os.close();

		JThemesCore.getDefault().getPresetManager().invalidateUserPreset();
	}

	public void setName(String newName) {
		if (!isSafeName(newName)) {
			throw new IllegalArgumentException("Name must not contains: \\/:*?\"<>|");
		}
		this.name = newName;
	}

	public static boolean isSafeName(String name) {
		Matcher matcher = Pattern.compile("[\\/:*?\"<>|]").matcher(name);
		if (matcher.find()) {
			return false;
		} else {
			return true;
		}
	}

	public void delete() {
		if (file.exists()) {
			file.delete();
		}
		JThemesCore.getDefault().getPresetManager().invalidateUserPreset();
	}

	@Override
	public String getId() {
		return this.getName();
	}

	private File getPresetFolder() {
		return JThemesCore.getDefault().getPresetManager().getUserPresetFolder();
	}

}
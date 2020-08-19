package it.uniroma1.nlp.kb;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;

public class TextLoader
{
	private static String verbAtlasResourceFolderName;

	public static List<String> loadTxt(String path)
			throws IOException, URISyntaxException, MissingVerbAtlasResourceException
	{
		if (verbAtlasResourceFolderName == null)
			verbAtlasResourceFolderName = readVerbAtlasVersion() + "/";
		List<String> file = Files
				.readAllLines(Paths.get(ClassLoader.getSystemResource(verbAtlasResourceFolderName + path).toURI()));
		file.remove(0); // rimuovo la prima riga di ogni file, in quanto è sempre inutile
		return file;
	}

	public static String readVerbAtlasVersion() throws MissingVerbAtlasResourceException
	{
		for (String string : TextLoader.readDirectories())
			if (string.startsWith("VerbAtlas-"))
				return string;
		throw new MissingVerbAtlasResourceException("The VerbAtlas resource folder can not be found");
	}

	private static String[] readDirectories()
	{
		File file = new File("resources/");
		return file.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File current, String name)
			{
				return new File(current, name).isDirectory();
			}
		});
	}
}

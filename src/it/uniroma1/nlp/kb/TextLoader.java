package it.uniroma1.nlp.kb;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;

/**
 * Classe di utilità principalmente per leggere dei file di testo dalle
 * resources.
 * 
 * @author Salvi Marco
 */
public class TextLoader
{
	private static String verbAtlasResourceFolderName;

	/**
	 * Restituisce una lista contenente tutte le righe del file fornito in input.
	 * 
	 * @param nome del file da convertire in lista.
	 * @return List<Stirng> contenente tutte le righe del file
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws MissingVerbAtlasResourceException
	 */
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

	/**
	 * Restituisce una stringa che rappresenta la versione della risorsa VerbAtlas
	 * se presente.
	 * 
	 * @return String version.
	 * @throws MissingVerbAtlasResourceException
	 */
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

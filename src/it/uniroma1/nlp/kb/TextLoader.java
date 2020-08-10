package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class TextLoader
{
	public static List<String> loadTxt(String path) throws IOException, URISyntaxException
	{
		List<String> file = Files.readAllLines(Paths.get(ClassLoader.getSystemResource(path).toURI()));
		file.remove(0);	//rimuovo la prima riga di ogni file, in quanto è sempre inutile
		return file;
	}
}

package it.uniroma1.nlp.verbatlas;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import it.uniroma1.nlp.kb.exceptions.ResourceDateException;

public class VerbAtlasVersion
{
	// TODO dispone di una sola costante con valore determinato sulla base della
	// cartella da cui saranno caricati i file. Un oggetto VerbAtlasVersion dispone
	// di un metodo LocalDate getReleaseDate() che restituisce la data di rilascio
	// della versione (cablata all'interno della classe stessa)
	private String version;

	public VerbAtlasVersion()
	{
		File file = new File("resources/");
		String[] directories = file.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File current, String name)
			{
				return new File(current, name).isDirectory();
			}
		});

		for (String string : directories)
			if (string.startsWith("VerbAtlas"))
			{
				version = "V" + string.substring(string.indexOf("-") + 1);
				version = version.replace('.', '_');
				break;
			}
	}

	public LocalDate getReleaseDate() throws ResourceDateException
	{
		switch (version)
		{
		case "V1_0_2":
			return LocalDate.of(2020, 3, 23);
		case "V1_0_3":
			return LocalDate.of(2020, 7, 9);
		}
		throw new ResourceDateException("This version of the VerbAtlas resource does not have a known date of release");

	}

	public String getVersion()
	{
		return version;
	}

	@Override
	public String toString()
	{
		try
		{
			return "VerbAtlas resource " + version + ". Released "
					+ getReleaseDate().format(DateTimeFormatter.ISO_DATE);
		}
		catch (ResourceDateException e)
		{
			e.printStackTrace();
			return "VerbAtlas resource " + version + ". Released in unknown date";
		}
	}
}

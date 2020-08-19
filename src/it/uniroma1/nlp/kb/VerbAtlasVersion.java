package it.uniroma1.nlp.kb;

import java.io.File;

import java.io.FilenameFilter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;
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
		try
		{
			
			version = TextLoader.readVerbAtlasVersion().replace('.', '_');
			version = "V" + version.substring(version.indexOf("-") + 1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			version = "The VerbAtlas resource folder can not be found";
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
			return "VerbAtlas resource " + version + ". Released on "
					+ getReleaseDate().format(DateTimeFormatter.ISO_DATE) + ".";
		}
		catch (ResourceDateException e)
		{
			return "VerbAtlas resource " + version + ". Released date unknown.";
		}
	}
}

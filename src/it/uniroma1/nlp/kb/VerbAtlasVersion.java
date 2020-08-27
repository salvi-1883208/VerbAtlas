package it.uniroma1.nlp.kb;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import it.uniroma1.nlp.kb.exceptions.ResourceDateException;

/**
 * Classe per rappresentare la versione della risorsa VerbAtlas.
 * 
 * @author Salvi Marco
 */
public class VerbAtlasVersion
{
	private String version;

	/**
	 * Costruttore della classe.
	 */
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
			version = "The VerbAtlas resource folder cannot be found";
		}
	}

	/**
	 * Restituisce un oggetto LocalDate che rappresenta la data di rilascio della
	 * risorsa VerbAtlas.
	 * 
	 * @return LocalDate che rappresenta la data di rilascio della risorsa VerbAtlas
	 * @throws ResourceDateException
	 */
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

	/**
	 * Restituisce la versione della risorsa VerbAtlas come stringa.
	 * 
	 * @return La versione della risorsa VerbAtlas come stringa
	 */
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
			return version + ". Released date unknown.";
		}
	}
}

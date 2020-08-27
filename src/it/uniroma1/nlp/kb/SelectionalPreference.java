package it.uniroma1.nlp.kb;

import java.io.IOException;

import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

/**
 * Classe che rappresenta una preferenza di selezione.
 * 
 * @author Salvi Marco
 */
public class SelectionalPreference extends RolePreference
{
	/**
	 * Costruttore della classe.
	 * 
	 * @param preferenceId
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws VerbAtlasException
	 */
	public SelectionalPreference(PreferenceID preferenceId) throws IOException, URISyntaxException, VerbAtlasException
	{
		super(preferenceId);
	}
}

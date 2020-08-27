package it.uniroma1.nlp.kb;

import java.io.IOException;

import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

/**
 * Classe che rappresenta un argomento ombra.
 * 
 * @author Salvi Marco
 */
public class ShadowArgument extends RolePreference
{
	/**
	 * Costruttore della classe
	 * 
	 * @param babelId
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws VerbAtlasException
	 */
	public ShadowArgument(BabelNetSynsetID babelId) throws IOException, URISyntaxException, VerbAtlasException
	{
		super(babelId);
	}
}

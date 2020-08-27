package it.uniroma1.nlp.kb;

import java.io.IOException;

import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

/**
 * Classe che rappresenta un argomento implicito.
 * 
 * @author Salvi Marco
 */
public class ImplicitArgument extends RolePreference
{
	/**
	 * Costruttore della classe.
	 * 
	 * @param BabelNetSynsetID babelId
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws VerbAtlasException
	 */
	public ImplicitArgument(BabelNetSynsetID babelId) throws IOException, URISyntaxException, VerbAtlasException
	{
		super(babelId);
	}
}

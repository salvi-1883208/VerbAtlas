package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.BabelNetSynsetIDToWordNetSynsetIDException;
import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

/**
 * Classe che rappresenta un ID di WordNet.
 * 
 * @author Salvi Marco
 */
public class WordNetSynsetID extends ResourceID
{
	private BabelNetSynsetID babelNetId;

	/**
	 * Costruttore della classe.
	 * 
	 * @param String id di WordNet
	 */
	public WordNetSynsetID(String id)
	{
		super(id);
	}

	/**
	 * Restituisce il BabelNetSynsetID associato al WordNetSynsetID
	 * 
	 * @return BabelNetSynsetID associato al WordNetSynsetID
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws VerbAtlasException
	 */
	public BabelNetSynsetID toBabelID() throws IOException, URISyntaxException, VerbAtlasException
	{
		if (babelNetId == null)
		{
			for (String line : TextLoader.loadTxt("bn2wn.tsv"))
				if (line.endsWith(getId()))
				{
					babelNetId = new BabelNetSynsetID(line.substring(0, line.indexOf("\t")));
					return babelNetId;
				}
			throw new BabelNetSynsetIDToWordNetSynsetIDException(
					"Cannot convert WordNetSynsetID '" + getId() + "' to BabelNetSynsetID");
		}
		return babelNetId;
	}

}

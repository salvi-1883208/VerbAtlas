package it.uniroma1.nlp.kb;

import java.io.IOException;

import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.BabelNetSynsetIDToVerbAtlasFrameIDException;
import it.uniroma1.nlp.kb.exceptions.BabelNetSynsetIDToWordNetSynsetIDException;
import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

/**
 * Classe che rappresenta un ID di BabelNet
 * 
 * @author Salvi Marco
 */
public class BabelNetSynsetID extends ResourceID
{
	private WordNetSynsetID wordNetId;
	private VerbAtlasFrameID verbAtlasId;

	/**
	 * Costruttore della classe
	 * 
	 * @param String id
	 */
	public BabelNetSynsetID(String id)
	{
		super(id);
	}

	/**
	 * Restituisce il WordNetSynsetID associato al BabelNetSynsetID
	 * 
	 * @return WordNetSynsetID associato al BabelNetSynsetID
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws VerbAtlasException
	 */
	public WordNetSynsetID toWordNetID() throws IOException, URISyntaxException, VerbAtlasException
	{
		if (wordNetId == null)
		{
			for (String line : TextLoader.loadTxt("bn2wn.tsv"))
				if (line.startsWith(getId()))
				{
					wordNetId = new WordNetSynsetID(line.substring(line.indexOf("\t") + 1));
					return wordNetId;
				}
			throw new BabelNetSynsetIDToWordNetSynsetIDException(
					"Cannot convert BabelNetSynsetID '" + getId() + "' to WordNetSynsetID");
		}
		return wordNetId;
	}

	/**
	 * Restituisce il VerbAtlasFrameID al quale è associato.
	 * 
	 * @return VerbAtlasFrameID associato al BabelNetSynsetID
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws VerbAtlasException
	 */
	public VerbAtlasFrameID toVerbAtlasID() throws IOException, URISyntaxException, VerbAtlasException
	{
		if (verbAtlasId == null)
		{
			for (String line : TextLoader.loadTxt("VA_bn2va.tsv"))
				if (line.contains(this.getId()))
				{
					verbAtlasId = new VerbAtlasFrameID(line.substring(line.indexOf("\t") + 1));
					return verbAtlasId;
				}
			throw new BabelNetSynsetIDToVerbAtlasFrameIDException(
					"Cannot convert BabelNetSynsetID '" + getId() + "' to VerbAtlasFrameID");
		}
		return verbAtlasId;
	}

}

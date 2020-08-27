package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.PropBankPredicateIDToVerbAtlasIDException;
import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

/**
 * Classe che rappresenta un PropBankPredicateID.
 * 
 * @author Salvi Marco
 */
public class PropBankPredicateID extends ResourceID
{
	private VerbAtlasFrameID verbAtlasId;

	/**
	 * Costruttore della classe.
	 * 
	 * @param String id
	 */
	public PropBankPredicateID(String id)
	{
		super(id);
	}

	/**
	 * Restituisce il VerbAtlasFrameID associato al PropBankPredicateID
	 * 
	 * @return VerbAtlasFrameID associato al PropBankPredicateID
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws VerbAtlasException
	 */
	public VerbAtlasFrameID toVerbAtlasID() throws IOException, URISyntaxException, VerbAtlasException
	{
		if (verbAtlasId == null)
		{
			for (String line : TextLoader.loadTxt("pb2va.tsv"))
				if (line.startsWith(getId()))
				{
					verbAtlasId = new VerbAtlasFrameID(line.substring(line.indexOf(">") + 1, line.indexOf("\t")));
					return verbAtlasId;
				}
			throw new PropBankPredicateIDToVerbAtlasIDException(
					"Cannot convert PropBankPredicateID '" + getId() + "' to VerbAtlasFrameID");
		}
		return verbAtlasId;
	}
}

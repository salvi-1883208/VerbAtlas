package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.PropBankPredicateIDToVerbAtlasIDException;
import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

public class VerbAtlasFrameID extends ResourceID
{
	private PropBankPredicateID propBankId;

	public VerbAtlasFrameID(String id)
	{
		super(id);
	}

	public PropBankPredicateID toPropBankID()
			throws IOException, URISyntaxException, VerbAtlasException
	{
		if (propBankId == null)
		{
			for (String line : TextLoader.loadTxt("bn2wn.tsv"))
				if (line.substring(line.indexOf(">") + 1, line.indexOf("\t")).equals(getId()))
				{
					propBankId = new PropBankPredicateID(line.substring(0, line.indexOf(">")));
					return propBankId;
				}
			throw new PropBankPredicateIDToVerbAtlasIDException(
					"Cannot convert VerbAtlasFrameID '" + getId() + "' to PropBankPredicateID");
		}
		return propBankId;
	}
}

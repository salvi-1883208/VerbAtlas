package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.PropBankPredicateIDToVerbAtlasIDException;

public class VerbAtlasFrameID extends ResourceID
{
	private PropBankPredicateID propBankId;

	public VerbAtlasFrameID(String id)
	{
		super(id);
	}

	public PropBankPredicateID toPropBankID()
			throws IOException, URISyntaxException, PropBankPredicateIDToVerbAtlasIDException
	{
		if (propBankId == null)
		{
			for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/bn2wn.tsv"))
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

package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.PropBankPredicateIDToVerbAtlasIDException;

public class PropBankPredicateID extends ResourceID
{
	public PropBankPredicateID(String id)
	{
		super(id);
	}

	// metodo per convertire da propbank a verbatlas (pb2va.tsv)
	public VerbAtlasFrameID toVerbAtlasID()
			throws IOException, URISyntaxException, PropBankPredicateIDToVerbAtlasIDException
	{
		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/pb2va.tsv"))
			if (line.startsWith(getId()))
				return new VerbAtlasFrameID(line.substring(line.indexOf(">") + 1, line.indexOf("\t")));
		throw new PropBankPredicateIDToVerbAtlasIDException(
				"Cannot convert PropBankPredicateID '" + getId() + "' to VerbAtlasFrameID");
	}
}

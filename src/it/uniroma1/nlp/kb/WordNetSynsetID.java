package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.BabelNetSynsetIDToWordNetSynsetIDException;

public class WordNetSynsetID extends ResourceID
{
	public WordNetSynsetID(String id)
	{
		super(id);
	}

	public BabelNetSynsetID toBabelID()
			throws IOException, URISyntaxException, BabelNetSynsetIDToWordNetSynsetIDException
	{
		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/bn2wn.tsv"))
			if (line.endsWith(getId()))
				return new BabelNetSynsetID(line.substring(0, line.indexOf("\t")));
		throw new BabelNetSynsetIDToWordNetSynsetIDException(
				"Cannot convert WordNetSynsetID '" + getId() + "' to BabelNetSynsetID");
	}

}

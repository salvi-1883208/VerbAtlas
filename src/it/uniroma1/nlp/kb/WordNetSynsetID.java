package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

public class WordNetSynsetID extends ResourceID
{
	public WordNetSynsetID(String id)
	{
		super(id);
	}

	public BabelNetSynsetID toBabelID() throws IOException, URISyntaxException
	{
		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/bn2wn.tsv"))
			if (line.contains(this.getId()))
				return new BabelNetSynsetID(line.substring(0, line.indexOf("\t")));
		System.out.println("WORDNET TO BABELNET ERROR");
		return null; // Throw exception se id wordnet non ha un corrispondente babelnet
	}

	// usare super.getId() per ottenere l'id
}

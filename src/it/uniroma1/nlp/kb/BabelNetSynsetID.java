package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

public class BabelNetSynsetID extends ResourceID
{
	public BabelNetSynsetID(String id)
	{
		super(id);
	}

	public WordNetSynsetID toWordNetID() throws IOException, URISyntaxException
	{
		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/bn2wn.tsv"))
			if (line.contains(this.getId()))
				return new WordNetSynsetID(line.substring(line.indexOf("\t") + 1));
		System.out.println("BABELNET TO WORDNET ERROR");
		return null; // Throw exception se id wordnet non ha un corrispondente babelnet
	}

	public VerbAtlasFrameID toVerbAtlasID() throws IOException, URISyntaxException
	{
		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/VA_bn2va.tsv"))
			if (line.contains(this.getId()))
				return new VerbAtlasFrameID(line.substring(line.indexOf("\t") + 1));
		System.out.println("BABELNET TO VERBATLAS ERROR");
		return null; // Throw exception
	}

	// usare super.getId() per ottenere l'id
}

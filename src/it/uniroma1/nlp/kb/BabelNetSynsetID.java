package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.BabelNetSynsetIDToVerbAtlasFrameIDException;
import it.uniroma1.nlp.kb.exceptions.BabelNetSynsetIDToWordNetSynsetIDException;

public class BabelNetSynsetID extends ResourceID
{
	public BabelNetSynsetID(String id)
	{
		super(id);
	}

	public WordNetSynsetID toWordNetID()
			throws IOException, URISyntaxException, BabelNetSynsetIDToWordNetSynsetIDException
	{
		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/bn2wn.tsv"))
			if (line.startsWith(getId()))
				return new WordNetSynsetID(line.substring(line.indexOf("\t") + 1));
		throw new BabelNetSynsetIDToWordNetSynsetIDException(
				"Cannot convert BabelNetSynsetID '" + getId() + "' to WordNetSynsetID");
	}

	public VerbAtlasFrameID toVerbAtlasID()
			throws IOException, URISyntaxException, BabelNetSynsetIDToVerbAtlasFrameIDException
	{
		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/VA_bn2va.tsv"))
			if (line.contains(this.getId()))
				return new VerbAtlasFrameID(line.substring(line.indexOf("\t") + 1));
		throw new BabelNetSynsetIDToVerbAtlasFrameIDException(
				"Cannot convert BabelNetSynsetID '" + getId() + "' to VerbAtlasFrameID");
	}

}

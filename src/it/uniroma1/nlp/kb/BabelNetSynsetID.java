package it.uniroma1.nlp.kb;

import java.io.IOException;

import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.BabelNetSynsetIDToVerbAtlasFrameIDException;
import it.uniroma1.nlp.kb.exceptions.BabelNetSynsetIDToWordNetSynsetIDException;

public class BabelNetSynsetID extends ResourceID
{
	private WordNetSynsetID wordNetId;
	private VerbAtlasFrameID verbAtlasId;

	public BabelNetSynsetID(String id)
	{
		super(id);
	}

	public WordNetSynsetID toWordNetID()
			throws IOException, URISyntaxException, BabelNetSynsetIDToWordNetSynsetIDException
	{
		if (wordNetId == null)
		{
			for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/bn2wn.tsv"))
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

	public VerbAtlasFrameID toVerbAtlasID()
			throws IOException, URISyntaxException, BabelNetSynsetIDToVerbAtlasFrameIDException
	{
		if (verbAtlasId == null)
		{
			for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/VA_bn2va.tsv"))
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

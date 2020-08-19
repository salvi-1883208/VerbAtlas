package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;

public class ShadowArgument extends RolePreference
{

	public ShadowArgument(BabelNetSynsetID babelId)
			throws MissingVerbAtlasResourceException, IOException, URISyntaxException
	{
		super(babelId);
	}

}

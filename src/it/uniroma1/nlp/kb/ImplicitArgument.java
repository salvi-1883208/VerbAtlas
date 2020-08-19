package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;

public class ImplicitArgument extends RolePreference
{
	public ImplicitArgument(BabelNetSynsetID babelId)
			throws MissingVerbAtlasResourceException, IOException, URISyntaxException
	{
		super(babelId);
	}
}

package it.uniroma1.nlp.kb;

import java.io.IOException;

import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

public class ShadowArgument extends RolePreference
{
	public ShadowArgument(BabelNetSynsetID babelId) throws IOException, URISyntaxException, VerbAtlasException
	{
		super(babelId);
	}
}

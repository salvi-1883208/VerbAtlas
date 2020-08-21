package it.uniroma1.nlp.kb;

import java.io.IOException;

import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

public class SelectionalPreference extends RolePreference
{
	public SelectionalPreference(PreferenceID preferenceId) throws IOException, URISyntaxException, VerbAtlasException
	{
		super(preferenceId);
	}
}

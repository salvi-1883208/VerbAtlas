package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;

public class SelectionalPreference extends RolePreference
{
	public SelectionalPreference(PreferenceID preferenceId)
			throws MissingVerbAtlasResourceException, IOException, URISyntaxException
	{
		super(preferenceId);
	}
}

package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;

public abstract class RolePreference implements Comparable<RolePreference>
{
	private PreferenceID preferenceId;
	private String preferenceName;
	private BabelNetSynsetID babelId;

	public RolePreference(PreferenceID preferenceId)
			throws MissingVerbAtlasResourceException, IOException, URISyntaxException
	{
		this.preferenceId = preferenceId;

		for (String line : TextLoader.loadTxt("VA_preference_ids.tsv"))
			if (line.startsWith(preferenceId.getId()))
			{
				babelId = new BabelNetSynsetID(line.substring(line.indexOf("\t") + 1, line.lastIndexOf("\t")));
				preferenceName = line.substring(line.lastIndexOf("\t") + 1);
				break;
			}
	}

	public RolePreference(BabelNetSynsetID babelId)
			throws MissingVerbAtlasResourceException, IOException, URISyntaxException
	{
		this.babelId = babelId;

		for (String line : TextLoader.loadTxt("VA_preference_ids.tsv"))
			if (line.contains(babelId.getId()))
			{
				preferenceId = new PreferenceID(line.substring(0, line.indexOf("\t")));
				preferenceName = line.substring(line.lastIndexOf("\t") + 1);
				break;
			}
	}

	public PreferenceID getId()
	{
		return preferenceId;
	}

	public String getName()
	{
		return preferenceName;
	}

	public BabelNetSynsetID getBabelSynsetId()
	{
		return babelId;
	}

	@Override
	public int compareTo(RolePreference rp)
	{
		return preferenceName.compareTo(rp.getName());
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;

		RolePreference rp = (RolePreference) o;
		return rp.getId().equals(preferenceId);
	}

	@Override
	public int hashCode()
	{
		return preferenceId.hashCode();
	}

	@Override
	public String toString()
	{
		return preferenceName;
	}
}

package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;

public class SelectionalPreference implements Comparable<SelectionalPreference>
{
	private Role role;
	private PreferenceID preferenceId;
	private String preferenceName;
	private BabelNetSynsetID babelId;

	public SelectionalPreference(Role role, PreferenceID preferenceId)
			throws IOException, URISyntaxException, MissingVerbAtlasResourceException
	{
		this.role = role;
		this.preferenceId = preferenceId;

		for (String line : TextLoader.loadTxt("VA_preference_ids.tsv"))
			if (line.startsWith(preferenceId.getId()))
			{
				babelId = new BabelNetSynsetID(line.substring(line.indexOf("\t") + 1, line.lastIndexOf("\t")));
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

	public Role getRole()
	{
		return role;
	}

	@Override
	public int compareTo(SelectionalPreference sp)
	{
		return preferenceName.compareTo(sp.getName());
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
			return true;
		if (o == null || o.getClass() != this.getClass())
			return false;

		SelectionalPreference sp = (SelectionalPreference) o;
		return sp.getId().equals(getId());
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

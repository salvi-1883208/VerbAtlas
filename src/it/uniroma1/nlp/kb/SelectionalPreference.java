package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;

import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;

public class SelectionalPreference implements Comparable<SelectionalPreference>
{
	// TODO implementare equals e hashcode
	private Role role;
	private PreferenceID preferenceId;
	private String preferenceName;
	private BabelNetSynsetID babelId;

	public SelectionalPreference(Role role, PreferenceID preferenceId) throws IOException, URISyntaxException
	{
		this.role = role;
		this.preferenceId = preferenceId;

		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/VA_preference_ids.tsv"))
		{
			if (line.substring(0, line.indexOf("\t")).equals(preferenceId.getId()))
			{
				babelId = new BabelNetSynsetID(line.substring(line.indexOf("\t") + 1, line.lastIndexOf("\t")));
				preferenceName = line.substring(line.lastIndexOf("\t") + 1);
				break;
			}
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
	public String toString()
	{
		return preferenceName;
	}
}

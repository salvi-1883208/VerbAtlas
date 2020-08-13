package it.uniroma1.nlp.verbatlas;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import it.uniroma1.nlp.kb.BabelNetSynsetID;
import it.uniroma1.nlp.kb.Frame;
import it.uniroma1.nlp.kb.PreferenceID;
import it.uniroma1.nlp.kb.ResourceID;
import it.uniroma1.nlp.kb.SelectionalPreference;
import it.uniroma1.nlp.kb.TextLoader;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;

public class VerbAtlasSynsetFrame implements Frame
{
	private VerbAtlasFrame frame; // Frame di cui questo synset fa parte
	private BabelNetSynsetID synsetId;
	private TreeSet<Role> roles;

	public VerbAtlasSynsetFrame(VerbAtlasFrame frame, BabelNetSynsetID synsetId, TreeSet<Role> roles)
			throws IOException, URISyntaxException
	{
		this.frame = frame;
		this.synsetId = synsetId;
		this.roles = roles;

		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/VA_bn2sp.tsv"))
			if (line.substring(0, line.indexOf("\t")).equals(synsetId.getId()))
				for (Role role : roles)
					if (line.contains(role.getType()))
					{
						int start = line.indexOf(role.getType()) + role.getType().length() + 1;
						int end = line.substring(line.indexOf(role.getType()) + role.getType().length() + 1)
								.indexOf("\t") + line.indexOf(role.getType()) + role.getType().length() + 1;
						String strRoles = line.substring(start, end);
						for (String strId : strRoles.split("\\|"))
							role.addSelectionalPreference(new SelectionalPreference(role, new PreferenceID(strId)));
					}
	}

	public VerbAtlasFrame toFrame()
	{
		return frame;
	}

	@Override
	public String getName()
	{
		return frame.getName();
	}

	@Override
	public ResourceID getId()
	{
		return synsetId;
	}

	@Override
	public TreeSet<Role> getRoles()
	{
		return roles;
	}

	@Override
	public String toString()
	{
		List<String> strings = new ArrayList<String>();
		for (Role role : roles)
			strings.add(role.toString());
		return "BabelNetSynsetID: " + synsetId + "\nRoles: \n\t" + String.join(",\n\t", strings);
	}

}

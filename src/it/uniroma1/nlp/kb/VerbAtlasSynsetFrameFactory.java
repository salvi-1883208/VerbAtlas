package it.uniroma1.nlp.kb;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.TreeSet;

import it.uniroma1.nlp.verbatlas.VerbAtlas;
import it.uniroma1.nlp.verbatlas.VerbAtlasSynsetFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;

public class VerbAtlasSynsetFrameFactory
{
	public VerbAtlasSynsetFrame buildSynsetFrame(BabelNetSynsetID id, VerbAtlasFrame frame, TreeSet<Role> roles)
			throws IOException, URISyntaxException
	{
		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/VA_bn2sp.tsv"))
			if (line.substring(0, line.indexOf("\t")).equals(id.getId()))
				for (Role role : roles)
					if (line.contains(role.getType()))
					{
						int start = line.indexOf(role.getType()) + role.getType().length() + 1;
						int end = line.substring(line.indexOf(role.getType()) + role.getType().length() + 1)
								.indexOf("\t") + line.indexOf(role.getType()) + role.getType().length() + 1;
						String strRoles = "";
						if (end < start)
							strRoles = line.substring(start);
						else
							strRoles = line.substring(start, end);
						for (String strId : strRoles.split("\\|"))
							role.addSelectionalPreference(new SelectionalPreference(role, new PreferenceID(strId)));
					}
		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/wn2lemma.tsv"))
			if (line.contains(id.toWordNetID().getId()))
				return new VerbAtlasSynsetFrame(frame, id, roles, line.substring(line.indexOf("\t") + 1));
		return null; // TODO throw exception
	}

	public VerbAtlasSynsetFrame buildSynsetFrame(WordNetSynsetID id, VerbAtlasFrame frame, TreeSet<Role> roles)
			throws IOException, URISyntaxException
	{
		return buildSynsetFrame(id.toBabelID(), frame, roles);
	}
}
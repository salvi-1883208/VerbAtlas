package it.uniroma1.nlp.kb;

import java.io.IOException;

import java.net.URISyntaxException;
import java.util.TreeSet;

import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;
import it.uniroma1.nlp.kb.exceptions.WordNetIDToLemmaException;
import it.uniroma1.nlp.verbatlas.VerbAtlasSynsetFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;

public class VerbAtlasSynsetFrameFactory
{
	public VerbAtlasSynsetFrame buildSynsetFrame(BabelNetSynsetID id, VerbAtlasFrame frame, TreeSet<Role> roles)
			throws VerbAtlasException, IOException, URISyntaxException
	{
		// Selectional Preferences
		for (String line : TextLoader.loadTxt("VA_bn2sp.tsv"))
			if (line.startsWith(id.getId()))
			{
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
							role.addSelectionalPreference(new SelectionalPreference(new PreferenceID(strId)));
					}
				break;
			}

		// Implicit Arguments
		for (String line : TextLoader.loadTxt("VA_bn2implicit.tsv"))
			if (line.startsWith(id.getId()))
				for (Role role : roles)
					if (line.contains(role.getType()))
					{
						int start = line.indexOf(role.getType()) + role.getType().length() + 1;
						int end = line.substring(line.indexOf(role.getType()) + role.getType().length() + 1)
								.indexOf("\t") + line.indexOf(role.getType()) + role.getType().length() + 1;
						String strImplicit = "";
						if (end < start)
							strImplicit = line.substring(start);
						else
							strImplicit = line.substring(start, end);
						for (String strId : strImplicit.split("\\|"))
							for (String line_ : TextLoader.loadTxt("VA_preference_ids.tsv"))
								if (line_.contains(strId))
									role.addImplicitArgument(new ImplicitArgument(new BabelNetSynsetID(
											line_.substring(line_.indexOf("\t") + 1, line_.lastIndexOf("\t")))));
					}

		// TODO Shadow Arguments
		for (String line : TextLoader.loadTxt("VA_bn2shadow.tsv"))
			if (line.startsWith(id.getId()))
				for (Role role : roles)
					if (line.contains(role.getType()))
					{
						int start = line.indexOf(role.getType()) + role.getType().length() + 1;
						int end = line.substring(line.indexOf(role.getType()) + role.getType().length() + 1)
								.indexOf("\t") + line.indexOf(role.getType()) + role.getType().length() + 1;
						String strShadow = "";
						if (end < start)
							strShadow = line.substring(start);
						else
							strShadow = line.substring(start, end);
						for (String strId : strShadow.split("\\|"))
							for (String line_ : TextLoader.loadTxt("VA_preference_ids.tsv"))
								if (line_.contains(strId))
									role.addShadowArgument(new ShadowArgument(new BabelNetSynsetID(
											line_.substring(line_.indexOf("\t") + 1, line_.lastIndexOf("\t")))));
					}

		for (String line : TextLoader.loadTxt("wn2lemma.tsv"))
			if (line.startsWith(id.toWordNetID().getId()))
				return new VerbAtlasSynsetFrame(frame, id, roles, line.substring(line.indexOf("\t") + 1));
		throw new WordNetIDToLemmaException("WordNetID '" + id.toWordNetID() + "' does not exist");
	}
}

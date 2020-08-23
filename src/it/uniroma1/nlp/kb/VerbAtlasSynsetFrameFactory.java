package it.uniroma1.nlp.kb;

import java.io.IOException;

import java.net.URISyntaxException;
import java.util.TreeSet;

import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;
import it.uniroma1.nlp.kb.exceptions.RoleNotFocalizedOnSynsetException;
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
		setArguments(id, roles, "VA_bn2sp.tsv");
		setArguments(id, roles, "VA_bn2implicit.tsv");
		setArguments(id, roles, "VA_bn2shadow.tsv");

		for (String line : TextLoader.loadTxt("wn2lemma.tsv"))
			if (line.startsWith(id.toWordNetID().getId()))
				return new VerbAtlasSynsetFrame(frame, id, roles, line.substring(line.indexOf("\t") + 1));
		throw new WordNetIDToLemmaException("WordNetID '" + id.toWordNetID() + "' does not exist");
	}

//	private void setArguments(BabelNetSynsetID id, TreeSet<Role> roles, String fileName)
//			throws RoleNotFocalizedOnSynsetException, IOException, URISyntaxException, VerbAtlasException
//	{
//		for (String line : TextLoader.loadTxt(fileName))
//			if (line.startsWith(id.getId()))
//				for (Role role : roles)
//					if (line.contains(role.getType()))
//					{
//						int start = line.indexOf(role.getType()) + role.getType().length() + 1;
//						int end = line.substring(line.indexOf(role.getType()) + role.getType().length() + 1)
//								.indexOf("\t") + line.indexOf(role.getType()) + role.getType().length() + 1;
//						String str = "";
//						if (end < start)
//							str = line.substring(start);
//						else
//							str = line.substring(start, end);
//						for (String strId : str.split("\\|"))
//							switch (fileName)
//							{
//							case "VA_bn2sp.tsv":
//								role.addSelectionalPreference(new SelectionalPreference(new PreferenceID(strId)));
//								break;
//							case "VA_bn2implicit.tsv":
//								role.addImplicitArgument(new ImplicitArgument(new BabelNetSynsetID(strId)));
//								break;
//							case "VA_bn2shadow.tsv":
//								role.addShadowArgument(new ShadowArgument(new BabelNetSynsetID(strId)));
//								break;
//							}
//					}
//	}

	private void setArguments(BabelNetSynsetID id, TreeSet<Role> roles, String fileName)
			throws IOException, URISyntaxException, VerbAtlasException
	{
		for (String line : TextLoader.loadTxt(fileName))
			if (line.startsWith(id.getId()))
			{
				String role = "";
				for (String str : line.split("\t"))
				{
					if (Character.isUpperCase(str.charAt(0)))
						role = str;
					else if (str.contains("|"))
						for (String strId : str.split("\\|"))
							addRole(fileName, roles, role, strId);
					else if (!role.isEmpty())
						addRole(fileName, roles, role, str);
				}
			}
	}

	private void addRole(String fileName, TreeSet<Role> roles, String roleName, String strId)
			throws IOException, URISyntaxException, VerbAtlasException
	{
		boolean containsRole = false;
		for (Role r : roles)
			if (containsRole = r.getType().equals(roleName))
				switch (fileName)
				{
				case "VA_bn2sp.tsv":
					r.addSelectionalPreference(new SelectionalPreference(new PreferenceID(strId)));
					break;
				case "VA_bn2implicit.tsv":
					r.addImplicitArgument(new ImplicitArgument(new BabelNetSynsetID(strId)));
					break;
				case "VA_bn2shadow.tsv":
					r.addShadowArgument(new ShadowArgument(new BabelNetSynsetID(strId)));
					break;
				}
		if (!containsRole)
			switch (fileName)
			{
//			case "VA_bn2sp.tsv":
//				Role sp = new Role(roleName);
//				sp.addSelectionalPreference(new SelectionalPreference(new PreferenceID(strId)));
//				roles.add(sp);
//				break;
			case "VA_bn2implicit.tsv":
				Role implicit = new Role(roleName);
				implicit.addImplicitArgument(new ImplicitArgument(new BabelNetSynsetID(strId)));
				roles.add(implicit);
				break;
			case "VA_bn2shadow.tsv":
				Role shadow = new Role(roleName);
				shadow.addShadowArgument(new ShadowArgument(new BabelNetSynsetID(strId)));
				roles.add(shadow);
				break;
			}
	}
}

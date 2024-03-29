package it.uniroma1.nlp.kb;

import java.io.IOException;

import java.net.URISyntaxException;
import java.util.TreeSet;

import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;
import it.uniroma1.nlp.kb.exceptions.WordNetIDToLemmaException;
import it.uniroma1.nlp.verbatlas.VerbAtlasSynsetFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;

/**
 * Factory per costruire istanze di VerbAtlasSynsetFrame
 * 
 * @author Salvi Marco
 */
public class VerbAtlasSynsetFrameFactory
{
	/**
	 * Restituisce un VerbAtlasSynsetFrame costruito con i parametri forniti in
	 * input.
	 * 
	 * @param id    del Synset
	 * @param frame del quale questo synset fa parte
	 * @param roles del synset
	 * @return VerbAtlasSynsetFrame
	 * @throws VerbAtlasException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
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

package it.uniroma1.nlp.kb;

import java.io.IOException;

import java.net.URISyntaxException;

import it.uniroma1.nlp.kb.exceptions.ResourceIDToRoleException;
import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

/**
 * Classe astratta che serve a rappresentare i vari tipi di preferenze per un
 * ruolo (Shadow, Implicit e Selectional).
 * 
 * @author Salvi Marco
 */
public abstract class RolePreference implements Comparable<RolePreference>
{
	private PreferenceID preferenceId;
	private String preferenceName = "";
	private BabelNetSynsetID babelId;

	private RolePreference(ResourceID id) throws VerbAtlasException, IOException, URISyntaxException
	{
		// Questo if è inutile, ma lo lascio per sicurezza
		if (!(id instanceof BabelNetSynsetID) && !(id instanceof PreferenceID))
			throw new ResourceIDToRoleException("Cannot convert from id '" + id + "' to a role");

		if (id instanceof BabelNetSynsetID)
			babelId = (BabelNetSynsetID) id;
		else
			preferenceId = (PreferenceID) id;

		for (String line : TextLoader.loadTxt("VA_preference_ids.tsv"))
			if (line.contains(id.getId()))
			{
				if (id instanceof PreferenceID)
					babelId = new BabelNetSynsetID(line.substring(line.indexOf("\t") + 1, line.lastIndexOf("\t")));
				else
					preferenceId = new PreferenceID(line.substring(0, line.indexOf("\t")));
				preferenceName = line.substring(line.lastIndexOf("\t") + 1);
				break;
			}
	}

	/**
	 * Costruttore della classe.
	 * 
	 * @param BabaleNetSynsetID id
	 * @throws VerbAtlasException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public RolePreference(BabelNetSynsetID id) throws VerbAtlasException, IOException, URISyntaxException
	{
		this((ResourceID) id);
	}

	/**
	 * Costruttore della classe.
	 * 
	 * @param PreferenceID id
	 * @throws VerbAtlasException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public RolePreference(PreferenceID id) throws VerbAtlasException, IOException, URISyntaxException
	{
		this((ResourceID) id);
	}

	/**
	 * Restituisce il PreferenceID della preferenza.
	 * 
	 * @return PreferenceID
	 */
	public PreferenceID getId()
	{
		return preferenceId;
	}

	/**
	 * Restituisce il nome della preferenza.
	 * 
	 * @return String nome
	 */
	public String getName()
	{
		return preferenceName;
	}

	/**
	 * Restituisce il BabelNetSynsetID della preferenza.
	 * 
	 * @return BabelNetSynsetID
	 */
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
		return rp.getBabelSynsetId().equals(babelId);
	}

	@Override
	public int hashCode()
	{
		return babelId.hashCode();
	}

	@Override
	public String toString()
	{
		if (preferenceName != "")
			return preferenceName;
		return babelId.toString();
	}
}

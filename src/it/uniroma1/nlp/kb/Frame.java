package it.uniroma1.nlp.kb;

import java.util.TreeSet;

import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;

/**
 * Interfaccia implementata da VerbAtlasFrame e da VerbAtlasSynsetFrame.
 * 
 * @author Salvi Marco
 */
public interface Frame
{
	/**
	 * Restituisce il nome del Frame come stringa.
	 * 
	 * @return String nome
	 */
	public String getName();

	/**
	 * Restituisce l'ID del Frame.
	 * 
	 * @return
	 */
	public ResourceID getId();

	/**
	 * Restituisce la sequenza ordinata dei ruoli del Frame.
	 * 
	 * @return TreeSet<Role> roles
	 */
	public TreeSet<Role> getRoles();
}

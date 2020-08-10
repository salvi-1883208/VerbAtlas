package it.uniroma1.nlp.kb;

import java.util.TreeSet;

import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;

public interface Frame
{
	public String getName();

	public ResourceID getId();

	public TreeSet<Role> getRoles();
}

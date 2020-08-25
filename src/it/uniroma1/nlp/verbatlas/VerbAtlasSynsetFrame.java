package it.uniroma1.nlp.verbatlas;

import java.util.TreeSet;
import java.util.stream.Collectors;

import it.uniroma1.nlp.kb.BabelNetSynsetID;
import it.uniroma1.nlp.kb.Frame;
import it.uniroma1.nlp.kb.ResourceID;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;

public class VerbAtlasSynsetFrame implements Frame
{
	private VerbAtlasFrame frame; // Frame di cui questo synset fa parte
	private BabelNetSynsetID synsetId;
	private TreeSet<Role> roles;
	private String name;

	public VerbAtlasSynsetFrame(VerbAtlasFrame frame, BabelNetSynsetID synsetId, TreeSet<Role> roles, String name)
	{
		this.frame = frame;
		this.synsetId = synsetId;
		this.roles = roles;
		this.name = name;

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
		return "\t\t\t\tFrame: " + frame.getName() + "\t\tSynset: " + name + "\nVerbAtlas Frame ID: "
				+ frame.getId().getId() + "\nBabelNetSynsetID: " + synsetId + "\nRoles: \n\t"
				+ String.join(",\n\t", roles.stream().filter(x -> x.toString() != "").map(x -> x.toString())
						.collect(Collectors.toList()));
	}

}

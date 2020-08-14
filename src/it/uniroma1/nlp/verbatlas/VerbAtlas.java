package it.uniroma1.nlp.verbatlas;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;
import it.uniroma1.nlp.kb.BabelNetSynsetID;
import it.uniroma1.nlp.kb.Frame;
import it.uniroma1.nlp.kb.ResourceID;
import it.uniroma1.nlp.kb.SelectionalPreference;
import it.uniroma1.nlp.kb.TextLoader;
import it.uniroma1.nlp.kb.VerbAtlasFrameID;
import it.uniroma1.nlp.kb.VerbAtlasSynsetFrameFactory;
import it.uniroma1.nlp.kb.WordNetSynsetID;

/**
 * 
 * @author Marco Salvi Classe principale del progetto VerbAtlas
 *
 */
public class VerbAtlas implements Iterable<VerbAtlasFrame>
{
	private static VerbAtlas instance;
	private VerbAtlasVersion version;
	private HashSet<VerbAtlasFrame> frames;

	// Costruttore privato necessario per il pattern del singoletto
	private VerbAtlas() throws IOException, URISyntaxException
	{
		frames = new HashSet<VerbAtlasFrame>();

		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/VA_frame_ids.tsv"))
			frames.add(
					new VerbAtlasFrame(line.substring(9), new VerbAtlasFrameID(line.substring(0, line.indexOf("\t")))));
	}

	public static VerbAtlas getInstance() throws IOException, URISyntaxException
	{
		if (instance == null)
			instance = new VerbAtlas();
		return instance;
	}

	public VerbAtlasFrame getFrame(String name)
	{
		for (VerbAtlasFrame frame : frames)
			if (frame.getName().equals(name))
				return frame;
		// TODO throw exception "frame <name> non trovato"
		System.out.println("FRAME "+name+" DOESN'T EXIST ERROR");
		return null;
	}

	public Frame getFrame(ResourceID id) throws IOException, URISyntaxException
	{
		if (id instanceof VerbAtlasFrameID)
			return toVerbAtlasFrame((VerbAtlasFrameID) id);

		if (id instanceof BabelNetSynsetID)
			return toVerbAtlasFrame(((BabelNetSynsetID) id).toVerbAtlasID()).toSynsetFrame((BabelNetSynsetID) id);

		if (id instanceof WordNetSynsetID)
			return getFrame(((WordNetSynsetID) id).toBabelID());

		// TODO PropBank

		System.out.println("ID TO FRAME ERROR");
		return null; // throw exception 
	}

	private VerbAtlasFrame toVerbAtlasFrame(VerbAtlasFrameID id)
	{
		for (VerbAtlasFrame frame : frames)
			if (id.equals(frame.getId()))
				return frame;
		// throw exception
		System.out.println("TO VERBATLASFRAME ERROR");
		return null;
	}

	public HashSet<VerbAtlasFrame> getFramesByVerb(String verb) throws IOException, URISyntaxException
	{
		// TODO cercare tutti i frame che contengono un verbo fornito in input,
		// restituisce l'insieme dei frame che si riferiscono ai vari significati di
		// "run"
		// e contengono i synset corrispondenti. I sinonimi di ogni synset verbale sono
		// nel file wn2lemma.tsv
		HashSet<VerbAtlasFrame> frames = new HashSet<VerbAtlasFrame>();
		for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/wn2lemma.tsv"))
			if (line.contains(verb.toLowerCase()))
				frames.add(toVerbAtlasFrame(
						new WordNetSynsetID(line.substring(0, line.indexOf("\t"))).toBabelID().toVerbAtlasID()));
		return frames;
	}

	public VerbAtlasVersion getVersion()
	{
		return version;
	}

	@Override
	public Iterator<VerbAtlasFrame> iterator()
	{
		return frames.iterator();
	}

	public static class VerbAtlasFrame implements Frame, Iterable<BabelNetSynsetID>
	{
		// TODO implementare equals e hashcode
		private String name;
		private HashSet<BabelNetSynsetID> babelSynsetIds;
		private VerbAtlasFrameID frameId;
		private TreeSet<Role> roles;
		private VerbAtlasSynsetFrameFactory synsetFactory = new VerbAtlasSynsetFrameFactory();

		// Costruttore privato necessario
		private VerbAtlasFrame(String name, VerbAtlasFrameID frameId) throws IOException, URISyntaxException
		{
			this(name, frameId, readSynsetIds(frameId), readRoles(frameId));
		}

		// Costruttore usato dal builder per specificare gli id e i ruoli
		private VerbAtlasFrame(String name, VerbAtlasFrameID frameId, HashSet<BabelNetSynsetID> babelSynsetIds,
				TreeSet<Role> roles)
		{
			this.name = name;
			this.frameId = frameId;
			this.roles = roles;
			this.babelSynsetIds = babelSynsetIds;
		}

		private static HashSet<BabelNetSynsetID> readSynsetIds(VerbAtlasFrameID frameId)
				throws IOException, URISyntaxException
		{
			HashSet<BabelNetSynsetID> synsetIds = new HashSet<BabelNetSynsetID>();
			for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/VA_bn2va.tsv"))
				if (line.substring(line.indexOf("\t") + 1).equals(frameId.getId()))
					synsetIds.add(new BabelNetSynsetID(line.substring(0, line.indexOf("\t")).strip()));
			return synsetIds;
		}

		private static TreeSet<Role> readRoles(VerbAtlasFrameID frameId) throws IOException, URISyntaxException
		{
			TreeSet<Role> roles = new TreeSet<Role>();
			for (String line : TextLoader.loadTxt("Verbatlas-1.0.3/VA_va2pas.tsv"))
				if (line.substring(0, line.indexOf("\t")).equals(frameId.getId()))
				{
					for (String role : line.substring(line.indexOf("\t") + 1).split("\t"))
						if (!role.isEmpty())
							roles.add(new Role(role, frameId));
					break;
				}
			return roles;
		}

		public VerbAtlasSynsetFrame toSynsetFrame(BabelNetSynsetID id) throws IOException, URISyntaxException
		{
			return synsetFactory.buildSynsetFrame(id, this, roles);
		}

		public VerbAtlasSynsetFrame toSynsetFrame(WordNetSynsetID id) throws IOException, URISyntaxException
		{
			return synsetFactory.buildSynsetFrame(id, this, roles);
		}

		@Override
		public String getName()
		{
			return name;
		}

		@Override
		public ResourceID getId()
		{
			return frameId;
		}

		@Override
		public TreeSet<Role> getRoles()
		{
			return roles;
		}

		@Override
		public Iterator<BabelNetSynsetID> iterator()
		{
			return babelSynsetIds.iterator();
		}

		@Override
		public String toString()
		{
			List<String> roles = new ArrayList<String>();
			List<String> ids = new ArrayList<String>();
			for (Role role : this.roles)
				roles.add(role.toString());
			for (BabelNetSynsetID babelId : babelSynsetIds)
				ids.add(babelId.getId());

			return "Frame Name: " + name + "	Frame ID: " + frameId.getId() + "\n" + "Roles: \n\t"
					+ String.join(",\n\t", roles) + "\n" + "BabelNet Synset IDs: \n\t" + String.join(", ", ids) + "\n";
		}

		public static class Builder
		{
			private HashSet<BabelNetSynsetID> babelIds = new HashSet<BabelNetSynsetID>();
			private TreeSet<Role> roles = new TreeSet<Role>();
			private String name;
			private VerbAtlasFrameID frameId;

			public Builder(String name, VerbAtlasFrameID frameId)
			{
				this.name = name;
				this.frameId = frameId;
			}

			public void addBabelId(BabelNetSynsetID babelId)
			{
				babelIds.add(babelId);
			}

			public void addRole(Role role)
			{
				roles.add(role);
			}

			public VerbAtlasFrame build()
			{
				return new VerbAtlasFrame(name, frameId, babelIds, roles);
			}
		}

		public static class Role implements Comparable<Role>
		{
			// TODO implementare equals e hashcode
			private VerbAtlasFrameID frameId;
			private Type type;
			private TreeSet<SelectionalPreference> sp = new TreeSet<SelectionalPreference>();

			public Role(String type, VerbAtlasFrameID frameId)
			{
				this.type = Type.valueOf(type.replace('-', '_').toUpperCase());
				this.frameId = frameId;
			}

			public void addSelectionalPreference(SelectionalPreference sp)
			{
				this.sp.add(sp);
			}

			public HashSet<BabelNetSynsetID> getImplicitArguments()
			{
				// TODO
				return null;
			}

			public HashSet<BabelNetSynsetID> getShadowArguments()
			{
				// TODO
				return null;
			}

			public String getType()
			{
				String role = type.toString();
				role = role.toLowerCase();
				role = role.substring(0, 1).toUpperCase() + role.substring(1); // First Letter Only Uppercase
				return role;
			}

			@Override
			public int compareTo(Role o)
			{
				return type.toString().compareTo(o.toString());
			}

			@Override
			public String toString()
			{
				List<String> strings = new ArrayList<String>();
				for (SelectionalPreference sel : sp)
					strings.add(sel.toString());
				return "Type: " + type.toString() + " --> [" + String.join(", ", strings) + "]";
			}

			enum Type
			{
				AGENT, ASSET, ATTRIBUTE, BENEFICIARY, CAUSE, CO_AGENT, CO_PATIENT, CO_THEME, DESTINATION, EXPERIENCER,
				EXTENT, GOAL, IDIOM, INSTRUMENT, LOCATION, MATERIAL, PATIENT, PRODUCT, PURPOSE, RECIPIENT, RESULT,
				SOURCE, STIMULUS, THEME, TIME, TOPIC, VALUE;
			}
		}
	}
}

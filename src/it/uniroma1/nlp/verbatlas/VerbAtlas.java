package it.uniroma1.nlp.verbatlas;

import java.io.IOException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;
import it.uniroma1.nlp.kb.BabelNetSynsetID;
import it.uniroma1.nlp.kb.Frame;
import it.uniroma1.nlp.kb.ImplicitArgument;
import it.uniroma1.nlp.kb.PropBankPredicateID;
import it.uniroma1.nlp.kb.ResourceID;
import it.uniroma1.nlp.kb.RolePreference;
import it.uniroma1.nlp.kb.SelectionalPreference;
import it.uniroma1.nlp.kb.ShadowArgument;
import it.uniroma1.nlp.kb.TextLoader;
import it.uniroma1.nlp.kb.VerbAtlasFrameID;
import it.uniroma1.nlp.kb.VerbAtlasSynsetFrameFactory;
import it.uniroma1.nlp.kb.VerbAtlasVersion;
import it.uniroma1.nlp.kb.WordNetSynsetID;
import it.uniroma1.nlp.kb.exceptions.FrameDoesNotExist;
import it.uniroma1.nlp.kb.exceptions.IdToFrameException;
import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;
import it.uniroma1.nlp.kb.exceptions.RoleNotFocalizedOnSynsetException;
import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

/**
 * 
 * @author Marco Salvi Classe principale del progetto VerbAtlas
 *
 */
public class VerbAtlas implements Iterable<VerbAtlasFrame>
{
	private static VerbAtlas instance;
	private VerbAtlasVersion version = new VerbAtlasVersion();
	private HashSet<VerbAtlasFrame> frames;

	// Costruttore privato necessario per il pattern del singoletto
	private VerbAtlas() throws IOException, URISyntaxException, MissingVerbAtlasResourceException
	{
		frames = new HashSet<VerbAtlasFrame>();

		for (String line : TextLoader.loadTxt("VA_frame_ids.tsv"))
			frames.add(new VerbAtlasFrame(line.substring(line.indexOf("\t") + 1),
					new VerbAtlasFrameID(line.substring(0, line.indexOf("\t")))));
	}

	public static VerbAtlas getInstance() throws IOException, URISyntaxException, MissingVerbAtlasResourceException
	{
		if (instance == null)
			instance = new VerbAtlas();
		return instance;
	}

	public VerbAtlasFrame getFrame(String name) throws FrameDoesNotExist
	{
		for (VerbAtlasFrame frame : frames)
			if (frame.getName().equals(name))
				return frame;
		throw new FrameDoesNotExist("Frame " + name + " does not exist");
	}

	public Frame getFrame(ResourceID id) throws IOException, URISyntaxException, VerbAtlasException
	{
		if (id instanceof VerbAtlasFrameID)
			return toVerbAtlasFrame((VerbAtlasFrameID) id);

		if (id instanceof BabelNetSynsetID)
			return toVerbAtlasFrame(((BabelNetSynsetID) id).toVerbAtlasID()).toSynsetFrame((BabelNetSynsetID) id);

		if (id instanceof WordNetSynsetID)
			return getFrame(((WordNetSynsetID) id).toBabelID());

		if (id instanceof PropBankPredicateID)
			return getFrame(((PropBankPredicateID) id).toVerbAtlasID());

		throw new IdToFrameException("ID '" + id.getId() + "' does not exist");
	}

	private VerbAtlasFrame toVerbAtlasFrame(VerbAtlasFrameID id) throws IdToFrameException
	{
		for (VerbAtlasFrame frame : frames)
			if (id.equals(frame.getId()))
				return frame;
		throw new IdToFrameException("ID '" + id.getId() + "' does not exist");
	}

	public HashSet<VerbAtlasFrame> getFramesByVerb(String verb)
			throws VerbAtlasException, IOException, URISyntaxException, MissingVerbAtlasResourceException
	{
		// cercare tutti i frame che contengono un verbo fornito in input,
		// restituisce l'insieme dei frame che si riferiscono ai vari significati di
		// "run"
		// e contengono i synset corrispondenti. I sinonimi di ogni synset verbale sono
		// nel file wn2lemma.tsv
		HashSet<VerbAtlasFrame> frames = new HashSet<VerbAtlasFrame>();
		for (String line : TextLoader.loadTxt("wn2lemma.tsv"))
			if (line.endsWith(verb.toLowerCase()))
				frames.add(toVerbAtlasFrame(
						new WordNetSynsetID(line.substring(0, line.indexOf("\t"))).toBabelID().toVerbAtlasID()));
		return frames;
	}

	// TODO getSynsetByVerb, stessa cosa di get Frames By Verb ma restituisce i
	// synset che contengono il verbo, non i frame

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
		private String name;

		private HashMap<BabelNetSynsetID, VerbAtlasSynsetFrame> synsets = new HashMap<BabelNetSynsetID, VerbAtlasSynsetFrame>();
		private Set<BabelNetSynsetID> babelSynsetIds = synsets.keySet();
		private VerbAtlasFrameID frameId;
		private TreeSet<Role> roles;
		private VerbAtlasSynsetFrameFactory synsetFactory = new VerbAtlasSynsetFrameFactory();

		// Costruttore privato necessario
		private VerbAtlasFrame(String name, VerbAtlasFrameID frameId)
				throws IOException, URISyntaxException, MissingVerbAtlasResourceException
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

			// TODO usare stream?
			for (BabelNetSynsetID synsetId : babelSynsetIds)
				synsets.put(synsetId, null);
		}

		private static HashSet<BabelNetSynsetID> readSynsetIds(VerbAtlasFrameID frameId)
				throws IOException, URISyntaxException, MissingVerbAtlasResourceException
		{
			HashSet<BabelNetSynsetID> synsetIds = new HashSet<BabelNetSynsetID>();
			for (String line : TextLoader.loadTxt("VA_bn2va.tsv"))
				if (line.endsWith(frameId.getId()))
					synsetIds.add(new BabelNetSynsetID(line.substring(0, line.indexOf("\t")).strip()));
			return synsetIds;
		}

		private static TreeSet<Role> readRoles(VerbAtlasFrameID frameId)
				throws IOException, URISyntaxException, MissingVerbAtlasResourceException
		{
			TreeSet<Role> roles = new TreeSet<Role>();
			for (String line : TextLoader.loadTxt("VA_va2pas.tsv"))
				if (line.startsWith(frameId.getId()))
				{
					for (String role : line.substring(line.indexOf("\t") + 1).split("\t"))
						if (!role.isEmpty())
							roles.add(new Role(role));
					break;
				}
			return roles;
		}

		public VerbAtlasSynsetFrame toSynsetFrame(BabelNetSynsetID id)
				throws IOException, URISyntaxException, VerbAtlasException
		{
			if (synsets.get(id) == null)
			{
				synsets.put(id, synsetFactory.buildSynsetFrame(id, this, roles));
				return synsets.get(id);
			}
			return synsets.get(id);
		}

		public VerbAtlasSynsetFrame toSynsetFrame(WordNetSynsetID id)
				throws IOException, URISyntaxException, VerbAtlasException
		{
			return toSynsetFrame(id.toBabelID());
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
		public boolean equals(Object o)
		{
			if (this == o)
				return true;
			if (o == null || o.getClass() != this.getClass())
				return false;

			VerbAtlasFrame frame = (VerbAtlasFrame) o;
			return frame.getId().equals(this.frameId);
		}

		@Override
		public int hashCode()
		{
			return frameId.hashCode();
		}

		@Override
		public String toString()
		{
			return "\t\t\tFrame Name: " + name + "	Frame ID: " + frameId.getId() + "\n" + "Roles: \n\t"
					+ String.join(",\n\t",
							roles.stream().map(x -> x.getType().toUpperCase()).collect(Collectors.toList()))
					+ "\nBabelNet Synset IDs: \n\t"
					+ String.join(", ", babelSynsetIds.stream().map(x -> x.toString()).collect(Collectors.toList()))
					+ "\n";
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
			private Type type;
			private TreeSet<SelectionalPreference> sp = new TreeSet<SelectionalPreference>();
			private HashSet<ImplicitArgument> implicitArguments = new HashSet<ImplicitArgument>();
			private HashSet<ShadowArgument> shadowArguments = new HashSet<ShadowArgument>();

			public Role(String type)
			{
				this.type = Type.valueOf(type.replace('-', '_').toUpperCase());
			}

			public void addSelectionalPreference(SelectionalPreference sp)
			{
				this.sp.add(sp);
			}

			public void addImplicitArgument(ImplicitArgument impArg) throws RoleNotFocalizedOnSynsetException
			{
				this.implicitArguments.add(impArg);
			}

			public void addShadowArgument(ShadowArgument shadArg) throws RoleNotFocalizedOnSynsetException
			{
				this.shadowArguments.add(shadArg);
			}

			public HashSet<BabelNetSynsetID> getImplicitArguments()
			{
				return implicitArguments.stream().map(x -> x.getBabelSynsetId())
						.collect(Collectors.toCollection(HashSet::new));
			}

			public HashSet<BabelNetSynsetID> getShadowArguments()
			{
				return shadowArguments.stream().map(x -> x.getBabelSynsetId())
						.collect(Collectors.toCollection(HashSet::new));
			}

			public String getType()
			{
				String role = type.toString();
				role = role.toLowerCase();
				role = role.substring(0, 1).toUpperCase() + role.substring(1); // First Letter Only Uppercase
				return role;
			}

			public TreeSet<SelectionalPreference> getSelectionalPreferences()
			{
				return sp;
			}

			@Override
			public int compareTo(Role o)
			{
				return getType().compareTo(o.getType());
			}

			@Override
			public boolean equals(Object o)
			{
				if (o == this)
					return true;
				if (o == null || o.getClass() != this.getClass())
					return false;

				Role role = (Role) o;
				return role.getSelectionalPreferences().equals(this.sp) && role.getType().equals(getType())
						&& role.getImplicitArguments().equals(implicitArguments)
						&& role.getShadowArguments().equals(shadowArguments);
			}

			@Override
			public int hashCode()
			{
				return Objects.hash(sp, type);
			}

			@Override
			public String toString()
			{
				if (sp.isEmpty())
					return "";
				String s = type.toString() + " --> ["
						+ String.join(", ", sp.stream().map(x -> x.toString()).collect(Collectors.toList()));
				String s1 = String.join(", ",
						implicitArguments.stream().map(x -> x.toString()).collect(Collectors.toList()));
				String s2 = String.join(", ",
						shadowArguments.stream().map(x -> x.toString()).collect(Collectors.toList()));
				if (!s1.isEmpty())
					s += ", " + s1;
				if (!s2.isEmpty())
					s += ", " + s2;
				return s + " ]";
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

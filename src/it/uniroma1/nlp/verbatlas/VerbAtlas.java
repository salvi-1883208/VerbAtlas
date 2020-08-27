package it.uniroma1.nlp.verbatlas;

import java.io.IOException;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;
import it.uniroma1.nlp.kb.BabelNetSynsetID;
import it.uniroma1.nlp.kb.Frame;
import it.uniroma1.nlp.kb.ImplicitArgument;
import it.uniroma1.nlp.kb.PropBankPredicateID;
import it.uniroma1.nlp.kb.ResourceID;
import it.uniroma1.nlp.kb.SelectionalPreference;
import it.uniroma1.nlp.kb.ShadowArgument;
import it.uniroma1.nlp.kb.TextLoader;
import it.uniroma1.nlp.kb.VerbAtlasFrameID;
import it.uniroma1.nlp.kb.VerbAtlasSynsetFrameFactory;
import it.uniroma1.nlp.kb.VerbAtlasVersion;
import it.uniroma1.nlp.kb.WordNetSynsetID;
import it.uniroma1.nlp.kb.exceptions.FrameDoesNotExistException;
import it.uniroma1.nlp.kb.exceptions.IdToFrameException;
import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;
import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;

/**
 * Classe che contiene tutti i frame riportati nella risorsa VerbAtlas. Non può
 * essere istanziata singolarmente, per ottenere un'istanza usare il metodo
 * VerbAtlas.getInstance(). Non può essere presente più di una istanza della
 * classe.
 * 
 * @author Salvi Marco
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

	/**
	 * Metodo per ottenere un'istanza di VerbAtlas.
	 * 
	 * @return Istanza di VerbAtlas
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws MissingVerbAtlasResourceException
	 */
	public static VerbAtlas getInstance() throws IOException, URISyntaxException, MissingVerbAtlasResourceException
	{
		if (instance == null)
			instance = new VerbAtlas();
		return instance;
	}

	/**
	 * Metodo per ottenere un VerbAtlasFrame il quale nome corrisponde alla stringa
	 * fornita in input. Lancia FrameDoesNotExistException se non esiste nessun
	 * frame chiamato come la stringa fornita in input.
	 * 
	 * @param name del frame da restituire
	 * @return VerbAtlasFrame
	 * @throws FrameDoesNotExistException
	 */
	public VerbAtlasFrame getFrame(String name) throws FrameDoesNotExistException
	{
		for (VerbAtlasFrame frame : frames)
			if (frame.getName().equals(name))
				return frame;
		throw new FrameDoesNotExistException("Frame " + name + " does not exist");
	}

	/**
	 * Metodo per ottenere un VerbAtlasFrame o VerbAtlasSynsetFrame fornendo in
	 * input l'ID del Frame o Synset che si vuole ottenere.
	 * 
	 * @param id del frame da restituire
	 * @return VerbAtlasFrame oppure VerbAtlasSynsetFrame
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws VerbAtlasException
	 */
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

	/**
	 * Metodo per ottenere l'insieme dei frame che si riferiscono ai vari
	 * significati del verbo fornito in input come stringa.
	 * 
	 * @param verbo da cercare nei frame
	 * @return HashSet<VerbAtlasFrame> dei frame che si riferiscono ai vari
	 *         significati di verb
	 * @throws VerbAtlasException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @throws MissingVerbAtlasResourceException
	 */
	public HashSet<VerbAtlasFrame> getFramesByVerb(String verb)
			throws VerbAtlasException, IOException, URISyntaxException, MissingVerbAtlasResourceException
	{
		HashSet<VerbAtlasFrame> frames = new HashSet<VerbAtlasFrame>();
		for (String line : TextLoader.loadTxt("wn2lemma.tsv"))
			if (line.endsWith(verb.toLowerCase()))
				frames.add(toVerbAtlasFrame(
						new WordNetSynsetID(line.substring(0, line.indexOf("\t"))).toBabelID().toVerbAtlasID()));
		return frames;
	}

	/**
	 * Restituisce un'istanza di VerbAtlasVersion che rappresenta la versione della
	 * risorsa VerbAtlas.
	 * 
	 * @return un'istanza di VerbAtlasVersion
	 */
	public VerbAtlasVersion getVersion()
	{
		return version;
	}

	@Override
	public Iterator<VerbAtlasFrame> iterator()
	{
		return frames.iterator();
	}

	/**
	 * Classe che rappresenta un Frame contenuto nella risorsa VerbAtlas. Un Frame
	 * contiene: un nome, un insieme di tutti i BabelSynsetID associati a quel
	 * frame, un VerbAtlasFrameID e una sequenza ordinata dei ruoli del frame.
	 * 
	 * @author Salvi Marco
	 */
	public static class VerbAtlasFrame implements Frame, Iterable<BabelNetSynsetID>
	{
		private String name;

		private HashMap<BabelNetSynsetID, VerbAtlasSynsetFrame> synsets = new HashMap<BabelNetSynsetID, VerbAtlasSynsetFrame>();
		private Set<BabelNetSynsetID> babelSynsetIds = synsets.keySet();
		private VerbAtlasFrameID frameId;
		private TreeSet<Role> roles;
		private VerbAtlasSynsetFrameFactory synsetFactory = new VerbAtlasSynsetFrameFactory();

		private VerbAtlasFrame(String name, VerbAtlasFrameID frameId)
				throws IOException, URISyntaxException, MissingVerbAtlasResourceException
		{
			this(name, frameId, readSynsetIds(frameId), readRoles(frameId));
		}

		private VerbAtlasFrame(String name, VerbAtlasFrameID frameId, HashSet<BabelNetSynsetID> babelSynsetIds,
				TreeSet<Role> roles)
		{
			this.name = name;
			this.frameId = frameId;
			this.roles = roles;

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

		/**
		 * "Focalizza" un VerbAtlasFrame in un VerbAtlasSynsetFrame fornendo in input il
		 * BabelNetSynsetID del Synset.
		 * 
		 * @param BabelNetSynsetID id del VerbAtlasSynsetFrame
		 * @return VerbAtlasSynsetFrame
		 * @throws IOException
		 * @throws URISyntaxException
		 * @throws VerbAtlasException
		 */
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

		/**
		 * "Focalizza" un VerbAtlasFrame in un VerbAtlasSynsetFrame fornendo in input il
		 * BabelNetSynsetID del Synset.
		 * 
		 * @param WordNetSynsetID id del VerbAtlasSynsetFrame
		 * @return VerbAtlasSynsetFrame
		 * @throws IOException
		 * @throws URISyntaxException
		 * @throws VerbAtlasException
		 */
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

		/**
		 * Builder di VerbAtlasFrame. Permette di costruire un Frame specificando i
		 * ruoli e i synset a esso associati.
		 * 
		 * @author Salvi Marco
		 */
		public static class Builder
		{
			private HashSet<BabelNetSynsetID> babelIds = new HashSet<BabelNetSynsetID>();
			private TreeSet<Role> roles = new TreeSet<Role>();
			private String name;
			private VerbAtlasFrameID frameId;

			/**
			 * Costruttore del builder
			 * 
			 * @param name    del Frame da costruire
			 * @param frameId del Frame da costruire
			 */
			public Builder(String name, VerbAtlasFrameID frameId)
			{
				this.name = name;
				this.frameId = frameId;
			}

			/**
			 * Aggiunge un BabelNetSynsetID all'insieme dei Synset del Frame che si sta
			 * costruendo.
			 * 
			 * @param babelId da aggiungere al Frame
			 */
			public void addBabelId(BabelNetSynsetID babelId)
			{
				babelIds.add(babelId);
			}

			/**
			 * Aggiunge un ruolo Role all'insieme dei Synset del Frame che si sta
			 * costruendo.
			 * 
			 * @param role da aggiungere al Frame
			 */
			public void addRole(Role role)
			{
				roles.add(role);
			}

			/**
			 * Metodo finale per costruire il VerbAtlasFrame.
			 * 
			 * @return VerbAtlasFrame con ruoli e synset specificati con il builder
			 */
			public VerbAtlasFrame build()
			{
				return new VerbAtlasFrame(name, frameId, babelIds, roles);
			}
		}

		/**
		 * Classe che rappresenta un singolo ruolo di un certo Frame o Synset. Contiene:
		 * il nome del ruolo, un insieme delle preferenze di selezione di tale ruolo in
		 * tale Synset, un insieme degli argomenti impliciti e un'insieme degli
		 * argomenti ombra.
		 * 
		 * @author Salvi Marco
		 */
		public static class Role implements Comparable<Role>
		{
			private Type type;
			private TreeSet<SelectionalPreference> sp = new TreeSet<SelectionalPreference>();
			private HashSet<ImplicitArgument> implicitArguments = new HashSet<ImplicitArgument>();
			private HashSet<ShadowArgument> shadowArguments = new HashSet<ShadowArgument>();

			/**
			 * Costruttore della classe.
			 * 
			 * @param nome del ruolo che viene convertito in un'istanza dell'enum Type
			 */
			public Role(String type)
			{
				this.type = Type.valueOf(type.replace('-', '_').toUpperCase());
			}

			/**
			 * Aggiunge una preferenza di selezione all'insieme di SelectionalPreference.
			 * 
			 * @param SelectionalPreference da aggiungere
			 */
			public void addSelectionalPreference(SelectionalPreference sp)
			{
				this.sp.add(sp);
			}

			/**
			 * Aggiunge un argomento implicito all'insieme di ImplicitArgument.
			 * 
			 * @param ImplicitArgument da aggiungere
			 */
			public void addImplicitArgument(ImplicitArgument impArg)
			{
				this.implicitArguments.add(impArg);
			}

			/**
			 * Aggiunge un argomento ombra all'insieme di ShadowArgument.
			 * 
			 * @param ShadowArgument da aggiungere.
			 */
			public void addShadowArgument(ShadowArgument shadArg)
			{
				this.shadowArguments.add(shadArg);
			}

			/**
			 * Restituisce l'insieme di tutti gli argomenti impliciti associati al ruolo.
			 * 
			 * @return HashSet<BabelNetSynsetID> ovvero gli argomenti impliciti
			 */
			public HashSet<BabelNetSynsetID> getImplicitArguments()
			{
				return implicitArguments.stream().map(x -> x.getBabelSynsetId())
						.collect(Collectors.toCollection(HashSet::new));
			}

			/**
			 * Restituisce l'insieme di tutti gli argomenti ombra associati al ruolo.
			 * 
			 * @return HashSet<BabelNetSynsetID> ovvero gli argomenti ombra
			 */
			public HashSet<BabelNetSynsetID> getShadowArguments()
			{
				return shadowArguments.stream().map(x -> x.getBabelSynsetId())
						.collect(Collectors.toCollection(HashSet::new));
			}

			/**
			 * Restituisce il nome del Ruolo
			 * 
			 * @return String nome del ruolo
			 */
			public String getType()
			{
				String role = type.toString();
				role = role.toLowerCase();
				role = role.substring(0, 1).toUpperCase() + role.substring(1); // First Letter Only Uppercase
				return role;
			}

			/**
			 * Restituisce l'insieme delle preferenze di selezione associate al ruolo.
			 * 
			 * @return TreeSet<SelectionalPreference> ovvero le preferenze di selezione
			 */
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

			/**
			 * Enumerazione di tutti i nomi possibili per un qualunque ruolo.
			 * 
			 * @author Salvi
			 */
			enum Type
			{
				AGENT, ASSET, ATTRIBUTE, BENEFICIARY, CAUSE, CO_AGENT, CO_PATIENT, CO_THEME, DESTINATION, EXPERIENCER,
				EXTENT, GOAL, IDIOM, INSTRUMENT, LOCATION, MATERIAL, PATIENT, PRODUCT, PURPOSE, RECIPIENT, RESULT,
				SOURCE, STIMULUS, THEME, TIME, TOPIC, VALUE;
			}
		}
	}
}

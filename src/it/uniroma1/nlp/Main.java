package it.uniroma1.nlp;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import it.uniroma1.nlp.kb.BabelNetSynsetID;
import it.uniroma1.nlp.kb.PropBankPredicateID;
import it.uniroma1.nlp.kb.TextLoader;
import it.uniroma1.nlp.kb.VerbAtlasFrameID;
import it.uniroma1.nlp.kb.WordNetSynsetID;
import it.uniroma1.nlp.kb.exceptions.FrameDoesNotExist;
import it.uniroma1.nlp.kb.exceptions.MissingVerbAtlasResourceException;
import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;
import it.uniroma1.nlp.verbatlas.VerbAtlas;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame.Role;
import it.uniroma1.nlp.verbatlas.VerbAtlasSynsetFrame;

public class Main
{
	public static void main(String[] args)
			throws IOException, URISyntaxException, VerbAtlasException, MissingVerbAtlasResourceException
	{

		VerbAtlas va = VerbAtlas.getInstance();

//		PrintStream fileStream = new PrintStream("BabelNetSynsetFrames.txt");
//		System.setOut(fileStream);
//		for (VerbAtlasFrame vaf : va)
//			for (BabelNetSynsetID babid : vaf)
//			{
//				if (babid.getId().equals("bn:00015081n") || babid.getId().equals("bn:03572679n"))
//					continue;
//				System.out.println(vaf.toSynsetFrame(babid) + "\n\n"
//						+ "-------------------------------------------------------------" + "\n");
//			}
//		System.out.println("FINITO");

		System.out.println(va.getFrame(new WordNetSynsetID("wn:01168468v")));
		System.out.println(va.getFrame(new BabelNetSynsetID("bn:00083035v")));
		System.out.println(va.getFrame(new VerbAtlasFrameID("va:0463f")));
		System.out.println(va.getFrame(new PropBankPredicateID("abet.01")));
		System.out.println(va.getFrame("EAT_BITE"));
		for(VerbAtlasFrame frame : va.getFramesByVerb("hit"))
			System.out.println(frame);

//		for (VerbAtlasFrame frame : va.getFramesByVerb("eat"))
//			System.out.println(frame.getName());

//		VerbAtlasFrame vaf = va.getFrame("EAT_BITE");
//		for (BabelNetSynsetID babid : vaf)
//			System.out.println(vaf.toSynsetFrame(babid));

//		HashSet<BabelNetSynsetID> hash = new HashSet<BabelNetSynsetID>();
//		hash.add(new BabelNetSynsetID("bn:00015081n"));
//		hash.add(new BabelNetSynsetID("bn:00015080n"));
//		System.out.println(hash);

//		HashMap<BabelNetSynsetID, VerbAtlasSynsetFrame> synsets = new HashMap<BabelNetSynsetID, VerbAtlasSynsetFrame>();
//		Set<BabelNetSynsetID> babelSynsetIds = synsets.keySet();
//		
//		synsets.put(new BabelNetSynsetID("bn:00015080n"), null);
//		babelSynsetIds.add(new BabelNetSynsetID("bn:10015080n"));
//		
//		System.out.println(synsets);

//		final double start = System.nanoTime();
//
//		VerbAtlasFrame frame = va.getFrame("EAT_BITE");
//
//		for (int i = 0; i < 100; i++)
//			for (BabelNetSynsetID synsetId : frame)
//				System.out.println(frame.toSynsetFrame(synsetId));
//
//		final double end = System.nanoTime();
//
//		System.out.println("Tempo trascorso: " + (end - start));

//		System.out.println(va.getVersion());

//		for (Role role : va.getFrame(new BabelNetSynsetID("bn:00082388v")).getRoles())
//		{
//			System.out.println("Role Name: " + role.getType() + "\n\tImplicit Arguments: " + role.getImplicitArguments()
//					+ "\n\tShadow Arguments: " + role.getShadowArguments() + "\n");
//		}
	}
}

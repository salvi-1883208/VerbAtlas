package it.uniroma1.nlp;

import java.io.File;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import it.uniroma1.nlp.kb.BabelNetSynsetID;
import it.uniroma1.nlp.kb.PropBankPredicateID;
import it.uniroma1.nlp.kb.TextLoader;
import it.uniroma1.nlp.kb.VerbAtlasFrameID;
import it.uniroma1.nlp.kb.WordNetSynsetID;
import it.uniroma1.nlp.kb.exceptions.FrameDoesNotExist;
import it.uniroma1.nlp.kb.exceptions.VerbAtlasException;
import it.uniroma1.nlp.verbatlas.VerbAtlas;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlasSynsetFrame;

public class Main
{
	public static void main(String[] args) throws IOException, URISyntaxException, VerbAtlasException
	{
		VerbAtlas va = VerbAtlas.getInstance();
//		PrintStream fileStream = new PrintStream("BabelNetSynsetFrames.txt");
//		System.setOut(fileStream);
//		for (VerbAtlasFrame vaf : va)
//			for (BabelNetSynsetID babid : vaf)
//				System.out.println(vaf.toSynsetFrame(babid) + "\n\n"
//						+ "-------------------------------------------------------------" + "\n");
//		System.out.println("FINITO");

//		System.out.println(va.getFrame(new WordNetSynsetID("wn:01168468v")));
//		System.out.println(va.getFrame(new BabelNetSynsetID("bn:00083231v")));
//		System.out.println(va.getFrame(new VerbAtlasFrameID("va:0428f")));
//		System.out.println(va.getFrame(new PropBankPredicateID("abet.01")));
//		System.out.println(va.getFrame("EAT_BITE"));

//		for (VerbAtlasFrame frame : va.getFramesByVerb("eat"))
//			System.out.println(frame.getName());

		VerbAtlasFrame vaf = va.getFrame("EAT_BITE");
		for (BabelNetSynsetID babid : vaf)
			System.out.println(vaf.toSynsetFrame(babid));
	}
}
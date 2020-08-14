package it.uniroma1.nlp;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import it.uniroma1.nlp.kb.BabelNetSynsetID;
import it.uniroma1.nlp.kb.TextLoader;
import it.uniroma1.nlp.kb.VerbAtlasFrameID;
import it.uniroma1.nlp.kb.WordNetSynsetID;
import it.uniroma1.nlp.verbatlas.VerbAtlas;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;
import it.uniroma1.nlp.verbatlas.VerbAtlasSynsetFrame;

public class Main
{
	public static void main(String[] args) throws IOException, URISyntaxException
	{
//		HashSet<VerbAtlasSynsetFrame> frames = new HashSet<VerbAtlasSynsetFrame>();
		VerbAtlas va = VerbAtlas.getInstance();
//		PrintStream fileStream = new PrintStream("BabelNetSynsetFrames.txt");
//		System.setOut(fileStream);
//		for (VerbAtlasFrame vaf : va)
//			for (BabelNetSynsetID babid : vaf)
//				System.out.println(vaf.toSynsetFrame(babid) + "\n\n"
//						+ "-------------------------------------------------------------" + "\n");
//		System.out.println("FINITO");
//		System.out.println(va.getFrame(new WordNetSynsetID("wn:01168468v")));
		for(VerbAtlasFrame frame: va.getFramesByVerb("eat"))
			System.out.println(frame.getName().equals("EAT_BITE"));
	}
}
package it.uniroma1.nlp;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.TreeSet;

import it.uniroma1.nlp.kb.BabelNetSynsetID;
import it.uniroma1.nlp.kb.TextLoader;
import it.uniroma1.nlp.kb.VerbAtlasFrameID;
import it.uniroma1.nlp.verbatlas.VerbAtlas;
import it.uniroma1.nlp.verbatlas.VerbAtlas.VerbAtlasFrame;

public class Main
{
	public static void main(String[] args) throws IOException, URISyntaxException
	{
//		List<String> list = TextLoader.loadTxt("Verbatlas-1.0.3/VA_va2pas.tsv");
//		TreeSet<String> result = new TreeSet<String>();
//		
//		for(String line : list)
//		{
//			for(String parola : line.split("\t"))
//			{
//				if(Character.isUpperCase(parola.charAt(0)))
//					result.add(parola.toUpperCase());
//			}
//		}
//		System.out.println(result);
		
		VerbAtlas va = VerbAtlas.getInstance();
		System.out.println(va.getFrame(new BabelNetSynsetID("bn:00084554v")));
		
//		for(BabelNetSynsetID bns : va.getFrame("HIT"))
//		{
//			System.out.println(bns);
//		}
	}
}

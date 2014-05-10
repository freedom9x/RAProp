package rank;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cmu.arktweetnlp.Twokenize;
import cmu.arktweetnlp.impl.Model;
import cmu.arktweetnlp.impl.ModelSentence;
import cmu.arktweetnlp.impl.Sentence;
import cmu.arktweetnlp.impl.features.FeatureExtractor;


/**
 * Tagger object -- wraps up the entire tagger for easy usage from Java.
 * 
 * To use:
 * 
 * (1) call loadModel().
 * 
 * (2) call tokenizeAndTag() for every tweet.
 *  
 * See main() for example code.
 * 
 * (Note RunTagger.java has a more sophisticated runner.
 * This class is intended to be easiest to use in other applications.)
 */
public class Tagger {
	public Model model;
	public FeatureExtractor featureExtractor;
	private static HashMap<String,Double> weights;
	/**
	 * Loads a model from a file.  The tagger should be ready to tag after calling this.
	 * 
	 * @param modelFilename
	 * @throws IOException
	 */
	public void loadModel(String modelFilename) throws IOException {
		model = Model.loadModelFromText(modelFilename);
		featureExtractor = new FeatureExtractor(model, false);
	}

	/**
	 * One token and its tag.
	 **/
	public static class TaggedToken {
		public String token;
		public String tag;
	}


	/**
	 * Run the tokenizer and tagger on one tweet's text.
	 **/
	public List<TaggedToken> tokenizeAndTag(String text) {
		if (model == null) throw new RuntimeException("Must loadModel() first before tagging anything");
		List<String> tokens = Twokenize.tokenizeRawTweetText(text);

		Sentence sentence = new Sentence();
		sentence.tokens = tokens;
		ModelSentence ms = new ModelSentence(sentence.T());
		featureExtractor.computeFeatures(sentence, ms);
		model.greedyDecode(ms, false);

		ArrayList<TaggedToken> taggedTokens = new ArrayList<TaggedToken>();

		for (int t=0; t < sentence.T(); t++) {
			TaggedToken tt = new TaggedToken();
			tt.token = tokens.get(t);
			tt.tag = model.labelVocab.name( ms.labels[t] );
			taggedTokens.add(tt);
		}

		return taggedTokens;
	}

	/**
	 * Illustrate how to load and call the POS tagger.
	 * This main() is not intended for serious use; see RunTagger.java for that.
	 * @throws IOException 
	 **/
	public static double Tag(String term) throws IOException  {
		//
		weights = new HashMap<String,Double>();
		// proper nouns
		weights.put("^", 4.0);
		weights.put("M", 4.0);
		weights.put("Z", 4.0);
		// common nouns
		weights.put("N", 3.0);
		weights.put("S", 3.0);
		weights.put("L", 3.0);
		// pronoun
		weights.put("O", 1.0);
		// verb
		weights.put("V", 1.0);
		// adjective / adverb
		weights.put("A", 3.0);
		weights.put("R", 3.0);
		// interjection / preposition
		weights.put("!", 0.5);
		weights.put("P", 0.5);
		weights.put("T", 0.5);
		// existential
		weights.put("X", 0.2);
		weights.put("Y", 0.2);
		// twitter stuff
		weights.put("#", 6.0);
		weights.put("U", 8.0);
		weights.put("$", 2.0);
		
		
		String modelFilename ="/cmu/arktweetnlp/model.20120919";

		Tagger tagger = new Tagger();
		tagger.loadModel(modelFilename);

		//String text = "RT @DjBlack_Pearl: wat muhfuckaz wearin 4 the lingerie party?????";
	//	String text = args[0];
		List<TaggedToken> taggedTokens = tagger.tokenizeAndTag(term);
		
		
		//System.out.println(text);
		for (TaggedToken token : taggedTokens) {
			//System.out.printf("%s\t%s\n", token.tag, token.token);
			try {
				return weights.get(token.tag);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return 0.0;
				
			}
		}
		return 0.0;
		//taggedTokens.
		//return
	}
	

}

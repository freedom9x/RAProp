package rank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dataset.ReadFile;


public class TFIDF {
	
	public static double TF_term(String term, String text)
	{
		 double max_TF = 0 ;
		 List<String> list = Arrays.asList(text.split(" "));
		 Set<String> uniqueWords = new HashSet<String>(list);
		 
		 for(String word : uniqueWords)
		 {
			 double term1 = Collections.frequency(list, word);
			 if(max_TF<term1) max_TF = term1;
		 }
		 return Collections.frequency(list, term)/max_TF;
	}
	
    public static double IDF_term(String term, Tweet[] tweets,
    		int N, String query, boolean isHTTP)
    {
    	int n = 0;
    	if (!isHTTP) 
    	{   		
			for (Tweet t : tweets)
				if (Term_in_text(term, ReadFile.RemoveAllSW(t.content, query)))
					n = n + 1;
		}
    	else 
    	{
    		for (Tweet t : tweets)
				if (Term_in_text(term, t.content))
					n = n + 1;
		}
		double idf = Math.log(N/n);
    	return idf;
    }
    
    public static boolean Term_in_text (String term, String text)
    {
    	//String text_rmsw = ReadFile.RemoveAllSW(text, AG_graph.query);
    	List<String> list = Arrays.asList(text.split(" "));
    //	Set<String> uniqueWords =  new HashSet<String>(list);
    	if(Collections.frequency(list, term)==0) return false;
    	return true;
    }
    
   	public static List<String> Get_same(String text1, String text2)
    {
    	List<String> result = new ArrayList<String>(); 
    	List<String> list1 = Arrays.asList(text1.split(" "));
    	List<String> list2 = Arrays.asList(text2.split(" "));
    	
    	Set<String> uni_text1 = new HashSet<String>(list1);
    	Set<String> uni_text2 = new HashSet<String>(list2);
    	for (String string : uni_text1) {
			if(uni_text2.contains(string) && string!=" " && string!="")
				result.add(string);
			
		}
    	
    	return result;
    }
   	public static double Agreement(String text_1, String text_2, Tweet[] tweets, int N, String query) throws IOException
   	{
//   		tweets[i].Remove1();
//		tweets[i].rmv_stopword();
//		tweets[i].content = tweets[i].content.replace(query, "");//bo tu query
//		tweets[i].content = tweets[i].content.replaceAll("( )+", " ");
		if(text_1.isEmpty()|text_2.isEmpty()) return 0.0;
		String text1 = ReadFile.RemoveAllSW(text_1, query);
		String text2 = ReadFile.RemoveAllSW(text_2, query);
   		double ag_score=0.0;
   		//same word
   		List<String> same_term = Get_same(text1, text2);
   		if (!same_term.isEmpty()) 
   		{
//			for (String string : same_term) 
//			{
//				System.out.println(string);
//			}
			for (String term : same_term) 
			{
				if (term.isEmpty())continue;
				double tf1 = TF_term(term, text1);
				double tf2 = TF_term(term, text2);
				double idf = IDF_term(term, tweets, N, query, false);
				double tag = Tagger.Tag(term);
				ag_score = ag_score + (tf1 * tf2 * Math.pow(idf, 2) * tag);
				//System.out.println(term + ": tf1=" + tf1 + "\ttf2=" + tf2
				//		+ "\tidf=" + idf + "\ttag=" + tag);
			}
		}
		//same http://
   		List<String> same_http = Get_same(text_1, text_2);
   		if(!same_http.isEmpty())
   		{
//   			for (String string : same_http) 
//			{
//				if(string.contains("http://"))
//					System.out.println(string);
//			}
   			for (String term : same_http) 
			{
				if (term.contains("http://"))
				{
					double tf1 = TF_term(term, text_1);
					double tf2 = TF_term(term, text_2);
					double idf = IDF_term(term, tweets, N, query, true);
					double tag = 8.0;
					ag_score = ag_score + (tf1 * tf2 * Math.pow(idf, 2) * tag);
//					System.out.println(term + ": tf1=" + tf1 + "\ttf2=" + tf2
//							+ "\tidf=" + idf + "\ttag=" + tag);
				}
			}
   		}
   		//s
   		
   		return ag_score;
   	}
   	public static void main(String[] args)//main to test
   	{
   		String query= "BBC World Service staff cuts";
   		
   		
   	}
    //public static boolean
}

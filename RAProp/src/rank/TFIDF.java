package rank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dataset.ReadFile;
import edu.berkeley.nlp.optimize.PowellSearch;


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
		double idf = Math.log(N/(double)n);
    	return idf;
    }
    
    public static boolean Term_in_text (String term, String text)
    {
    	//String text_rmsw = ReadFile.RemoveAllSW(text, AG_graph.query);
    	List<String> list = Arrays.asList(text.split(" "));
    //	Set<String> uniqueWords =  new HashSet<String>(list);
    	if(Collections.frequency(list, term)==0) return false;
//    	if(text.contains(term)) return true;
//    	else return false;
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
//   		String query= "BBC World Service staff cuts";
//   		System.out.println(1+Math.log(3/2.0));
   		String query = "life learning";
   		Tweet[] tweets = new Tweet[3];
   		tweets[0] = new Tweet("i love my life the game of life is a game of everlasting learning", 1);
   		tweets[1] = new Tweet("the unexamined life is not worth living", 2);
   		tweets[2] = new Tweet("never stop learning", 3);
   		int N = 3;
//   		System.out.println(tfidf(query, tweets[0].content, tweets, N));
//   		System.out.println(tfidf(query, tweets[1].content, tweets, N));
//   		System.out.println(tfidf(query, tweets[2].content, tweets, N));  		
   		System.out.println(total_distance(query, tweets[0].content));
   		System.out.println(TFIDFSimilarity(query, tweets[0].content, tweets, N));
   		System.out.println(total_distance(query, tweets[1].content));
   		System.out.println(TFIDFSimilarity(query, tweets[1].content, tweets, N));
   		System.out.println(total_distance(query, tweets[2].content));
   		System.out.println(TFIDFSimilarity(query, tweets[2].content, tweets, N));
   	}
   	public static double tfidf(String query, String doc, Tweet[] tweets, int N)
   	{
   		double sim= 0.0;
   		//List<String> parts = Get_same(query, doc);
   		String[] parts = query.split(" ");
   	//	String test = parts[0];
   		double[] v_query = new double [parts.length];   	
   		double[] v_doc = new double [parts.length];
   		double[] idf_term = new double[parts.length];//idf cua tung tu
   	//	int i = 0;
   		for (int i = 0; i<parts.length; i++) {//idf cua tung tu
			idf_term[i]=idf(parts[i], tweets, N);
			
		}
   		
   		for (int i = 0; i<parts.length; i++) {//tao 2 vector
			v_query[i] = tf(parts[i], query)*idf_term[i];
			v_doc[i] = tf(parts[i], doc)*idf_term[i];
			
		}
   		sim = dp_2_vector(v_doc, v_query)/(d_vector(v_doc)*d_vector(v_query));
   		return sim;
   	}
   	public static double idf(String term, Tweet[] tweets, int N)
   	{
   		
   		int n = 0;
    		for (Tweet t : tweets)
				if (Term_in_text(term, t.content))
					n = n + 1;
		double idf =1+ Math.log(N/(double)n);
    	return idf;
   	}
   	public static double tf(String term, String text)
	{
		// double max_TF = 0 ;
   		if(!text.contains(term)) return 0.0;
		 List<String> list = Arrays.asList(text.split(" "));

		 return Collections.frequency(list, term)/(double)list.size();
	}
   	public static double d_vector(double[] vector)// tinh do dai vector
   	{
   		double term = 0.0;
   		for (double d : vector){
			term = term + d*d;
		}
   		return Math.sqrt(term);
   	}
   	public static double dp_2_vector(double[] vector1, double[] vector2)// tinh tong 2 vector
   	{
   		double result = 0.0;
   		for(int i = 0 ; i< vector1.length; i++)
   		{
   			result = result + (vector1[i]* vector2[i]);
   		}
   		return result;
   	}
   	public static int total_distance(String query, String tweet)
   	{
   		query = query.toLowerCase();
   		tweet = tweet.toLowerCase();
   		
   		
   		tweet = Tweet.Remove1(tweet);
   		tweet = Tweet.rmv_stopword(tweet);
   		
   		query = Tweet.Remove1(query);
   		query = Tweet.rmv_stopword(query);
   		int dis = 0;
   		String[] parts = tweet.split(" ");
   		String first_splip = "";
   		for(int i = 0; i<parts.length; i++)
   		{
   			if(Term_in_text(parts[i], query))
   			{
   				for(int j = i; j<parts.length; j++)
   					first_splip = first_splip + parts[j]+" ";
   				break;
   			}
   		}
   		first_splip = first_splip.trim();
   		parts = first_splip.split(" ");
   		while(check_q_t(query, first_splip))
   		{
   			for(int i = 1; i< parts.length; i++)
   			{
   				if(Term_in_text(parts[i], query))
   				{
   					dis = dis + i-1;
   					String[] term = first_splip.split(" ");
   					first_splip = "";
   					for(int j = i; j< term.length; j++)
   					{
   						first_splip = first_splip+term[j]+" ";
   					}
   					first_splip = first_splip.trim();
   					parts = first_splip.split(" ");
   					break;
   				}
   					
   			}
   		}
   		System.out.println(dis);
   		return dis;
   	}
   	public static boolean check_q_t(String query, String tweet)//trong cau tweet toan tai 2 tu query
   	{
   		int dem = 0;
   		for (String  term : tweet.split(" ")) {
			if(Term_in_text(term, query)) dem++;
			if(dem==2) return true;
		}
   		return false;
   	}
	public static double TFIDFSimilarity(String query, String doc, Tweet[] tweets, int N)
   	{
   		double result = 0.0;
   		double l = doc.split(" ").length;
   		double d = total_distance(query, doc);
   		double w = 0.2;
   		if(TFIDF.Get_same(doc, query).size()==0) return 0.0;
   		result = tfidf(query, doc, tweets, N)* Math.pow(Math.E,(-w*d)/l);
   	//	System.out.println(Math.pow(Math.E,(-w*d)/l));
   		return result;
   	}
}

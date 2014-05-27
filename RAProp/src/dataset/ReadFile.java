package dataset;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import rank.Tweet;


public class ReadFile {
	
	public static void WriteResult(Tweet[] tweets, String filename, boolean rm_sw, String query)
	{
	try {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		String newLine = System.getProperty("line.separator");
		int i = 0;
		for (Tweet t : tweets) {
			String term = t.content;
			if(rm_sw) 
				term = ReadFile.RemoveAllSW(term, query);			
			writer.write(t.queryID + " " + t.ID + " " + t.label + " "
					+ t.user_name + " \"" + t.content + "\"\n");
			i++;
		}
		
		writer.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	}
	public static String RemoveAllSW(String term, String query) {
		// TODO Auto-generated method stub
		String[] parts = query.split(" ");
		for (String string : parts) {
			term = term.replace(string, "");
		}
		term = term.toLowerCase();
		term = Tweet.Remove1(term);
		//term = term.replace(query, "");
		term = Tweet.rmv_stopword(term);
		term = term.replace("\\", "");
		term = term.replaceAll("( )+", " ");
////		tweets[i].rmv_stopword();
////		tweets[i].content = tweets[i].content.replace(query, "");//bo tu query
////		tweets[i].content = tweets[i].content.replaceAll("( )+", " ");
//		String test = "dadada \\dasda";
//		test = test.replace("\\", "");
		return term;
	}
	
	
	//doc 200 tweet
//	public static Tweet[] GetNtweet (int N, String querry) throws IOException
//	{
//		Tweet[] tweets = new Tweet[N];
//		String topic = Query.TopicDectection(querry);
//		int i = 0;
//		File folder = new File("dataset/"+topic);
//		File[] listOfFiles = folder.listFiles();
//		for (File file : listOfFiles) {
//			if(file.length()==0) continue;
//			System.out.println("-------------------"+file.getName());
//			BufferedReader reader = new BufferedReader(new FileReader(file));
//			String term;
//			if(i==N) return tweets;
//			while((term = reader.readLine())!=null)
//			{
//				if(i==N) return tweets;
//				long id;
//				String content ="";
//				String[] parts = term.split("  ");
//				id = Long.parseLong(parts[1]);
//				for(int j = 2 ; j<parts.length; j++)
//				{
//					parts[j]=parts[j].replaceAll("\"", "");
//					content += parts[j];
//				}
//				if(i==71)
//				{
//					@SuppressWarnings("unused")
//					String stop="nhung";
//				}
//				if(ContentIsOveride(content, tweets)) continue;
//				else{
//					tweets[i] = new Tweet(content, id);
//					i++;
//				};
//				
//			}
//			reader.close();
//		}
//		return tweets;
//	}
	//doc 200tweet tu file
	public static Tweet[] GetNtweet(String path_file, int N) throws Exception
	{
	//	int N = CountTweet(path_file);		
		Tweet[] tweets = new Tweet[N];
		BufferedReader reader = new BufferedReader(new FileReader(path_file));
		String term;
		int i = 0;
		while((term = reader.readLine())!=null)
		{
			int begin = term.indexOf("\"");
			int end = term.indexOf("\"", begin+1);
			String content = term.substring(begin+1, end);//content
			String[] parts = term.split("\t",8);
			String queryID = parts[0];
			long ID = Long.parseLong(parts[1]);
			int label = Integer.parseInt(parts[2]);
			String user_name = parts[3];
			int favourite = Integer.parseInt(parts[5]);
			int retweet = Integer.parseInt(parts[6]);
			String date = parts[7];
		//	if(ContentIsOveride(content, tweets)) continue;
			tweets[i] = new Tweet(queryID, ID, label, user_name, content, retweet, favourite, date);
			i++;
		}
		reader.close();
		return tweets;
	}
	
	public static boolean ContentIsOveride(String text1, Tweet[] tweet)
	{    //no co roi la true
		if(text1.split(" ").length<4) return true;
		for (Tweet tweet2 : tweet) {
			if(tweet2==null) return false;
			String temr1= Tweet.Remove1(text1);
			String temr2= Tweet.Remove1(tweet2.content);
			//temr1.equals(anObject)
			if(temr1.equals(temr2)) return true;
			if(temr1.contains(temr2)) return true;
			if(temr2.contains(temr1)) return true;
			
		}
		return false;
	}
	public static void GetAG(double[][] ag_graph, int N, String path_file) throws IOException
	{
			BufferedReader reader = new BufferedReader(new FileReader(path_file));
			String term1;
			while(( term1 = reader.readLine())!=null)
			{					
				//String text = "1\2";
				String[] parts = term1.split("  ");
				for (String string : parts) {
					
					String[] parts_1 = string.split("=");
					String[] parts_2 = parts_1[0].split("\\\\");
					double ag =  Double.parseDouble(parts_1[1]);
					int i = Integer.parseInt(parts_2[0]);
					int j = Integer.parseInt(parts_2[1]);
					ag_graph[i][j] = ag;
				}
				
			}
			reader.close();
		
	}
	public static void RankingAG(Tweet[] tweets, double[][] ag_graph, int N)
	{
		for(int i = 0 ; i <N; i++)
		{
			tweets[i].total_AG = AGofI(i, ag_graph, N);
		}
		int max = 0;
		for(int i = 0; i< N-1; i++)
		{
			max = i;
			for(int j =i+1; j<N; j++)
			{
				if(tweets[j].total_AG>tweets[max].total_AG)
					max = j;
			}
			if(max!=i)
			{
				Tweet term = tweets[i];
				tweets[i] = tweets[max];
				tweets[max] = term;
			}
		}
	}
	public static int[] TopK_AG(int K, double[][] ag_graph, int N)
	{
		int dem = 0;;
		int[] topN = new int[N];
		double[]copy_ag = new double[N];
		
		for(int i = 0; i<N; i++)
		{
			copy_ag[i]=  AGofI(i, ag_graph, N);
		}
		
		//selection sort
		int max;
		//int dem;
		for(int i = 0; i<N-1; i++)
		{
			max = i;
			for(int j = i+1; j<N; j++)
			{
				if(copy_ag[max]<copy_ag[j])
					max = j;
			}
			if(max!=i)//swap i vs max
			{
				double term = 0.0;		
				term = copy_ag[max];
				copy_ag[max] = copy_ag[i];
				copy_ag[i] = term;				
				topN[dem] = max;
				dem++;
			}
		}		
		
		int[] result = new int[K];
		for(int i = 0; i<K; i++)
		{
			result[i] = topN[i];
		}
		return result;
	}
	public static double AGofI(int i, double[][] ag_graph, int N)
	{
		double result = 0.0;
		for(int j = 0; j<N; j++)
		{
			result  = result + ag_graph[i][j];
		}
		return result;
	}
	public static void GetTweetResult(Tweet[] tweets, String path_file, int N) throws NumberFormatException, IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(path_file));
		String term ;
		int i=0;
		while((term = reader.readLine())!=null)
		{
			
			long id;
			String content ="";
			String[] parts = term.split("  ");
			id = Long.parseLong(parts[1]);
			for(int j = 2 ; j<parts.length; j++)
			{
				parts[j]=parts[j].replaceAll("\"", "");
				content += parts[j];
			}
			tweets[i] = new Tweet(content, id);
			i++;
			if(i==N) return ;
		}
		reader.close();
	}
	public static int CountTweet(String path_file) throws Exception
	{
		BufferedReader reader1 = new BufferedReader(new FileReader(path_file));
		String term1;
		int N = 0;
		while((term1 = reader1.readLine())!=null)
		{
			N++;
		}
		reader1.close();
		return N;
	}
}

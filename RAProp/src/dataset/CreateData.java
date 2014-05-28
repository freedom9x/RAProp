package dataset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rank.TFIDF;
import rank.Tweet;

public class CreateData {
	public static void main(String[] args) throws Exception
	{
		CreateDataTrain("tweet2011/test/49Query/");
	}

	public static void CreateQueryTable(String path) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(path));
		int dem = 0;
		String term;
		PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/test/QueryTable.txt"));
		while(((term = reader.readLine())!=null))
		{
			if(term.contains("<num>"))
			{
				String term2 = reader.readLine();
				int begin = term.indexOf("<num>");
				String id = term.substring(begin+14,begin+18);
				begin = term2.indexOf("title");
				int end = term2.indexOf("</title>");
				String query  = term2.substring(begin+7, end);
				pw.println(id+"\t"+query);
			}
		}
		pw.close();		
		reader.close();
	}
	
	public static String ComputeFeatures(Tweet tweet, Tweet[] tweets, int N) throws Exception
	{
		//cau truc file train
		//[taget] qid:[query_id] 1:[TFIDFSimilarity] 2:[#retweet] 3:[#favorites] 
		//4:[#hash_tag] 5:[Profile Verified] 6:[#status] 7:[#follower] 8:[#friend-follwing]
		//9:[Creation Time] 
		
		String result = "";
		String query = FindQuery(tweet.queryID);
		//feature tweet
		double simi = TFIDF.TFIDFSimilarity(query, tweet.content, tweets, N);
		int favor_c = tweet.favorite_count;
		int retweet_c = tweet.retweet_count;
		int hash_c = HashTagCount(tweet.content);
	//	int present = Present(content);
		//feature user
		String user_features = GetUserFeature(tweet.user_name);
		String parts_user[] = user_features.split("\t");
		int veri;
		if(parts_user[1].equals("false")) veri = 0;
		else veri = 1;
		int status_c = Integer.parseInt(parts_user[2]);
		int follower = Integer.parseInt(parts_user[3]);
		int following = Integer.parseInt(parts_user[4]);
		int pageRank = CommonLib.PageRank(tweet.content);
		//int Hastag = HashTagCount(tweet.content);
				//cumpute date
		String term[] = user_features.split("\t", 6);
		Date now = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).parse( "Tue Feb 08 00:00:00 +0000 2011");
		Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).parse(term[5]);
		long sub_date = now.getTime()-date.getTime();
		//cau truc file train
		//[taget] qid:[query_id] 1:[TFIDFSimilarity] 2:[#retweet] 3:[#favorites] 
		//4:[#hash_tag] 5:[Profile Verified] 6:[#status] 7:[#follower] 8:[#friend-follwing]
		//9:[Creation Time] 10:[pageRank]
		result =tweet.label+" "+ tweet.queryID+" 1:"+simi+" 2:"+retweet_c+" 3:"+favor_c+" 4:"+hash_c
				+" 5:"+veri+" 6:"+status_c+" 7:"+follower+" 8:"+following+" 9:"+sub_date+" 10:"+pageRank+" #"+tweet.ID;
		//System.out.println(result);
		return result;
	}
	private static String FindQuery(String queryID) throws Exception {
		// TODO Auto-generated method stub4
		BufferedReader reader = new BufferedReader(new FileReader("tweet2011/test/QueryTable.txt"));
		String term;
		
		while((term=reader.readLine())!=null)
		{
			if(term.split("\t")[0].equals(queryID))
				return term.split("\t")[1];
		}
		
		return null;
	}

	private static int Present(String content) {
		// TODO Auto-generated method stub
		String emoticons[] = {"!","?",
				":)", "^^",};
		List<String> list= Arrays.asList(emoticons);
		
		
		
		return 0;
	}
	private static int HashTagCount(String content) {
		// TODO Auto-generated method stub
		String[] parts  = content.split(" ");
		int dem = 0;
		for (String string : parts) {
			if(string.length()>1)
			{
				String term = string.substring(0,1);
				if(term == "#") dem++;
			}
			
		}
		return dem;
	}
	private static String GetUserFeature(String UserName) throws IOException
	{
		 BufferedReader reader = new 
				 BufferedReader(new FileReader("tweet2011/UserFeatures/UserInfo.txt"));
		 String term = "";
		 while(( term = reader.readLine())!=null)
			{
				if(UserName.equals(term.split("\t")[0])) return term;
			}
		 return null;
	}
	private static boolean UserNull(String user) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader("tweet2011/UserFeatures/UserInfo.txt"));
		String term ;
		while((term=reader.readLine())!=null)
		{
			String[] parts = term.split("\t");
			if(user.equals(parts[0]))
			{
				if(term.split("\t")[1].equals("null"))
				{
					reader.close();
					System.out.println(user+"\tnullllllll");
					return true;
				}
					
				else
				{
					reader.close();
					return false;
				}
			}
		}
		reader.close();
		return false;
	}
	private static void CreateDataTrain(String path_folder) throws Exception
	{
		File folder = new File(path_folder);
		File[] listOfFiles = folder.listFiles();
		//PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/TrainData/train.txt", true));
		for (File file : listOfFiles)
		{
			String path = path_folder+file.getName();
			System.out.println(file.getName());
			int N = ReadFile.CountTweet(path);
			Tweet[] tweets = ReadFile.GetNtweet(path, N);
			
			PrintWriter pw = new PrintWriter(new FileWriter("tweet2011/DataTest/"+file.getName(), true));
			String term ;			
			for (Tweet tweet : tweets) {
				term = ComputeFeatures(tweet, tweets, N);
				System.out.println(term);
				pw.println(term);
				
			}
			pw.close();
			System.out.println(file.getName()+"done-------------");
		}
	}
}

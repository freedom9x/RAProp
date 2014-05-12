package vali;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import dataset.ReadFile;
import rank.Tweet;


public class TopK_result {

	Tweet[] tweets ;
	static double[][] AG_graph;
	static int K =30;
	static int[] topK= new int[30];
	
	public static void main(String path_tweet, String path_AG) throws Exception {
		// TODO Auto-generated method stub
		int N = ReadFile.CountTweet(path_tweet);
		Tweet[] tweets = new Tweet[N];
		AG_graph = new double[N+1][N];
		//ReadFile.GetTweetResult(tweets, "/tweet2011/Available/MB01_200_fix.txt", N);
		tweets = ReadFile.GetNtweet(path_tweet);
		ReadFile.GetAG(AG_graph, N, path_AG);
		ReadFile.RankingAG(tweets, AG_graph, N);
		//topK = ReadFile.TopK_AG(K, AG_graph, N);
		File file2 = new File(path_AG);
		String name = file2.getName().split("_")[0];
		PrintWriter pw = new PrintWriter(
				new FileWriter("tweet2011/result/AG/"+name+"_result.txt"));
		for(int i = 0 ; i < N; i++)
		{
			System.out.println(tweets[i].total_AG);
			pw.println(i+" "+tweets[i].ID+" "+tweets[i].label+" \""+tweets[i].content+"\""+
					" "+"total_AG="+tweets[i].total_AG);
		}
		pw.close();
		System.out.print("done");
	}
	
}

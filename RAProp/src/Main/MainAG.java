package Main;

import java.io.IOException;

import rank.AG_graph;
import vali.TopK_result;
import dataset.CreateData;

public class MainAG {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		CreateData.main("tweet2011/tweetN");// lay tweet, bo reteet, lu trong test folder
		//AG_graph.main("BBC World Service staff cuts", "MB01", "tweet2011/test/MB01_AG.txt");
		//TopK_result.main("tweet2011/test/MB01_AG.txt", "tweet2011/test/MB01_ag.txt");
		
	}

}

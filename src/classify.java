import java.lang.*;
import java.util.*;
import java.io.*;


public class classify {
	
	static File train_Hloc=new File("Z:/School/Machine Learning/HW2/train/ham");	
	static File train_Sloc=new File("Z:/School/Machine Learning/HW2/train/spam");
	static File trainH[] = train_Hloc.listFiles();
	static File trainS[]= train_Sloc.listFiles();
	static String s,t;
	static int Sstored=0,Hstored=0,Tstored=0;
	static String[]hammer= new String[50000];
	static int[] hammer_index= new int[50000];
	static String[]spammer= new String[50000];
	static int[] spammer_index= new int[50000];
	static String[]testing= new String[50000];
	static int[] testing_index= new int[50000];
	static double[] condprob_test= new double[150000];
	static double[] condprob_test2= new double[150000];
	static int ham=0,spam=0;
	static double[] score=new double[2];
	
	
	
	public static void main(String args[]) throws IOException
	{
	// Cleaning condprob 1 and 2;
		score[0]=0;score[1]=0;
		for(int ij=0;ij<Hstored;ij++)
		{condprob_test[ij]=0;}
		for(int ij=0;ij<Sstored;ij++)
		{condprob_test2[ij]=0;}
		//START OF HAM READER
		 
		int j=0;int flag=0,flag_1=0;
		for(int i=0;i<trainH.length;i++)
			{
			try{
				
			FileReader ham_file = new FileReader(trainH[i]);
			BufferedReader hbr = new BufferedReader(ham_file);
			s=hbr.readLine();
			
			while(hbr.readLine()!=null)
			{	StringTokenizer st = new StringTokenizer(s," ");
				
				while (st.hasMoreTokens())
				{flag=0;
					String temp=st.nextToken();
					for(int z=0;z<Hstored;z++)
						{
						if(temp.equals(hammer[z])) 
						{
						hammer_index[z]++;
						//System.out.println("duplicate at: "+z);
						flag=1;
						}
						}
					if (flag==0)
					{	hammer[j]=temp;
						hammer_index[j]=0;
						//System.out.println(hammer[j]+"   "+hammer_index[j]+"   ");
						j++;Hstored++;
					}			
					

				}
				
			}
			//for(int d=0;d<stored;d++)
				//System.out.println(hammer[d]+"   "+hammer_index[d]);
			}
			catch(Exception fe) {System.out.println(fe);}
			}// end of ham read
		
		//START SPAM READER
		
		int j1=0;int flag1=0;Sstored=0;
		for(int i=0;i<trainS.length;i++)
			{
			try{
				
			FileReader spam_file = new FileReader(trainS[i]);
			BufferedReader sbr = new BufferedReader(spam_file);
			s=sbr.readLine();
			
			while(sbr.readLine()!=null)
			{	StringTokenizer st = new StringTokenizer(s," ");
				
				while (st.hasMoreTokens())
				{flag1=0;
					String temp=st.nextToken();
					for(int z=0;z<Sstored;z++)
						{
						if(temp.equals(spammer[z])) 
						{
						spammer_index[z]++;
						//System.out.println("duplicate at: "+z);
						flag1=1;
						}
						}
					if (flag1==0)
					{	spammer[j1]=temp;
						spammer_index[j1]=0;
						//System.out.println(spammer[j1]+"   "+spammer_index[j1]+"   ");
						j1++;Sstored++;
					}			
					

				}
				
			}
			//for(int d=0;d<stored;d++)
				//System.out.println(spammer[d]+"   "+spammer_index[d]);
			}
			catch(Exception fe) {System.out.println(fe);}
			}// end of SPAM read
		
		final int ham_length,spam_length;
		ham_length= trainH.length;
		spam_length=trainS.length;
		System.out.println("The length of training data is as follows-");
		System.out.println("No of Ham Class Files:    "+ham_length);
		System.out.println("No of Spam Class Files:   "+spam_length);
		
		
		// IMPLEMENTATION OF MULTINOMIAL NAIVE BAYESIAN \m/
		
		int N = ham_length+spam_length;
		int Nh = ham_length;
		int Ns = spam_length;
		double Nc_h= (double) Nh/N;
		double Nc_s= (double) Ns/N;
		//System.out.println(Nc_h+"   "+Nc_s);
		
		double[][] condprob=new double[50000][2];
		double[] prior= new double[2];
		prior[0]=Nc_h;
		prior[1]=Nc_s;
		int Tct_s=0,Tct_h=0;
		for(int i=0;i<Nh;i++)
			Tct_h+=hammer_index[i];
		for(int i=0;i<Ns;i++)
			Tct_s+=spammer_index[i];
		for(int i1=0;i1<Nh;i1++)
			condprob[i1][0]=(double)(hammer_index[i1]+1)/(double)(1+Tct_h);
		for(int i1=0;i1<Nh;i1++)
			condprob[i1][1]=(double)(spammer_index[i1]+1)/(double)(1+Tct_s);
		
		// READ NEW FILES AND ANALYSE
		
		train_Hloc=new File("Z:/School/Machine Learning/HW2/test/ham");	
		train_Sloc=new File("Z:/School/Machine Learning/HW2/test/spam");
		File trainH[] = train_Hloc.listFiles();
		File trainS[]= train_Sloc.listFiles();
		
		j=0; flag=0;Tstored=0;
		
		for(int i=0;i<trainH.length;i++)
			{
			try{
				
			FileReader ham_file = new FileReader(trainH[i]);
			BufferedReader hbr = new BufferedReader(ham_file);
			s=hbr.readLine();
			
			while(hbr.readLine()!=null)
			{	StringTokenizer st = new StringTokenizer(s," ");
				
				while (st.hasMoreTokens())
				{flag=0;
					String temp=st.nextToken();
					double x1=0;
					for(int beg=0;beg<Hstored;beg++)
						{if(temp.equals(hammer[beg]))
							{x1=(double)Math.log(hammer_index[beg]+Math.log(prior[0]));
							condprob_test[beg]+=(x1);break;}}
						
							for(int beg=0;beg<Sstored;beg++)
								{if(temp.equals(spammer[beg]+Math.log(prior[1])))
									{x1=(double)Math.log(spammer_index[beg]);
									condprob_test2[beg]+=(x1);break;}}
					for(int z=0;z<Tstored;z++)
						{
						if(temp.equals(testing[z])) 
						{
						testing_index[z]++;
						
						//System.out.println("duplicate at: "+testing[z]);
						flag=1;
						}
									
								
						}
					if (flag==0)
					{	testing[j]=temp;
						testing_index[j]=0;
						
						j++;Tstored++;
					}			
					

				}
				
			}
			
			//for(int d=0;d<stored;d++)
				//System.out.println(hammer[d]+"   "+hammer_index[d]);
			}
			catch(Exception fe) {System.out.println(fe);}
			
			
			for(int k=0;k<Hstored;k++)
				try{if(condprob_test[k]>0&&condprob_test[k]<5)score[0]= score[0]+(condprob_test[k]/Tct_h);}catch(Exception e){}
			for(int k=0;k<Sstored;k++)
				try{if(condprob_test2[k]>0&&condprob_test2[k]<5)score[1]= score[0]+(condprob_test2[k]/Tct_s);}catch(Exception e){}
		    
			if((double)score[0]>(double)score[1]){ham++;}
			else {spam++;}
			
			}
			
			// SPAM TRAINING::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
			
			j=0; flag=0;Tstored=0;
			
			for(int ik=0;ik<trainS.length;ik++)
				{
				try{
					
				FileReader spam_file = new FileReader(trainS[ik]);
				BufferedReader sbr = new BufferedReader(spam_file);
				s=sbr.readLine();
				
				while(sbr.readLine()!=null)
				{	StringTokenizer st = new StringTokenizer(s," ");
					
					while (st.hasMoreTokens())
					{flag=0;
						String temp=st.nextToken();
						double x1=0;
						for(int beg=0;beg<Hstored;beg++)
							{if(temp.equals(hammer[beg]))
								{x1=(double)Math.log(hammer_index[beg]+Math.log(prior[0]));
								condprob_test2[beg]+=(x1);break;}}
							
								for(int beg=0;beg<Sstored;beg++)
									{if(temp.equals(spammer[beg]))
										{x1=(double)Math.log(spammer_index[beg]+Math.log(prior[1]));
										condprob_test2[beg]+=(x1);break;}}
						for(int z=0;z<Tstored;z++)
							{
							if(temp.equals(testing[z])) 
							{
							testing_index[z]++;
							
							//System.out.println("duplicate at: "+testing[z]);
							flag=1;
							}
										
									
							}
						if (flag==0)
						{	testing[j]=temp;
							testing_index[j]=0;
							
							j++;Tstored++;
						}			
						

					}
					
				}
				
				//for(int d=0;d<stored;d++)
					//System.out.println(hammer[d]+"   "+hammer_index[d]);
				}
				catch(Exception fe) {System.out.println(fe);}
			
			
			// CALCULATION OF CONDITIONAL PROBABILITY OF TESTING DATA
			for(int k=0;k<Hstored;k++)
				try{if(condprob_test[k]>0&&condprob_test[k]<5)score[0]= score[0]+(condprob_test[k]/Tct_h);}catch(Exception e){}
			for(int k=0;k<Sstored;k++)
				try{if(condprob_test2[k]>0&&condprob_test2[k]<5)score[1]= score[0]+(condprob_test2[k]/Tct_s);}catch(Exception e){}
		    
			if((double)score[0]>(double)score[1]){ham++;}
			else {spam++;}
			}
			
		//System.out.println("Total Hams="+ham+"   "+"Total Spams="+spam);
		double accuracy=0;
		accuracy=((((double)ham_length+1)/ham)+(((double)spam_length+1)/spam))/2;
		accuracy=accuracy*100;
		System.out.println("Accuracy="+accuracy);
	
		//for(j=0;j<Hstored;j++)
			//System.out.println(condprob_test[j]);
		// SCORE CALCscore
		
		
// ADDING STOP WORDS
		
	
		File one=new File("Z:/School/Machine Learning/HW2/STOP.txt");
		String[] stopwords=new String[200];
		String part;
		FileReader wordfile;
		try {
		
			wordfile = new FileReader(one);
			BufferedReader wr = new BufferedReader(wordfile);
			part=wr.readLine();
			int init=0;
			while(wr.readLine()!=null)
			{	StringTokenizer st = new StringTokenizer(part,"\n");
				
				while (st.hasMoreTokens())
				{ stopwords[init]=st.nextToken();
				init++;
				}
			}
		} catch (FileNotFoundException e){e.printStackTrace();}
		System.out.println("Running the Algorithm using Stop words:");
		
		
		
		
		
		
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		

		
		// Cleaning condprob 1 and 2;
			score[0]=0;score[1]=0;
			for(int ij=0;ij<Hstored;ij++)
			{condprob_test[ij]=0;}
			for(int ij=0;ij<Sstored;ij++)
			{condprob_test2[ij]=0;}
			//START OF HAM READER
			 
			j=0;flag=0;flag_1=0;
			for(int i=0;i<trainH.length;i++)
				{
				try{
					
				FileReader ham_file = new FileReader(trainH[i]);
				BufferedReader hbr = new BufferedReader(ham_file);
				s=hbr.readLine();
				
				while(hbr.readLine()!=null)
				{	StringTokenizer st = new StringTokenizer(s," ");
					
					while (st.hasMoreTokens())
					{flag=0;
						String temp=st.nextToken();
						
						// Removing Stopwords.. !! 
						for(int key=0;key<50;key++)
						if(temp.equals(stopwords[key]))st.nextToken();
						
						
						for(int z=0;z<Hstored;z++)
							{
							if(temp.equals(hammer[z])) 
							{
							hammer_index[z]++;
							//System.out.println("duplicate at: "+z);
							flag=1;
							}
							}
						if (flag==0)
						{	hammer[j]=temp;
							hammer_index[j]=0;
							//System.out.println(hammer[j]+"   "+hammer_index[j]+"   ");
							j++;Hstored++;
						}			
						

					}
					
				}
				//for(int d=0;d<stored;d++)
					//System.out.println(hammer[d]+"   "+hammer_index[d]);
				}
				catch(Exception fe) {System.out.println(fe);}
				}// end of ham read
			
			
			
			System.out.println("The length of training data is as follows-");
			System.out.println("No of Ham Class Files:    "+ham_length);
			System.out.println("No of Spam Class Files:   "+spam_length);
			
			
			// IMPLEMENTATION OF MULTINOMIAL NAIVE BAYESIAN \m/
			
			
			
			prior[0]=Nc_h;
			prior[1]=Nc_s;
			Tct_s=0;Tct_h=0;
			for(int i=0;i<Nh;i++)
				Tct_h+=hammer_index[i];
			for(int i=0;i<Ns;i++)
				Tct_s+=spammer_index[i];
			for(int i1=0;i1<Nh;i1++)
				condprob[i1][0]=(double)(hammer_index[i1]+1)/(double)(1+Tct_h);
			for(int i1=0;i1<Nh;i1++)
				condprob[i1][1]=(double)(spammer_index[i1]+1)/(double)(1+Tct_s);
			
			
			
				// CALCULATION OF CONDITIONAL PROBABILITY OF TESTING DATA
				for(int k=0;k<Hstored;k++)
					try{if(condprob_test[k]>0&&condprob_test[k]<5)score[0]= score[0]+(condprob_test[k]/Tct_h);}catch(Exception e){}
				for(int k=0;k<Sstored;k++)
					try{if(condprob_test2[k]>0&&condprob_test2[k]<5)score[1]= score[0]+(condprob_test2[k]/Tct_s);}catch(Exception e){}
			    
				if((double)score[0]>(double)score[1]){ham++;}
				else {spam++;}
				
				
			//System.out.println("Total Hams="+ham+"   "+"Total Spams="+spam);
			accuracy=0;
			accuracy=((((double)ham_length+1)/(ham))+(((double)spam_length+3)/(spam)))/2;
			accuracy=accuracy*100;
			System.out.println("Accuracy="+accuracy);
		
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			
			// Applying Logistic Regression...
			
			
			
		
	}// end of main 


}// end of class


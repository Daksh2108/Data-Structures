package friends;


import structures.Queue;
import structures.Stack;

import java.util.*;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {

		/** COMPLETE THIS METHOD **/

		Person[] memb;
		memb = g.members;
		ArrayList<String> shortestChain = new ArrayList<String>();
		int n = memb.length;

		int A[] = new int[n];
		int B[] = new int[n];
		int C[] = new int[n];
		boolean visited[] = new boolean[n];

		for(int i=0;i<n;i++)
		{

			B[i]=Integer.MAX_VALUE;
		}
		B[g.map.get(p1)]=0;



		for(int i=0;i<n; i++)
			A[i]=i;

		for(int i=0;i<n-1;i++)
		{

			int minVertex = findMinVertex(B,visited);
			visited[minVertex]=true;
			if(B[minVertex]!=Integer.MAX_VALUE)
			{

				while(memb[minVertex].first!=null)
				{
					int t = memb[minVertex].first.fnum;
					if(B[t]>B[minVertex]+1)
					{
						B[t] = B[minVertex]+1;
						C[t]=minVertex;
					}

					memb[minVertex].first = memb[minVertex].first.next;
				}
			}

		}
		int sourceIndex = g.map.get(p1);
		int destinationIndex = g.map.get(p2);
		if(sourceIndex==destinationIndex)
		{
			shortestChain.add(0,memb[destinationIndex].name);
			shortestChain.add(0,memb[destinationIndex].name);
			return shortestChain;
		}
		if(B[destinationIndex]==Integer.MAX_VALUE)
			return null;
		shortestChain.add(0,memb[destinationIndex].name);
		while(true)
		{
			if(B[destinationIndex]==Integer.MAX_VALUE)
				return null;
			shortestChain.add(0,memb[C[destinationIndex]].name);
			destinationIndex = C[destinationIndex];
			if(destinationIndex==sourceIndex)
			{
				break;
			}
		}

		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY
		// CHANGE AS REQUIRED FOR YOUR IMPLEMENTATION
		return shortestChain;
	}

	private static int findMinVertex(int[] b, boolean[] visited) {
		int minVertex = -1;
		for(int i = 0; i < b.length;i++)
		{
			if(!visited[i] && (minVertex == -1 || b[i] < b[minVertex]))
			{
				minVertex = i;
			}
		}
		return minVertex;
	}

	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		if(school==null){
			return null;
		}
		if(school.isEmpty()){
			return null;
		}
		ArrayList<ArrayList<String>> cliques=new ArrayList<ArrayList<String>>();
		Person[] allMember=g.members;
		
		ArrayList<String> schoolMembers= getSchoolMembers(allMember,school);
		
		Set<String> groupMembers=new TreeSet<>();
		
		for(String smember : schoolMembers){
			int perIndex=g.map.get(smember);
			Person per=g.members[perIndex];
			
			Friend ptr = per.first;
			while(ptr!=null){
				if(per.student && per.school.endsWith(school)){
					Person frnd=g.members[ptr.fnum];
					
					
					if(frnd.student && frnd.school.endsWith(school)){
						groupMembers.add(frnd.name);
						
					}
				}
				ptr=ptr.next;
			}
			
		}
		
		
		
		
		
		
		
		cliques.add(schoolMembers);
		Set<String> stname=new TreeSet<>();
		for(String person : schoolMembers){
			
			int pIndex=g.map.get(person);
			
			Friend ptr=g.members[pIndex].first;
					
			while(ptr!=null){/*
				Person per=g.members[ptr.fnum];
				if(ptr.student && person.school.equalsIgnoreCase(school)){
					stname.add(per.name);
				}
				ptr=ptr.next;
				
			*/}
		}
		
		return cliques;
		
		

	}


	private static ArrayList<String> getSchoolMembers(Person[] allMember, String school) {
		
		ArrayList<String> schoolMembers=new ArrayList<>();
		for(Person member : allMember){
			
			if(member.student && member.school.equalsIgnoreCase(school)){
				schoolMembers.add(member.name);
			}
		}
		
		return schoolMembers;
	}

	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {

		ArrayList<String> connectors = new ArrayList<>();
       int allMembersLength=g.members.length;

        boolean[] travesed = new boolean[allMembersLength]; 
        int[] noOfSearch = new int[allMembersLength];
        int[] perivious = new int[allMembersLength];
        Person [] members=g.members;
        for (Person member : members) {
            if (!travesed[g.map.get(member.name)]){
            	noOfSearch = new int[allMembersLength];
                chekforConnectors(g.map.get(member.name), g.map.get(member.name), g,
                		travesed, noOfSearch, perivious, connectors);
            }
        }

        if(connectors.size()>0){
        	
        
        for (int i = 0; i < connectors.size(); i++) {
            Friend ptr = g.members[g.map.get(connectors.get(i))].first;

            int count = 0;
            while (ptr != null) {
                ptr = ptr.next;
                count++;
            }

            if (count == 0 || count == 1) {
            	connectors.remove(i);
            }
        }
        }

        for (Person member : g.members) {
            if ((member.first.next == null && !connectors.contains(g.members[member.first.fnum].name))) {
            	connectors.add(g.members[member.first.fnum].name);
            }
        }

        return connectors;
	}


	
	private static void chekforConnectors(int memberIndex, int start, Graph g,
			boolean[] travesed, int[] noOfMembers, int[] perivious, ArrayList<String> answer){
        Person p = g.members[memberIndex];
        travesed[g.map.get(p.name)] = true;
        int count = 0;
        
        for (int i = 0; i < noOfMembers.length; i++) {
            if (noOfMembers[i] != 0) {
                count++;
            }
        }

        if (noOfMembers[memberIndex] == 0 && perivious[memberIndex] == 0) {
        	noOfMembers[memberIndex] = count;
            perivious[memberIndex] = noOfMembers[memberIndex];
        }

        for (Friend ptr = p.first; ptr != null; ptr = ptr.next) {
            if (!travesed[ptr.fnum]) {

            	chekforConnectors(ptr.fnum, start, g, travesed, noOfMembers, perivious, answer);

                if (noOfMembers[memberIndex] > perivious[ptr.fnum]) {
                    perivious[memberIndex] = Math.min(perivious[memberIndex], perivious[ptr.fnum]);
                } else {
                    if (Math.abs(noOfMembers[memberIndex]-perivious[ptr.fnum]) < 1 && Math.abs(noOfMembers[memberIndex]-noOfMembers[ptr.fnum]) <=1 && perivious[ptr.fnum] ==1 && memberIndex == start) {
                        continue;
                    }

                    if (noOfMembers[memberIndex] <= perivious[ptr.fnum] && (memberIndex != start || perivious[ptr.fnum] == 1 )) { 
                        if (!answer.contains(g.members[memberIndex].name)) {
                            answer.add(g.members[memberIndex].name);
                        }else{
                        	//check else 
                        }
                    }

                }
            } else {
                perivious[memberIndex] = Math.min(perivious[memberIndex], noOfMembers[ptr.fnum]);
            }
        }
        return;
    }
}


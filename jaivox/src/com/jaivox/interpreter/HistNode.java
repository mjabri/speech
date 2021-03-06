/*
   Jaivox version 0.5 August 2013
   Copyright 2010-2013 by Bits and Pixels, Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.jaivox.interpreter;

import java.util.TreeMap;

public class HistNode {

	public static int quad = 4;
	String fsmNode [];
	String userInput;
	String systemResponse;
	TreeMap <Integer, String> matches;

/**
 * Create a history node. This is generally called from Script
 * after figuring out the response to a user input.
 * @param fnode
 * @param input
 * @param answer
 */
	
	public HistNode (String fnode [], String input, String answer) {
		matches = new TreeMap <Integer, String> ();
		fsmNode = new String [quad];
		fsmNode = (String [])fnode.clone ();
		userInput = input;
		systemResponse = answer;
	}

/**
 * Create a history node with a list of possible matches to a user's
 * input. Since the user input may not have been understood correctly,
 * the history node is created with a small list of questions that match
 * the user's input. the list is sorted with the closest match first.
 * @param fnode
 * @param input
 * @param answer
 * @param map
 */
	
	public HistNode (String fnode [], String input, String answer, TreeMap <Integer, String> map) {
		matches = map;
		fsmNode = new String [quad];
		fsmNode = (String [])fnode.clone ();
		userInput = input;
		systemResponse = answer;
	}

/**
 * A readable form of the history node.
 * @return
 */
	
	public String toString () {
		StringBuffer sb = new StringBuffer ();
		sb.append (fsmNode[0]+" "+fsmNode[1]+" "+fsmNode[2]+" "+fsmNode[3]+"\n");
		sb.append (userInput+" / "+systemResponse+"\n");
		if (matches != null) sb.append (matches.toString ()+"\n");
		else sb.append ("no matches \n");
		sb.append ("--------------------------------------");
		String result = new String (sb);
		return result;
	}

/**
 * Get the finite state machine transition associated with this node.
 * @return
 */
	
	public String [] getFsmNode () {
		return fsmNode;
	}
	
/**
 * Get the matches available at this node. Since speech recognition may
 * not be perfect, there could be more than one user input that matches
 * the recognized string. These are ordered with the most likely match
 * first.
 * @return
 */

	public TreeMap<Integer, String> getMatches () {
		return matches;
	}

/**
 * Get the system response, i.e. what is said by the interpreter in
 * response to a user input. Note that the system can speak on its own
 * when user input is something corresponding to "no input" (for example
 * on no user input the system can say "say something".)
 * @return
 */
	
	public String getSystemResponse () {
		return systemResponse;
	}
	
/**
 * Get the user input associated with this history node. Note that the
 * user input may be any string, it does not have to be something the
 * user said. The string should however be accounted for in the finite
 * state machine.
 * @return
 */

	public String getUserInput () {
		return userInput;
	}

/**
 * Set the finite state production. Note that this version does not
 * clone in the incoming information.
 * @param fsmNode
 */
	
	public void setFsmNode (String[] fsmNode) {
		this.fsmNode = fsmNode;
	}

/**
 * Set the matches in this history node to be the given map which
 * associates a confidence score -100 to -0, with the lower numbers
 * corresponding to the best matches.
 * @param matches
 */
	
	public void setMatches (TreeMap<Integer, String> matches) {
		this.matches = matches;
	}

/**
 * Set the system;s response in his history node
 * @param systemResponse
 */
	
	public void setSystemResponse (String systemResponse) {
		this.systemResponse = systemResponse;
	}

/**
 * Set the user's input in this node.
 * @param userInput
 */
	
	public void setUserInput (String userInput) {
		this.userInput = userInput;
	}

}

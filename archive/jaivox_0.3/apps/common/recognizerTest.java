/*
   Jaivox version 0.3 December 2012
   Copyright 2010-2012 by Bits and Pixels, Inc.

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

/**
 * PATrecognizerTest creates a SphinxServer. It also uses Sphinx as a library
 * to recognize spoken questions.
 */

import java.io.*;
import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import com.jaivox.recognizer.sphinx.*;
import com.jaivox.util.*;

public class PATrecognizerTest extends Thread {

	static int port = PATport_recognizer;
	static int waitTime = 1000; // one second

	static String work1 =
	"send PATrecognizer_0 {action: interpret, from: PATrecognizer, to: PATinterpreter, message: ";
	static String connect1 = "connect localhost PATport_interpreter";
	static String who1 = "send PATrecognizer_0 {action: JviaWho, from: PATrecognizer, to: PATinterpreter, message: Jviawho}";
	static String config = "PATrecognizer_config_file";

	static void processSpeech (SphinxServer server) {
		ConfigurationManager cm = new ConfigurationManager (
				PATrecognizerTest.class.getResource (config));
		// allocate the recognizer
		Log.info ("Loading...");
		Recognizer recognizer = (Recognizer) cm.lookup ("recognizer");
		recognizer.allocate ();

		// start the microphone or exit if the programm if this is not possible
		Microphone microphone = (Microphone) cm.lookup ("microphone");
		if (!microphone.startRecording ()) {
			Log.severe ("Cannot start microphone.");
			recognizer.deallocate ();
			System.exit (1);
		}

		System.out.println ("Sample questions are in PATlm_training_file");

		// loop the recognition until the programm exits.
		while (true) {
			System.out.println ("Start speaking. Press Ctrl-C to quit.\n");

			Result result = recognizer.recognize ();

			if (result != null) {
				String resultText = result.getBestResultNoFiller ();
				System.out.println ("You said: " + resultText + '\n');
				String name = server.getServerId ();
				String message = work1 + "\"" + resultText + "\"}";
				server.execute (message);
				try {
					Thread.sleep (4000);
				} catch (Exception e) {
					System.out.println (e.toString ());
					break;
				}
			} else {
				System.out.println ("I can't hear what you said.");
			}
		}
	}

	public static void main (String args []) {
		SphinxServer Recognizer;
		Log log = new Log ();
		log.setLevelByName ("PATlog_level");
		try {
			Recognizer = new SphinxServer ("PATrecognizer", PATport_recognizer);
			BufferedReader in = new BufferedReader (
				new InputStreamReader (System.in));

			Log.info ("Waiting to connect to PATinterpreter");
			boolean connected = false;
			int waiting = 0;
			while (!connected && waiting < 1000) {
				sleep (waitTime);
				String result = Recognizer.executeReply (connect1);
				if (result.startsWith ("OK:")) {
					connected = true;
					break;
				}
				else {
					waiting++;
					System.out.println ("Waiting ... "+waiting);
					continue;
				}
			}
			if (!connected) {
				Log.severe ("Waiting for too long, terminating ...");
				return;
			}
			else {
				Log.info ("Connected to PATinterpreter");
				Log.info ("Waiting to establish who credentials");
			}
			sleep (waitTime * 4);
			boolean established = false;
			waiting = 0;
			while (!established && waiting < 100) {
				sleep (waitTime);
				String result = Recognizer.executeReply (who1);
				if (result.startsWith ("OK:")) {
					established = true;
					break;
				}
				else {
					waiting++;
					Log.info ("Establishing credentials ... "+waiting);
					continue;
				}
			}
			if (!established) {
				Log.severe ("Establishing did not work, terminating ...");
				return;
			}
			else {
				Log.info ("Established credentials with PATinterpreter");
				Log.info ("Processing recognition results");
			}

			sleep (waitTime * 4);

			// loop in SpeechInput
			processSpeech (Recognizer);
			System.out.println ("Terminating ...");
			System.exit (1);
		}
		catch (Exception e) {
			e.printStackTrace ();
		}
	}

}

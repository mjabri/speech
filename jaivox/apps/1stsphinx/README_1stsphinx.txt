
This README is based on Jaivox version 0.5, August, 2013

This directory contains a self-contained batch program that illustrates many
aspects of Jaivox.

Before trying anything though you need four things

1. You need to download and install Sphinx4. The Sphinx4 classes should
   be available in your classpath. The Sphinx4 audio model
   WSJ_8gau_13dCep_16k_40mel_130Hz_6800Hz should be in your classpath.

2. (Optional for this directory:
   We have placed some files that you need to generate using a language
   modeling tookit, for your convenience.
   You can build the language model using the files in this directory.
   This requires the CMU/Cambridge language modeling toolkit. If all the
   required ingredients are installed, then the language model can be
   generated by running lmgen.sh in this directory)

3. To synthesize answers, you need Free TTS. Download and install this
   package. See README_freetts.txt in the apps directory (parent of this
   directory) for information on compiling and installing Free TTS.
   The jar file freetts.jar should be in your classpath, please note that
   free TTS uses this location to find other files that also should be
   in the same directory as freetts.jar.

4. Finally you need to have the Jaivox classes in your classpath.

To test, from a shell window enter

sh compbatch.sh

This should compile cleanly without error messages. To run the program

sh runbatch.sh

You can use a headset to listen to the recordings that are passed to the
recognizer, and to hear the answers being generated through freetts. some
diagnostic information is printed to the screen, and a file InterYYMMDD.txt
(i.e. YY is current year, MM is current month and DD is current day)
is created showing the questions that are recognized and the anwers that are
generated.

Details of the Java programs in this directory:

Answer.java
	Most of the information in answers is supplied by other programs.
	The Answer class uses phrases in answer_en.txt to fill out the rest
	of the formulated answer. Note that the _en.txt here indicates that
	in this example, the answers forms use English, but other languages
	mayb be substituted by changing answer_en.txt to another language,
	for example answer_es.txt will be for Spanish.

batchTest.java
	The batchTest program is mostly generated by tools.Jvgen. But the
	version here is modified to include some specific wav file names in
	the recorded [] array. These wav files are in the ../audio directory.
	The batchTest class also passes commands involving the custom functions
	"ask" and "find" to roadCommand.java.

cmdProc.java
	roadCommand gets questions that involve "ask" and "find" functions. The
	real work of figuring out the answer is done in cmdProc. From the question,
	cmdProc determines the query to be run on data in road.txt, then uses
	some ranges of values defined for each quantifier (e.g. what is "faster"
	compared to "fast"?) it determines the information to create the answer.
	This is combined with phrases from Answer class to create the actual
	response to the question that is passed back to batchTest via roadCommand.

roadCommand.java
	The outline of this program is created by the generator tools.Jvgen.
	This version edits it to create a cmdProc to do the work of answering
	questions based on user-defined functions. In this case, there are two
	user-defined functions: ask and find.


Some of the other files in this directory

answer_en.txt
	These are English phrases used to construct full sentences for
	answers. If we had a different language, we sould use another two
	letter code for the language (en is English, es for example is Spanish.)

batch.xml
	The Sphinx recognizer uses this file to load information about dictionary,
	language model and audio model.

ccs.ccs
	This is a simple file needed by the language modeling toolkit.

common_en.txt
	Common words in English. This is to avoid confusion between words specific
	to this application and very common words.

compbatch.sh
	A shell script to compile the Java programs here. It includes a change
	to the classpath to include the path to freetts.jar.

errors.dlg
	If the recognition is not perfect, then the words that are recognized
	may not correspond exactly to any particular input that is expected (expected
	inputs are in road.sent.) The errors dialog handles asking the user to
	enter input again, or asks for confirmation whether a question has been
	correctly understood. All the applications in this release (0.4) of
	Jaivox uses the same errors.dlg. This file is included in the subject-
	specific dialog, here road.dlg includes errors.dlg.

lmgen.sh
	Shell script that uses the CMU/Cambridge language modeling toolkit to
	create the language model. The form used here is road.arpabo.DMP.

recorded.txt
	batchTest recognizes recordings of questions in the ../audio directory.
	This file contains the actual questions that were recorded (by one of
	us at Jaivox.) Note that the speaker in this case is a U.S. English
	speaker. The audio model we are using, WSJ model from CMU, is also
	recorded using U.S. speakers.

road.arpabo
	This is a version of the language model in the "ARPA" format. Essentially
	it is a calculation of unigram, bigram and trigram probabilities. This
	is used by Sphinx to guess the recognized words.

road.arpabo.DMP
	The "dump" format of road.arpabo. This is created using a program
	sphinx_lm_convert that is installed with sphinxbase.

road.dlg
	The dialog model for this application. This model includes errors.dlg.
	The model is a combination of a finite state machine and a grammar
	generator. This is used to generate the questions that can be recognized
	by this system. It is also used during runtime to figure out the appropriate
	answer to a recognized question.

road.idngram
	This is an intermediate file generated by the language modeling toolkit.

road.quest
	The questions that can be recognized are in this file. Along with each
	question, we also have some grammar information and some semantic information.
	The first column of this file is pretty much the same as road.sent (except
	for some markers like <s> and </s>.

road.sent
	This is essentially the first column of road.quest. It is used by the
	language modeling toolkit as its basic input to generate the language model.

road.spec
	Data specifications for data in road.txt. This says that the data consists
	of three columns, made up of a "field" road with "attributes"
	fast and smooth. You can think of "field"s as nouns and "attributes" as
	adjectives or adverbs. This file also gives alternate ways in which the
	field or attributes may appear in a question.

road.txt
	Data for this application. It lists road names along with values between
	0 and 100 indicating both speed ("fastness") and smoothness.

road.vocab
	A list of words generated by the language modeling toolkit.

runbatch.sh
	Batch file to run the program. This includes a modification to the
	classpath to include freetts.jar, similar to compbatch.sh



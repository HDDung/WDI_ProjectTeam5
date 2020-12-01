package com.team5.maven.IdentityResolution;

import java.io.File;

import org.slf4j.Logger;

import com.team5.maven.IdentityResolution.blocking.SongBlockingKeyByNameGenerator;
import com.team5.maven.IdentityResolution.comparators.SongAlbumComparatorMaxSim;
import com.team5.maven.IdentityResolution.comparators.SongAlbumComparatorToken;
import com.team5.maven.IdentityResolution.comparators.SongArtistsComparatorLevenshtein;
import com.team5.maven.IdentityResolution.comparators.SongArtistsComparatorToken;
import com.team5.maven.IdentityResolution.comparators.SongNameComparatorLevenshtein;
import com.team5.maven.IdentityResolution.comparators.SongNameComparatorRemoveBrackets;
import com.team5.maven.IdentityResolution.comparators.SongNameComparatorTokenJaccard;
import com.team5.maven.IdentityResolution.model.Song;
import com.team5.maven.IdentityResolution.model.SongXMLReader;

import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class IR_using_machine_learning_dbpedia_musicbrainz {
	
	/*
	 * Logging Options:
	 * 		default: 	level INFO	- console
	 * 		trace:		level TRACE     - console
	 * 		infoFile:	level INFO	- console/file
	 * 		traceFile:	level TRACE	- console/file
	 *  
	 * To set the log level to trace and write the log to winter.log and console, 
	 * activate the "traceFile" logger as follows:
	 *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	 *
	 */

	private static final Logger logger = WinterLogManager.activateLogger("default");
	
    public static void main( String[] args ) throws Exception
    {
		// loading data
		System.out.println("*\n*\tLoading datasets\n*");
		HashedDataSet<Song, Attribute> dbpedia = new HashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/dbpedia_translated.xml"), "/songs/song", dbpedia);
		HashedDataSet<Song, Attribute> musicbrainz = new HashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/musicbrainz_translated.xml"), "/songs/song", musicbrainz);
		
		// load the training set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		gsTraining.loadFromCSVFile(new File("data/goldstandard/gs_dbpedia_musicbrainz_train_ml.csv"));

		// create a matching rule
		String options[] = new String[] { "-S" };
		String modelType = "SimpleLogistic"; // use a logistic regression
		WekaMatchingRule<Song, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule_ml.csv", 1000, gsTraining);
		
		// add comparators
		matchingRule.addComparator(new SongNameComparatorRemoveBrackets());
		matchingRule.addComparator(new SongNameComparatorLevenshtein());
		matchingRule.addComparator(new SongArtistsComparatorToken());
		matchingRule.addComparator(new SongArtistsComparatorLevenshtein());
		matchingRule.addComparator(new SongAlbumComparatorMaxSim());
		matchingRule.addComparator(new SongAlbumComparatorToken());
		
		
		// train the matching rule's model
		System.out.println("*\n*\tLearning matching rule\n*");
		RuleLearner<Song, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(dbpedia, musicbrainz, null, matchingRule, gsTraining);
		System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));
		
		// create a blocker (blocking strategy)
		StandardRecordBlocker<Song, Attribute> blocker = new StandardRecordBlocker<Song, Attribute>(new SongBlockingKeyByNameGenerator());
//		SortedNeighbourhoodBlocker<Song, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new SongBlockingKeyByDecadeGenerator(), 1);
		blocker.collectBlockSizeData("data/output/debugResultsBlocking_ml.csv", 100);
		
		// Initialize Matching Engine
		MatchingEngine<Song, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		System.out.println("*\n*\tRunning identity resolution\n*");
		Processable<Correspondence<Song, Attribute>> correspondences = engine.runIdentityResolution(
				dbpedia, musicbrainz, null, matchingRule,
				blocker);

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/dbpedia_musicbrainz_correspondences_ml.csv"), correspondences);	

		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gs_dbpedia_musicbrainz_test_ml.csv"));
		
		// evaluate your result
		System.out.println("*\n*\tEvaluating result\n*");
		MatchingEvaluator<Song, Attribute> evaluator = new MatchingEvaluator<Song, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
				gsTest);
		
		// print the evaluation result
		System.out.println("DBpedia <-> Musicbrainz");
		System.out.println(String.format(
				"Precision: %.4f",perfTest.getPrecision()));
		System.out.println(String.format(
				"Recall: %.4f",	perfTest.getRecall()));
		System.out.println(String.format(
				"F1: %.4f",perfTest.getF1()));
    }
}

package com.team5.maven.IdentityResolution;

import java.io.File;

import org.slf4j.Logger;

import com.team5.maven.IdentityResolution.blocking.SongBlockingKeyByArtistGenerator;
import com.team5.maven.IdentityResolution.blocking.SongBlockingKeyByNameGenerator;
import com.team5.maven.IdentityResolution.comparators.SongAlbumComparatorMaxSim;
import com.team5.maven.IdentityResolution.comparators.SongAlbumComparatorToken;
import com.team5.maven.IdentityResolution.comparators.SongArtistCustomizedComparator;
import com.team5.maven.IdentityResolution.comparators.SongArtistsComparatorLevenshtein;
import com.team5.maven.IdentityResolution.comparators.SongArtistsComparatorToken;
import com.team5.maven.IdentityResolution.comparators.SongDateComparator2Years;
import com.team5.maven.IdentityResolution.comparators.SongNameComparatorEqual;
import com.team5.maven.IdentityResolution.comparators.SongNameComparatorLevenshtein;
import com.team5.maven.IdentityResolution.comparators.SongNameComparatorRemoveBracketsAndDash;
import com.team5.maven.IdentityResolution.model.Song;
import com.team5.maven.IdentityResolution.model.SongXMLReader;

import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.NoBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.SortedNeighbourhoodBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class IR_using_linear_combination_spotify_musicbrainz {

	private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	
    public static void main( String[] args ) throws Exception
    { 
		// loading data
    	
		System.out.println("*\n*\tLoading datasets\n*");
		HashedDataSet<Song, Attribute> spotify = new HashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/spotify_translated.xml"), "/songs/song", spotify);
		HashedDataSet<Song, Attribute> musicbrainz = new HashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/musicbrainz_translated.xml"), "/songs/song", musicbrainz);

		// load the gold standard (test set)
		System.out.println("*\n*\tLoading gold standard\n*");
		MatchingGoldStandard gsTest = new MatchingGoldStandard();
		gsTest.loadFromCSVFile(new File(
				"data/goldstandard/gs_spotify_musicbrainz_test.csv"));

		// create a matching rule
		LinearCombinationMatchingRule<Song, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
				0.7);
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule_spotify_musicbrainz.csv", 10000, gsTest);
		
		// add comparators
		//matchingRule.addComparator(new SongNameComparatorLevenshtein(), 0.5);
		matchingRule.addComparator(new SongNameComparatorRemoveBracketsAndDash(), 0.6);
		matchingRule.addComparator(new SongArtistCustomizedComparator(), 0.35);
		matchingRule.addComparator(new SongDateComparator2Years(), 0.05);
		//matchingRule.addComparator(new SongArtistsComparatorToken(), 0.2);
		//matchingRule.addComparator(new SongArtistsComparatorLevenshtein(), 0.3);
		

		// create a blocker (blocking strategy)
		
		StandardRecordBlocker<Song, Attribute> blocker = new StandardRecordBlocker<Song, Attribute>(new SongBlockingKeyByNameGenerator());
		//StandardRecordBlocker<Song, Attribute> blocker = new StandardRecordBlocker<Song, Attribute>(new SongBlockingKeyByArtistGenerator());

		//NoBlocker<Song, Attribute> blocker = new NoBlocker<>();
 		
		//SortedNeighbourhoodBlocker<Song, Attribute, Attribute> blocker = new SortedNeighbourhoodBlocker<>(new SongBlockingKeyByNameGenerator(), 1000);
		
		blocker.setMeasureBlockSizes(true);
		//Write debug results to file:
		blocker.collectBlockSizeData("data/output/debugResultsBlocking_spotify_musicbrainz.csv", 1000);
		
		// Initialize Matching Engine
		MatchingEngine<Song, Attribute> engine = new MatchingEngine<>();

		// Execute the matching
		System.out.println("*\n*\tRunning identity resolution\n*");
		Processable<Correspondence<Song, Attribute>> correspondences = engine.runIdentityResolution(
				spotify, musicbrainz, null, matchingRule,
				blocker);

		// Create a top-1 global matching
		// correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

//		 Alternative: Create a maximum-weight, bipartite matching
//		 MaximumBipartiteMatchingAlgorithm<Movie,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
//		 maxWeight.run();
//		 correspondences = maxWeight.getResult();

		// write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/spotify_musicbrainz_correspondences.csv"), correspondences);	
		
		System.out.println("*\n*\tEvaluating result\n*");
		// evaluate your result
		MatchingEvaluator<Song, Attribute> evaluator = new MatchingEvaluator<Song, Attribute>();
		Performance perfTest = evaluator.evaluateMatching(correspondences,
				gsTest);

		// print the evaluation result
		System.out.println("Spotify <-> Musicbrainz");
		System.out.println(String.format(
				"Precision: %.4f",perfTest.getPrecision()));
		System.out.println(String.format(
				"Recall: %.4f",	perfTest.getRecall()));
		System.out.println(String.format(
				"F1: %.4f",perfTest.getF1()));
    }
    
}

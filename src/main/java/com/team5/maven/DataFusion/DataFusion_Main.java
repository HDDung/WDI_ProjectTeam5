package com.team5.maven.DataFusion;

import java.io.File;

//import org.apache.logging.log4j.Logger;
import org.slf4j.Logger;

import com.team5.maven.DataFusion.evaluation.AlbumEvaluationRule;
import com.team5.maven.DataFusion.evaluation.ArtistEvaluationRule;
import com.team5.maven.DataFusion.evaluation.GenreEvaluationRule;
import com.team5.maven.DataFusion.evaluation.NameEvaluationRule;
import com.team5.maven.DataFusion.evaluation.ProducerEvaluationRule;
import com.team5.maven.DataFusion.evaluation.RecordLabelEvaluationRule;
import com.team5.maven.DataFusion.evaluation.WriterEvaluationRule;
import com.team5.maven.DataFusion.evaluation.YearEvaluationRule;
import com.team5.maven.DataFusion.fusers.AlbumFuserLongestString;
import com.team5.maven.DataFusion.fusers.ArtistFuserLongestString;
import com.team5.maven.DataFusion.fusers.GenreFuserLongestString;
import com.team5.maven.DataFusion.fusers.NameFuserLongestString;
import com.team5.maven.DataFusion.fusers.ProducerFuserLongestString;
import com.team5.maven.DataFusion.fusers.RecordLabelFuserLongestString;
import com.team5.maven.DataFusion.fusers.WriterFuserLongestString;
import com.team5.maven.DataFusion.fusers.YearFuserVoting;
import com.team5.maven.IdentityResolution.model.Song;
import com.team5.maven.IdentityResolution.model.SongXMLFormatter;
import com.team5.maven.IdentityResolution.model.SongXMLReader;

import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroupFactory;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;

public class DataFusion_Main 
{
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

	private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	
	public static void main( String[] args ) throws Exception
    {
		// Load the Data into FusibleDataSet
		System.out.println("*\n*\tLoading datasets\n*");
		FusibleDataSet<Song, Attribute> ds1 = new FusibleHashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/dbpedia_translated.xml"), "/songs/song", ds1);
		ds1.printDataSetDensityReport();

		FusibleDataSet<Song, Attribute> ds2 = new FusibleHashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/musicbrainz_translated.xml"), "/songs/song", ds2);
		ds2.printDataSetDensityReport();

		FusibleDataSet<Song, Attribute> ds3 = new FusibleHashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/input/spotify_translated.xml"), "/songs/song", ds3);
		ds3.printDataSetDensityReport();

		// Maintain Provenance
		// Scores (e.g. from rating)
		ds1.setScore(1.0);
		ds2.setScore(1.0);
		ds3.setScore(1.0);

		// Date (e.g. last update)
//		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
//		        .appendPattern("yyyy-MM-dd")
//		        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
//		        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
//		        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
//		        .toFormatter(Locale.ENGLISH);
		
		//ds1.setDate(LocalDateTime.parse("2012-01-01", formatter));
		//ds2.setDate(LocalDateTime.parse("2010-01-01", formatter));
		//ds3.setDate(LocalDateTime.parse("2008-01-01", formatter));

		// load correspondences
		System.out.println("*\n*\tLoading correspondences\n*");
		CorrespondenceSet<Song, Attribute> correspondences = new CorrespondenceSet<>();
		correspondences.loadCorrespondences(new File("data/output/dbpedia_musicbrainz_correspondences.csv"),ds1, ds2);
		correspondences.loadCorrespondences(new File("data/output/dbpedia_spotify_correspondences.csv"),ds1, ds3);
		correspondences.loadCorrespondences(new File("data/output/spotify_musicbrainz_correspondences.csv"),ds3, ds2);

		// write group size distribution
		correspondences.printGroupSizeDistribution();

		// load the gold standard
		System.out.println("*\n*\tEvaluating results\n*");
		DataSet<Song, Attribute> gs = new FusibleHashedDataSet<>();
		new SongXMLReader().loadFromXML(new File("data/goldstandard/gold.xml"), "/songs/song", gs);

		for(Song m : gs.get()) {
			System.out.println(String.format("gs: %s", m.getIdentifier()));
		}

		// define the fusion strategy
		DataFusionStrategy<Song, Attribute> strategy = new DataFusionStrategy<>(new SongXMLReader());
		// write debug results to file
		strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);
		
		// add attribute fusers
		strategy.addAttributeFuser(Song.NAME, new NameFuserLongestString(),new NameEvaluationRule());
		strategy.addAttributeFuser(Song.ARTIST,new ArtistFuserLongestString(), new ArtistEvaluationRule());
		strategy.addAttributeFuser(Song.ALBUM, new AlbumFuserLongestString(),new AlbumEvaluationRule());
		strategy.addAttributeFuser(Song.GENRE, new GenreFuserLongestString(),new GenreEvaluationRule());
		strategy.addAttributeFuser(Song.YEAR, new YearFuserVoting(),new YearEvaluationRule());
		//strategy.addAttributeFuser(Song.DURATION,new DurationFuserUnion(),new DurationEvaluationRule());
		strategy.addAttributeFuser(Song.RECORDLABEL,new RecordLabelFuserLongestString(),new RecordLabelEvaluationRule());
		strategy.addAttributeFuser(Song.PRODUCER, new ProducerFuserLongestString(),new ProducerEvaluationRule());
		strategy.addAttributeFuser(Song.WRITER, new WriterFuserLongestString(),new WriterEvaluationRule());
		
		// create the fusion engine
		DataFusionEngine<Song, Attribute> engine = new DataFusionEngine<>(strategy);

		// print consistency report
		engine.printClusterConsistencyReport(correspondences, null);
		
		// print record groups sorted by consistency
		engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

		// run the fusion
		System.out.println("*\n*\tRunning data fusion\n*");
		FusibleDataSet<Song, Attribute> fusedDataSet = engine.run(correspondences, null);

		// write the result
		new SongXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataSet);

		// evaluate
		DataFusionEvaluator<Song, Attribute> evaluator = new DataFusionEvaluator<>(strategy, new RecordGroupFactory<Song, Attribute>());
		
		double accuracy = evaluator.evaluate(fusedDataSet, gs, null);

		System.out.println(String.format("Accuracy: %.2f", accuracy));
    }
}

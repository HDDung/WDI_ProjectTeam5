package com.team5.maven.IdentityResolution.blocking;

import com.team5.maven.IdentityResolution.model.Song;

import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;


public class SongBlockingKeyByNameGenerator2 extends
RecordBlockingKeyGenerator<Song, Attribute> {

	private static final long serialVersionUID = 1L;

	@Override
	public void generateBlockingKeys(Song record, Processable<Correspondence<Attribute, Matchable>> correspondences,
			DataIterator<Pair<String, Song>> resultCollector) {

		String[] tokens  = record.getName().split(" ");

		String blockingKeyValue = "";
		
		// create key from first 2 characters of first token
		blockingKeyValue += tokens[0].substring(0, Math.min(2,tokens[0].length())).toUpperCase();
		
		resultCollector.next(new Pair<>(blockingKeyValue, record));
	}

}

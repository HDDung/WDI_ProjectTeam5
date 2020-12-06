package com.team5.maven.IdentityResolution.blocking;

import java.util.Arrays;

import com.team5.maven.IdentityResolution.model.Song;

import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;


public class SongBlockingKeyByNameGeneratorv3 extends
RecordBlockingKeyGenerator<Song, Attribute> {

	private static final long serialVersionUID = 1L;


	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.matching.blocking.generators.BlockingKeyGenerator#generateBlockingKeys(de.uni_mannheim.informatik.wdi.model.Matchable, de.uni_mannheim.informatik.wdi.model.Result, de.uni_mannheim.informatik.wdi.processing.DatasetIterator)
	 */
	@Override
	public void generateBlockingKeys(Song record, Processable<Correspondence<Attribute, Matchable>> correspondences,
			DataIterator<Pair<String, Song>> resultCollector) {

		String[] name_tokens  = record.getName().split(" ");
		String[] artist_tokens  = record.getArtist().split(",");

		String blockingKeyValue = "";
		
		//create key from first 2 characters of first 3 tokens
		for(int i = 0; i <= 2 && i < name_tokens.length; i++) {
			blockingKeyValue += name_tokens[i].substring(0, Math.min(2,name_tokens[i].length())).toUpperCase();
		}
		
		Arrays.sort(artist_tokens);
		blockingKeyValue += artist_tokens[0].substring(0, Math.min(2,artist_tokens[0].length())).toUpperCase();
		
		
		resultCollector.next(new Pair<>(blockingKeyValue, record));
	}

}

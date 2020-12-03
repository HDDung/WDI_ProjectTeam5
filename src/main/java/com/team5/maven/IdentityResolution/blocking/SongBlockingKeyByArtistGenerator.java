package com.team5.maven.IdentityResolution.blocking;

import java.util.List;

import com.team5.maven.IdentityResolution.model.Song;

import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;


public class SongBlockingKeyByArtistGenerator extends
RecordBlockingKeyGenerator<Song, Attribute> {

	private static final long serialVersionUID = 1L;


	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.matching.blocking.generators.BlockingKeyGenerator#generateBlockingKeys(de.uni_mannheim.informatik.wdi.model.Matchable, de.uni_mannheim.informatik.wdi.model.Result, de.uni_mannheim.informatik.wdi.processing.DatasetIterator)
	 */
	@Override
	public void generateBlockingKeys(Song record, Processable<Correspondence<Attribute, Matchable>> correspondences,
			DataIterator<Pair<String, Song>> resultCollector) {

		String blockingKeyValue = "";
		
		String[] tokens  = record.getArtist().split(" ");
		
		//iterate over first 2 tokens and use the first 2 characters of each as key
		for(int i = 0; i <= 2 && i < tokens.length; i++) {
			blockingKeyValue += tokens[i].substring(0, Math.min(2,tokens[i].length())).toUpperCase();
		}
		
		//iterate over first 2 tokens and use the first 3 characters of each as key
//		for(int i = 0; i <= 2 && i < tokens.length; i++) {
//			blockingKeyValue += tokens[i].substring(0, Math.min(3,tokens[i].length())).toUpperCase();
//		}

		resultCollector.next(new Pair<>(blockingKeyValue, record));
		
		//iterate over first 5 artists and use the first 3 characters of each as key
//		for(int i = 0; i <= 5 && i < tokens.size(); i++) {
//			if(!tokens.get(i).trim().isEmpty()){
//				 blockingKeyValue += tokens.get(i).substring(0, Math.min(3,tokens.get(i).length())).toUpperCase();
//			}
//		}
	}

}

package com.team5.maven.DataFusion.evaluation;

import com.team5.maven.IdentityResolution.model.Song;

import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class DurationEvaluationRule extends EvaluationRule<Song, Attribute> {


	@Override
	public boolean isEqual(Song record1, Song record2, Attribute schemaElement) {
		// duration unit is ms: 1 min = 60000 ms => 0.5 min = 30000
		if (Math.abs(record1.getDuration() - record2.getDuration()) <= 30000) {
			return true;
		}
		return false;
	}


	@Override
	public boolean isEqual(Song record1, Song record2,
			Correspondence<Attribute, Matchable> schemaCorrespondence) {
		return isEqual(record1, record2, (Attribute)null);
	}
	
}

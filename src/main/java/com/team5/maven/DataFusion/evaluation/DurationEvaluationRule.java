package com.team5.maven.DataFusion.evaluation;

import com.team5.maven.IdentityResolution.model.Song;

import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class DurationEvaluationRule extends EvaluationRule<Song, Attribute> {


	@Override
	public boolean isEqual(Song record1, Song record2, Attribute schemaElement) {
		// duration unit is ms: 1 min = 60000 ms => 1.5 min = 90000
		if (Math.abs(record1.getDuration() - record2.getDuration()) <= 90000) {
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.datafusion.EvaluationRule#isEqual(java.lang.Object, java.lang.Object, de.uni_mannheim.informatik.wdi.model.Correspondence)
	 */
	@Override
	public boolean isEqual(Song record1, Song record2,
			Correspondence<Attribute, Matchable> schemaCorrespondence) {
		return isEqual(record1, record2, (Attribute)null);
	}
	
}
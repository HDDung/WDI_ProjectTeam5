package com.team5.maven.DataFusion.fusers;

import com.team5.maven.IdentityResolution.model.Song;

import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.LongestString;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class RecordLabelFuserLongestString extends
		AttributeValueFuser<String, Song, Attribute> {

	public RecordLabelFuserLongestString() {
		super(new LongestString<Song, Attribute>());
	}

	@Override
	public boolean hasValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Song.RECORDLABEL);
	}

	@Override
	public String getValue(Song record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getRecordLabel();
	}

	@Override
	public void fuse(RecordGroup<Song, Attribute> group, Song fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<String, Song, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setRecordLabel(fused.getValue());
		fusedRecord.setAttributeProvenance(Song.RECORDLABEL,
				fused.getOriginalIds());
	}

}

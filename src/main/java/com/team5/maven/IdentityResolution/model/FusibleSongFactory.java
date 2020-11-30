package com.team5.maven.IdentityResolution.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class FusibleSongFactory implements FusibleFactory<Song, Attribute> {

	@Override
	public Song createInstanceForFusion(RecordGroup<Song, Attribute> cluster) {

		List<String> ids = new LinkedList<>();

		for (Song s : cluster.getRecords()) {
			ids.add(s.getIdentifier());
		}

		Collections.sort(ids);

		String mergedId = StringUtils.join(ids, '+');

		return new Song(mergedId, "fused");
	}
	
}

package com.team5.maven.IdentityResolution.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class SongXMLReader extends XMLMatchableReader<Song, Attribute> implements
FusibleFactory<Song, Attribute>  {

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	protected void initialiseDataset(DataSet<Song, Attribute> dataset) {
		super.initialiseDataset(dataset);
		
		dataset.addAttribute(Song.NAME);
		dataset.addAttribute(Song.ARTIST);
		dataset.addAttribute(Song.ALBUM);
		dataset.addAttribute(Song.YEAR);
		dataset.addAttribute(Song.DURATION);
		
	}
	
	@Override
	public Song createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Song song = new Song(id, provenanceInfo);

		// fill the attributes
		song.setName(getValueFromChildElement(node, "name"));
		
		song.setArtist(getValueFromChildElement(node, "artists"));
		
		song.setAlbum(getValueFromChildElement(node, "albums"));

		// load the list of actors
//		List<Actor> actors = getObjectListFromChildElement(node, "actors",
//				"actor", new ActorXMLReader(), provenanceInfo);
//		movie.setActors(actors);

		return song;
	}
	
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

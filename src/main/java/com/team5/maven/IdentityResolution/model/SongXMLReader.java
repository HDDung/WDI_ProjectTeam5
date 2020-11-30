package com.team5.maven.IdentityResolution.model;

import java.util.List;

import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

public class SongXMLReader extends XMLMatchableReader<Song, Attribute>  {

	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.io.XMLMatchableReader#initialiseDataset(de.uni_mannheim.informatik.wdi.model.DataSet)
	 */
	@Override
	protected void initialiseDataset(DataSet<Song, Attribute> dataset) {
		super.initialiseDataset(dataset);
		
	}
	
	@Override
	public Song createModelFromElement(Node node, String provenanceInfo) {
		String id = getValueFromChildElement(node, "id");

		// create the object with id and provenance information
		Song song = new Song(id, provenanceInfo);

		// fill the attributes
		song.setName(getValueFromChildElement(node, "name"));
		// load the list of artists
		List<String> artists = getListFromChildElement(node, "artists");
		//System.out.println(artists);
		song.setArtist(artists);

		// load the list of actors
//		List<Actor> actors = getObjectListFromChildElement(node, "actors",
//				"actor", new ActorXMLReader(), provenanceInfo);
//		movie.setActors(actors);

		return song;
	}

}

package com.team5.maven.IdentityResolution.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.team5.maven.common.Utilities;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class Song extends AbstractRecord<Attribute> implements Serializable {

	/*
	 * example entry <actor> <name>Janet Gaynor</name>
	 * <birthday>1906-01-01</birthday> <birthplace>Pennsylvania</birthplace>
	 * </actor>
	 */

	private static final long serialVersionUID = 1L;
	private String name;
	private String artist;
	private String genre;
	private String album;
	private Double duration;
	private LocalDateTime year;
	private String recordlabel;
	private String producer;
	private String writer;

	public Song(String identifier, String provenance) {
		super(identifier, provenance);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		name = Utilities.html2text(name);
		this.name = name;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		artist = Utilities.html2text(artist);
		this.artist = artist;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		genre = Utilities.html2text(genre);
		this.genre = genre;
	}
	
	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		album = Utilities.html2text(album);
		this.album = album;
	}
	
	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
	}
	
	public LocalDateTime getYear() {
		return year;
	}

	public void setYear(LocalDateTime year) {
		this.year = year;
	}
	
	public String getRecordLabel() {
		return recordlabel;
	}

	public void setRecordLabel(String recordlabel) {
		recordlabel = Utilities.html2text(recordlabel);
		this.recordlabel = recordlabel;
	}
	
	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		producer = Utilities.html2text(producer);
		this.producer = producer;
	}
	
	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		writer = Utilities.html2text(writer);
		this.writer = writer;
	}

	private Map<Attribute, Collection<String>> provenance = new HashMap<>();
	private Collection<String> recordProvenance;

	public void setRecordProvenance(Collection<String> provenance) {
		recordProvenance = provenance;
	}

	public Collection<String> getRecordProvenance() {
		return recordProvenance;
	}

	public void setAttributeProvenance(Attribute attribute,
			Collection<String> provenance) {
		this.provenance.put(attribute, provenance);
	}

	public Collection<String> getAttributeProvenance(String attribute) {
		return provenance.get(attribute);
	}

	public String getMergedAttributeProvenance(Attribute attribute) {
		Collection<String> prov = provenance.get(attribute);

		if (prov != null) {
			return StringUtils.join(prov, "+");
		} else {
			return "";
		}
	}	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 31 + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Song other = (Song) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public static final Attribute NAME = new Attribute("Name");
	public static final Attribute ARTIST = new Attribute("Artist");
	public static final Attribute GENRE = new Attribute("Genre");
	public static final Attribute ALBUM = new Attribute("Album");
	public static final Attribute DURATION = new Attribute("Duration");
	public static final Attribute YEAR = new Attribute("Year");
	public static final Attribute RECORDLABEL = new Attribute("RecordLabel");
	public static final Attribute PRODUCER = new Attribute("Producer");
	public static final Attribute WRITER = new Attribute("Writer");
	
	/* (non-Javadoc)
	 * @see de.uni_mannheim.informatik.wdi.model.Record#hasValue(java.lang.Object)
	 */
	@Override
	public boolean hasValue(Attribute attribute) {
		if(attribute==NAME)
			return name!=null && !getName().isEmpty();
		else if(attribute==ARTIST) 
			return artist!=null && !getArtist().isEmpty();
		else if(attribute==GENRE)
			return genre!=null && !getGenre().isEmpty();
		else if(attribute==ALBUM) 
			return album!=null && !getAlbum().isEmpty();
		else if(attribute==DURATION)
			return duration!=0 && duration!=-1;
		else if(attribute==YEAR) 
			return year!=null;
		else if(attribute==RECORDLABEL)
			return recordlabel!=null && !getRecordLabel().isEmpty();
		else if(attribute==PRODUCER) 
			return producer!=null && !getProducer().isEmpty();
		else if(attribute==WRITER)
			return writer!=null && !getWriter().isEmpty();
		return false;
	}
}
